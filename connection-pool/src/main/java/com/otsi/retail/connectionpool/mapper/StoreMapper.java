///**
// * 
// */
//package com.otsi.retail.connectionpool.mapper;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Component;
//
//import com.otsi.retail.connectionpool.entity.StoresEntity;
//import com.otsi.retail.connectionpool.vo.StoreVo;
//
///**
// * @author Sudheer.Swamy
// *
// */
//@Component
//public class StoreMapper {
//
//	public StoresEntity converStoreVoToEntity(StoreVo vo) {
//
//		StoresEntity dto = new StoresEntity();
//		dto.setStoreId(vo.getStoreId());
//		dto.setStoreName(vo.getStoreName());
//		dto.setStoreDescription(vo.getStoreDescription());
//
//		return dto;
//	}
//
//	public List<StoresEntity> VoToEntity(List<StoreVo> vos) {
//		return vos.stream().map(vo -> converStoreVoToEntity(vo)).collect(Collectors.toList());
//
//	}
//
//	public StoreVo entityToVo(StoresEntity dto) {
//
//		StoreVo vo = new StoreVo();
//		vo.setStoreId(dto.getStoreId());
//		vo.setStoreName(dto.getStoreName());
//		vo.setStoreDescription(vo.getStoreDescription());
//
//		return vo;
//	}
//
//	public List<StoreVo> EntityToVo(List<StoresEntity> dtos) {
//		return dtos.stream().map(dto -> entityToVo(dto)).collect(Collectors.toList());
//
//	}
//
//}
