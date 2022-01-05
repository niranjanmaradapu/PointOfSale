package com.otsi.retail.promoexchange.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.config.Config;
import com.otsi.retail.promoexchange.exceptions.InvalidDataException;
import com.otsi.retail.promoexchange.exceptions.RecordNotFoundException;
import com.otsi.retail.promoexchange.gateway.GateWayResponse;
import com.otsi.retail.promoexchange.mapper.PromoExchangeMapper;
import com.otsi.retail.promoexchange.repository.CustomerDetailsRepo;
import com.otsi.retail.promoexchange.repository.PromoExchangeRepository;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

@Component
public class PromoExchangeServiceeImpl implements PromoExchangeService {

	//private Logger log = LoggerFactory.getLogger(PromoExchangeServiceeImpl.class);
	
	  private Logger log= LogManager.getLogger(PromoExchangeServiceeImpl.class);

	@Autowired
	private RestTemplate template;

	// Rest call
	

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private PromoExchangeMapper promoExchangeMapper;

	@Autowired
	private PromoExchangeRepository promoExchangeRepository;

	@Autowired
	Config config;
	@Autowired
	private RestTemplate restTemplate;


	@Override
	public String savePromoItemExchangeRequest(PromoExchangeVo vo) {
		log.debug("debugging savePromoItemExchangeRequest:" + vo);
		if (vo.getDlSlip() == null || vo.getReturnSlips() == null || vo.getCustomerDetails() == null) {
			log.error("please enter valid data");
			throw new InvalidDataException("please enter valid data");
		}

		Random ran = new Random();

		PromoExchangeEntity entity = new PromoExchangeEntity();

		if (vo.getCustomerDetails() != null) {

			try {
				customerService.saveCustomerDetails(vo.getCustomerDetails());
			} catch (Exception e) {

				e.printStackTrace();
			}

			List<DeliverySlipVo> dlSlips = vo.getDlSlip();
			List<ListOfReturnSlipsVo> returnSlips = vo.getReturnSlips();
			entity.setPayType(vo.getPayType());
			entity.setGrossAmount(dlSlips.stream().mapToLong(i -> i.getMrp()).sum());
			entity.setTotalPromoDisc(dlSlips.stream().mapToLong(i -> i.getPromoDisc()).sum());
			entity.setTotalManualDisc(vo.getTotalManualDisc());
			entity.setCreatedDate(LocalDate.now());
			entity.setNetPayableAmount(dlSlips.stream().mapToLong(i -> i.getNetAmount()).sum());
			entity.setRecievedAmount(returnSlips.stream().mapToLong(r -> r.getAmount()).sum());
			Long balanceAmount = entity.getNetPayableAmount() - entity.getRecievedAmount();

			System.out.println("net payable amount:" + entity.getNetPayableAmount());
			System.out.println("recieved amount:" + entity.getRecievedAmount());
			System.out.println("balance amount:" + balanceAmount);

			Long net = dlSlips.stream().mapToLong(i -> i.getNetAmount()).sum() - vo.getTotalManualDisc();

			entity.setNetPayableAmount(net);
			entity.setBalanceAmount(balanceAmount);
			entity.setBillNumber(
					"KLM/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());

			List<String> dlsList = dlSlips.stream().map(x -> x.getDsNumber()).collect(Collectors.toList());
			PromoExchangeEntity saveEntity = promoExchangeRepository.save(entity);
		}
		log.warn("we wre checking if promotion is saved...");
		log.info("Bill generated with number :" + entity.getBillNumber());
		return "Bill generated with number :" + entity.getBillNumber();

	}

	@Override
	public DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws Exception {
		log.debug("debugging getDeliverySlipDetails:" + dsNumber);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		URI uri = null;
		// try {

		uri = UriComponentsBuilder.fromUri(new URI(config.getGetDsDetails() + "?dsNumber=" + dsNumber)).build()
				.encode().toUri();
		ResponseEntity<?> deliverySlipResponse = restTemplate.exchange(uri, HttpMethod.GET, entity,
				GateWayResponse.class);
		// return vo.getBody();

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(deliverySlipResponse.getBody(), GateWayResponse.class);

		DeliverySlipVo vo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<DeliverySlipVo>() {
		});
		log.warn("we re checking if delivery slip details is fetching...");
		log.info("after fetching delivery slip details:" + vo);
		return vo;
	}

	@Override
	public List<ListOfReturnSlipsVo> getListOfRetunSlips() throws JsonMappingException, JsonProcessingException {
		log.debug("debugging getListOfRetunSlips()");
		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetListOfReturnSlipsUrl(), HttpMethod.GET, null,
				GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		List<ListOfReturnSlipsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<ListOfReturnSlipsVo>>() {
				});
		log.warn("we re checking if return slip details is fetching...");
		log.info("after fetching return slip details:" + vo);
		return vo;
	}

	@Override
	public List<PromoExchangeVo> getListOfSaleBills() {
		log.debug("debugging getListOfSaleBills()");
		List<PromoExchangeEntity> pmodel = promoExchangeRepository.findAll();

		if (pmodel.isEmpty()) {
			log.error("Record not found");
			throw new RecordNotFoundException("Record not found");
		}
		List<PromoExchangeVo> pvo = promoExchangeMapper.mapEntityToVo(pmodel);
		log.warn("we re checking if list of sale bills details is fetching...");
		log.info("after fetching list of sale billsdetails:" + pvo);
		return pvo;
	}

	@Override
	public List<PromoExchangeVo> getSaleBillByBillNumber(String billNumber) {
		log.debug("debugging getSaleBillByBillNumber():" + billNumber);
		List<PromoExchangeEntity> pmodel = promoExchangeRepository.findByBillNumber(billNumber);

		if (pmodel.isEmpty()) {
			log.error("Record not found");
			throw new RecordNotFoundException("Record not found");
		}

		List<PromoExchangeVo> pvo = promoExchangeMapper.mapEntityToVo(pmodel);
		log.warn("we re checking if sale bills details is fetching with bill number...");
		log.info("after fetching sale bills with bill number:" + pvo);
		return pvo;

	}

}
