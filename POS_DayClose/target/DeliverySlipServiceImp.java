package com.otsi.kalamandir.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.kalamandir.mapper.DeliverySlipMapper;
import com.otsi.kalamandir.model.DeliverySlipModel;
import com.otsi.kalamandir.repository.DeliverySlipRepository;
import com.otsi.kalamandir.service.DeliverySlipService;
import com.otsi.kalamandir.vo.DelivarySlipVo;
@Component
public class DeliverySlipServiceImp  implements DeliverySlipService{
	@Autowired
	private DeliverySlipRepository deliverysliprepo;
	@Autowired
	private DeliverySlipMapper dsmapper;

	@Override
	public DelivarySlipVo createDeliverySlip(DelivarySlipVo dsvo) {
		
		DeliverySlipModel dsmodel=new DeliverySlipModel();
		dsmapper.convertVoToEntity(dsvo, dsmodel);
		DeliverySlipModel savedds = deliverysliprepo.save(dsmodel);
		return dsmapper.convertEntityToVo(savedds);
		
	
		
	}

}
