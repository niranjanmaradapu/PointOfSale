package com.otsi.retail.connectionpool.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.connectionpool.config.Config;
import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromoBarcodeEntity;
import com.otsi.retail.connectionpool.entity.PromotionToStoreEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
import com.otsi.retail.connectionpool.exceptions.DuplicateRecordException;
import com.otsi.retail.connectionpool.exceptions.InvalidDataException;
import com.otsi.retail.connectionpool.exceptions.RecordNotFoundException;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.mapper.PromotionMapper;
import com.otsi.retail.connectionpool.repository.PoolRepo;
import com.otsi.retail.connectionpool.repository.PromoBarcodeEntityRepository;
import com.otsi.retail.connectionpool.repository.PromotionRepo;
import com.otsi.retail.connectionpool.repository.PromotionToStoreRepo;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.LineItemVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.SearchPromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;

/**
 * This class contains all Bussiness logics regarding promotions and their
 * management
 * 
 * @author Manikanta Guptha
 *
 */

@Service
public class PromotionServiceImpl implements PromotionService {

	private Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

	@Autowired
	private PromotionMapper promoMapper;

	@Autowired
	private PromotionRepo promoRepo;

	@Autowired
	private PoolRepo poolRepo;
	@Autowired
	private PromotionToStoreRepo promostoreRepo;

	@Autowired
	private Config config;

	@Autowired
	private RestTemplate template;

	@Autowired
	private PromoBarcodeEntityRepository promoBarcodeRepo;

//	@Autowired
//	private StoreRepo storeRepo;

	// Method for adding promotion to pools
	@Override
	public String addPromotion(PromotionsVo vo) {
		log.debug("debugging addPromotion:" + vo);
		if (/* vo.getStoreVo() == null || */ vo.getPoolVo() == null) {
			throw new InvalidDataException("please give valid data");
		}
		List<ConnectionPoolVo> poolVo = vo.getPoolVo();

		List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

		List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

		// Code added by sudheer for store mappping
		// List<StoreVo> storeVo = vo.getStoreVo();

		/** Promotion mapped to store by storeId **/

		// List<Long> storeId = storeVo.stream().map(a ->
		// a.getStoreId()).collect(Collectors.toList());
		// List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

		/** Promotion mapped to store by storeName **/

		// List<String> storeName = storeVo.stream().map(b ->
		// b.getStoreName()).collect(Collectors.toList());
		// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

		PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList/* , storeList */);

		if (poolVo.size() == poolList.size() /* && storeVo.size() == storeList.size() */) {

			PromotionsEntity savedPromo = promoRepo.save(entity);

		} else {
			log.error("Please give valid/active pools");
			throw new InvalidDataException("Please give valid/active pools");
		}
		log.warn("we are checking if pool is added...");
		log.info("Promotion mapped succesfully...");
		return "Promotion mapped succesfully...";
	}

	// Method for getting list of Promotions by using flag(Status)
	@Override
	public List<PromotionsVo> getListOfPromotions(String flag) {
		log.debug("debugging getListOfPromotions:" + flag);
		List<PromotionsEntity> promoList = new ArrayList<>();

		Boolean status = null;
		if (flag.equalsIgnoreCase("true")) {
			status = Boolean.TRUE;
		}
		if (flag.equalsIgnoreCase("false")) {
			status = Boolean.FALSE;
		}
		if (flag.equalsIgnoreCase("all")) {
			promoList = promoRepo.findAll();
		} else {
			promoList = promoRepo.findByIsActive(status);
		}
		if (!promoList.isEmpty()) {
			List<PromotionsVo> listOfPromo = promoMapper.convertPromoEntityToVo(promoList);
			log.warn("we are checking if list of promotions is fetching...");
			log.info("list of promotions is fetching...");
			return listOfPromo;
		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}

	}

	// Method for modifying/editing Promotion
	@Override
	public String editPromotion(PromotionsVo vo) {
		log.debug("debugging editPromotion:" + vo);
		if (vo.getPoolVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}
		/* try { */
		Optional<PromotionsEntity> promotion = promoRepo.findById(vo.getPromoId());

		if (promotion.isPresent()) {
			List<ConnectionPoolVo> poolVo = vo.getPoolVo();

			List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

			List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

			// Code added by sudheer
			// List<StoreVo> storeVo = vo.getStoreVo();

			/** Promotion mapped to store by storeId **/

			// List<Long> storeId = storeVo.stream().map(a ->
			// a.getStoreId()).collect(Collectors.toList());
			// List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

			/** Promotion mapped to store by storeName **/

			// List<String> storeName = storeVo.stream().map(b ->
			// b.getStoreName()).collect(Collectors.toList());
			// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

			if (poolVo.size() == poolList.size()) {

				PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList/* , storeList */);
				PromotionsEntity savedPromo = promoRepo.save(entity);

			} else {
				log.error("Please give valid/active pools");
				throw new InvalidDataException("Please give valid/active pools");
			}

			log.warn("we wre checking if pool is modified...");
			log.info("Promotion Modified succesfully");
			return "Promotion Modified succesfully";

		} else {
			log.error("please give valid Promotion Id");
			throw new RecordNotFoundException("please give valid Promotion Id");
		}
		/*
		 * } catch (Exception e) { throw new
		 * Exception("Exception occurs while modifying Promotion"); }
		 */
	}

	@Override
	public String deletePromotion(Long id) {

		Optional<PromotionsEntity> entity = promoRepo.findById(id);

		if (entity.isPresent()) {
			promoRepo.deleteByPromoId(id);
		} else {
			throw new RecordNotFoundException("Given promotion is not exists");
		}
		return "Promotion Deleted Successfully";
	}

	@Override
	public List<StoreVo> getAllStores() {
		List<StoreVo> vo = new ArrayList<StoreVo>();
		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetAllStoresFromURM(), HttpMethod.GET,
				null, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		vo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVo>>() {
		});

		return vo;
	}

	@Override
	public String addPromotionToStore(PromotionsVo vo) {

		PromotionsEntity promotionEntity = promoRepo.findByPromoNameIs(vo.getPromoName());

		if (promotionEntity != null) {

			promotionEntity.setPromoType(vo.getPromoType());
			promotionEntity.setEndDate(vo.getEndDate());
			promotionEntity.setStartDate(vo.getStartDate());
			promotionEntity.setStoreName(vo.getStoreVo().getName());
			promoRepo.save(promotionEntity);

		}

		Long promoId = promotionEntity.getPromoId();
		Long storeId = vo.getStoreVo().getId();
		PromotionToStoreEntity entity = new PromotionToStoreEntity();
		entity.setPromoId(promoId);
		entity.setStoreId(storeId);

		promostoreRepo.save(entity);

		return "Promotion Mapped Successfully";
	}

	@Override
	public List<SearchPromotionsVo> searchPromotion(SearchPromotionsVo vo) {
		List<SearchPromotionsVo> searchPromoVo = new ArrayList<SearchPromotionsVo>();
		List<PromotionsEntity> promoDetails = new ArrayList<PromotionsEntity>();
		PromotionsEntity promoEntity = new PromotionsEntity();

		if (vo.getStartDate() != null && vo.getEndDate() != null) {
			if (vo.getPromotionName() != null && vo.getStoreName() != null) {

				promoDetails = promoRepo.findByStartDateAndEndDateAndPromoNameAndStoreNameAndIsActive(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionName(), vo.getStoreName(), vo.isPromotionStatus());
			} else if (vo.getStoreName() != null && vo.getPromotionName() == null) {
				promoDetails = promoRepo.findByStartDateAndEndDateAndStoreNameAndIsActive(vo.getStartDate(),
						vo.getEndDate(), vo.getStoreName(), vo.isPromotionStatus());

			} else if (vo.getPromotionName() != null && vo.getStoreName() == null) {
				promoDetails = promoRepo.findByStartDateAndEndDateAndPromoNameAndIsActive(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionName(), vo.isPromotionStatus());

			} else
				promoDetails = promoRepo.findByStartDateAndEndDateAndIsActive(vo.getStartDate(), vo.getEndDate(),
						vo.isPromotionStatus());

		} else if (vo.getStartDate() == null && vo.getEndDate() == null) {
			if (vo.getPromotionName() != null && vo.getStoreName() == null) {
				promoDetails = promoRepo.findByPromoName(vo.getPromotionName());
				// promoDetails.add(promoEntity);

			} else if (vo.getStoreName() != null && vo.getPromotionName() == null) {
				promoDetails = promoRepo.findByStoreName(vo.getStoreName());

			} else if (vo.getPromotionName() != null && vo.getStoreName() != null) {
				promoDetails = promoRepo.findByStoreNameAndPromoName(vo.getStoreName(), vo.getPromotionName());
			}

			else
				promoDetails = promoRepo.findByIsActive(vo.isPromotionStatus());

		}
		if (promoDetails.isEmpty()) {
			throw new RecordNotFoundException("No record found with given information");

		}

		else {
			Long promoCount = promoDetails.stream().count();
			promoDetails.stream().forEach(p -> {
				SearchPromotionsVo searchVo = new SearchPromotionsVo();
				searchVo.setPromotionName(p.getPromoName());
				searchVo.setStoreName(p.getStoreName());
				searchVo.setStartDate(p.getStartDate());
				searchVo.setEndDate(p.getEndDate());
				searchVo.setPromotionStatus(p.getIsActive());
				searchVo.setPromoId(p.getPromoId());
				searchVo.setPriority(p.getPriority());
				searchVo.setPromotionsCount(promoCount);

				searchPromoVo.add(searchVo);
			});

		}

		return searchPromoVo;
	}

	@Override
	public List<SearchPromotionsVo> searchByStore(SearchPromotionsVo vo) {

		List<SearchPromotionsVo> searchPromoVo = new ArrayList<SearchPromotionsVo>();
		List<PromotionsEntity> promoDetails = new ArrayList<PromotionsEntity>();

		if (vo.getStoreName() != null) {
			promoDetails = promoRepo.findByStoreName(vo.getStoreName());

		}
		if (promoDetails.isEmpty()) {
			throw new RecordNotFoundException("No record found with given information");

		} else {
			Long promoCount = promoDetails.stream().count();
			promoDetails.stream().forEach(p -> {
				SearchPromotionsVo searchVo = new SearchPromotionsVo();
				searchVo.setPromotionName(p.getPromoName());
				searchVo.setStoreName(p.getStoreName());
				searchVo.setStartDate(p.getStartDate());
				searchVo.setEndDate(p.getEndDate());
				searchVo.setPromotionStatus(p.getIsActive());
				searchVo.setPromoId(p.getPromoId());
				searchVo.setPriority(p.getPriority());
				searchVo.setPromotionsCount(promoCount);

				searchPromoVo.add(searchVo);
			});

		}

		return searchPromoVo;

	}

	@Override
	public String updatePriority(SearchPromotionsVo vo) {

		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}

		PromotionsEntity dto = promoRepo.findById(vo.getPromoId()).get();

		if (Objects.nonNull(vo.getPriority())) {
			dto.setPriority(vo.getPriority());
		}
		PromotionsEntity entity = promoRepo.save(dto);

		return "Promotion Priority Updated Successfully";

	}

	@Override
	public String updatePromotionDates(SearchPromotionsVo vo) {
		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}

		PromotionsEntity dto = promoRepo.findById(vo.getPromoId()).get();

		if (Objects.nonNull(vo.getStartDate()) && Objects.nonNull(vo.getEndDate())
				&& Objects.nonNull(vo.isPromotionStatus())) {
			dto.setPromoId(vo.getPromoId());
			dto.setStartDate(vo.getStartDate());
			;
			dto.setEndDate(vo.getEndDate());
			dto.setIsActive(vo.isPromotionStatus());
		}

		PromotionsEntity pEntity = promoRepo.save(dto);

		return "Promotion Dates Updated Successfully";
	}

	@Override
	public String clonePromotionByStore(SearchPromotionsVo vo) {

		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}
		if (promoRepo.existsByStoreName(vo.getStoreName())) {

			throw new DuplicateRecordException("Store name already exists");

		}

		// Random number generator and fixed ranges are setted
		int min = 100, max = 1000;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

		SearchPromotionsVo svo = new SearchPromotionsVo();
		Optional<PromotionsEntity> dto = promoRepo.findById(vo.getPromoId());
		PromotionsEntity newDto = new PromotionsEntity();
		System.out.println(dto.get().getStoreName());
		if (!(dto.get().getStoreName().equals(vo.getStoreName()))) {
			BeanUtils.copyProperties(dto, newDto);
			newDto.setStartDate(dto.get().getStartDate());
			newDto.setEndDate(dto.get().getEndDate());
			newDto.setPromoName(dto.get().getPromoName());
			newDto.setIsActive(true);
			newDto.setPriority(randomNum);
			newDto.setStoreName(vo.getStoreName());
			newDto.setCreatedDate(LocalDate.now());
			newDto.setPromoType(dto.get().getPromoType());
			newDto.setDescription(dto.get().getDescription());
			newDto.setPromoApplyType(dto.get().getPromoApplyType());
			newDto.setIsTaxExtra(dto.get().getIsTaxExtra());
			newDto.setLastModified(LocalDate.now());
			newDto.setPrintNameOnBill(dto.get().getPrintNameOnBill());
			newDto.setApplicability(dto.get().getApplicability());
			newDto.setBuyItemsFromPool(dto.get().getBuyItemsFromPool());
			promoRepo.save(newDto);
		} else {

			throw new InvalidDataException("Given Store are already mapped to the promotion");
		}

		return "Promotion Cloned Successfully";
	}

	// Method for adding promotion to product
	@Override
	public String addPromtionToBarcode(Long promoId, String barcode) {

		PromotionsEntity promo = promoRepo.findByPromoId(promoId);

		if (promo != null) {

			PromoBarcodeEntity entity = new PromoBarcodeEntity();
			entity.setPromoId(promoId);
			entity.setBarCode(barcode);

			promoBarcodeRepo.save(entity);

		} else {
			return "Promotion is not valid";
		}

		return "Successfully add promotion to Barcode";
	}

	public static Integer i;

	// Method for checking promotion to all line items
	@Override
	public List<LineItemVo> checkPromtion(List<LineItemVo> lineItems) {

		List<LineItemVo> result = new ArrayList<>();

		lineItems.stream().forEach(x -> {

			PromoBarcodeEntity promoBar = promoBarcodeRepo.findByBarCode(x.getBarCode());

			if (promoBar != null) {

				PromotionsEntity promotion = promoRepo.findByPromoId(promoBar.getPromoId());

				if (promotion != null) {
					promotion.getPoolEntity().stream().forEach(y -> {
						Map<Integer, Long> range = new HashMap<>();

						y.getRuleEntity().stream().forEach(z -> {

							range.put(i++, z.getGivenValue());

						});
						if (range.get(0) <= x.getItemPrice() && x.getItemPrice() <= range.get(1)) {

							// x.setItemPrice(y.getPoolPrice());
							x.setNetValue(x.getQuantity() * y.getPoolPrice());
							x.setDiscount(x.getGrossValue() - x.getNetValue());

						}
						i = 0;
					});
				}

			}
			result.add(x);
		});
		return result;
	}

	@Override
	public List<SearchPromotionsVo> listOfPromotionsBySearch(SearchPromotionsVo svo) {

		List<SearchPromotionsVo> searchPromotions = new ArrayList<>();
		List<PromotionsEntity> promotionsList = new ArrayList<>();

		if ((svo.getStartDate() != null) && (svo.getEndDate() != null)) 
		{

			if ((svo.getPromoId() != null) && (svo.getStoreName() != null)) {

				promotionsList = promoRepo.findByStartDateAndEndDateAndPromoIdAndStoreName(svo.getStartDate(),
						svo.getEndDate(), svo.getPromoId(), svo.getStoreName());

			} else if((svo.getPromoId() ==null) && (svo.getStoreName()!=null))
			{
				promotionsList = promoRepo.findByStartDateAndEndDateAndStoreName(svo.getStartDate(), svo.getEndDate(), svo.getStoreName());
			}else if((svo.getPromoId() !=null) && (svo.getStoreName()==null)){
					
				promotionsList = promoRepo.findByStartDateAndEndDateAndPromoId(svo.getStartDate(), svo.getEndDate(), svo.getPromoId());
				
			}
			else {
				
				promotionsList = promoRepo.findByStartDateAndEndDate(svo.getStartDate(), svo.getEndDate());
			}

		}else if((svo.getStartDate() == null) && (svo.getEndDate() ==null))			  
		{
			 if((svo.getPromoId()!=null) && (svo.getStoreName() ==null))
			 {
				 promotionsList = promoRepo.findByPromoIdIs(svo.getPromoId());
			 }
			 else if((svo.getPromoId()!=null) && (svo.getStoreName()!=null))
			 {
				 promotionsList = promoRepo.findByPromoIdAndStoreName(svo.getPromoId(),svo.getStoreName());
			 }else {
				 
				 promotionsList = promoRepo.findByStoreName(svo.getStoreName());
			 }
			
		}
		
		if(promotionsList.isEmpty())
		{
			throw new RecordNotFoundException("Promotins are not exists");
		}
		else {
			
			promotionsList.stream().forEach( r ->{
				
				SearchPromotionsVo searchVo = new SearchPromotionsVo();
				searchVo.setPromoId(r.getPromoId());
				searchVo.setPromotionName(r.getPromoName());
				searchVo.setDescription(r.getDescription());
				searchVo.setStoreName(r.getStoreName());
				searchVo.setStartDate(r.getStartDate());
				searchVo.setEndDate(r.getEndDate());
				searchPromotions.add(searchVo);
			});
			
			
		}

		return searchPromotions;
	}

}
