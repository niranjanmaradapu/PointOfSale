package com.otsi.kalamandir.serviceImp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.otsi.kalamandir.mapper.DeliverySlipMapper;
import com.otsi.kalamandir.model.DeliverySlipModel;
import com.otsi.kalamandir.service.DeliverySlipService;
import com.otsi.kalamandir.vo.DelivarySlipVo;

@Component
public class DeliverySlipServiceImp implements DeliverySlipService {
	@Autowired
	private com.otsi.kalamandir.repository.DeliverySlipRepository deliverysliprepo;
	@Autowired
	private DeliverySlipMapper dsmapper;

	/* method use - to create deliveryslip */
	@Override
	public DeliverySlipModel createDeliverySlip(DelivarySlipVo dsvo) {

		DeliverySlipModel dsmodel = new DeliverySlipModel();
		dsmapper.convertVoToEntity(dsvo, dsmodel);
		DeliverySlipModel savedds = deliverysliprepo.save(dsmodel);
		dsmapper.convertEntityToVo(savedds);
		return savedds;

	}

	/* method use- to update deliverySlip */
	@Override
	public DeliverySlipModel updateDeliverySlip(int spId, DelivarySlipVo dsvo) {
		DeliverySlipModel dsmodel = deliverysliprepo.getById(spId);

		dsmapper.convertVoToEntity(dsvo, dsmodel);
		dsmodel.setOpen(true);
		dsmodel.setDsOpenDate(LocalDateTime.now());
		DeliverySlipModel savedds = deliverysliprepo.save(dsmodel);
		dsmapper.convertEntityToVo(savedds);
		return savedds;

	}

	/* method use - for closing the day of POS*/
	@Override
	public String posDayClose() {

		List<DeliverySlipModel> DsList = deliverysliprepo.findByIsOpenAndCloseDate(Boolean.TRUE, LocalDate.now());

		if (DsList.isEmpty()) {
			return "successfully we can close the day of pos " + "/n" + " uncleared delivery Slips count :  ";

		} else
			return "to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size();

	}
}
