package com.otsi.kalamandir.service;


import org.springframework.stereotype.Service;

import com.otsi.kalamandir.model.DeliverySlipModel;
import com.otsi.kalamandir.vo.DelivarySlipVo;
@Service
public interface DeliverySlipService {

	DeliverySlipModel createDeliverySlip(DelivarySlipVo dsvo);

	DeliverySlipModel updateDeliverySlip(int spId,DelivarySlipVo dsvo);

	String posDayClose();

}
