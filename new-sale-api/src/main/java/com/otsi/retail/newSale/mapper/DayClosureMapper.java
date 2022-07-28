/**
 * 
 */
package com.otsi.retail.newSale.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import com.otsi.retail.newSale.Entity.DayClosure;
import com.otsi.retail.newSale.vo.DayClosureVO;

/**
 * @author vasavi.yakkali
 *
 */
@Component
public class DayClosureMapper {

	public DayClosureVO entityToVO(DayClosure dayClosure) {
		DayClosureVO dayClosureVO = new DayClosureVO();
		dayClosureVO.setCreatedDate(dayClosure.getCreatedDate());
		dayClosureVO.setLastModifiedDate(dayClosure.getLastModifiedDate());
		dayClosureVO.setDayClose(dayClosure.getDayClose().toLocalDate());
		dayClosureVO.setStoreId(dayClosure.getStoreId());
		return dayClosureVO;

	}

	public List<DayClosureVO> entityToVO(List<DayClosure> dayClosureList) {
		return dayClosureList.stream().map(dayClosure -> entityToVO(dayClosure)).collect(Collectors.toList());

	}

	public DayClosure voToEntity(DayClosureVO dayClosureVO) {
		DayClosure dayClosure = new DayClosure();
		dayClosure.setDayClose(dayClosureVO.getDayClose().atStartOfDay());
		dayClosure.setCreatedBy(dayClosureVO.getCreatedBy());
		dayClosure.setCreatedDate(LocalDateTime.now());
		dayClosure.setLastModifiedDate(LocalDateTime.now());
		dayClosure.setModifiedBy(dayClosureVO.getModifiedBy());
		dayClosure.setStoreId(dayClosureVO.getStoreId());

		return dayClosure;

	}

	public List<DayClosure> voToEntity(List<DayClosureVO> dayClosureVOList) {
		return dayClosureVOList.stream().map(DayClosureVO -> voToEntity(DayClosureVO)).collect(Collectors.toList());

	}

}
