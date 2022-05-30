package com.otsi.retail.promotions.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.ColumnNameAndOperators;
import com.otsi.retail.promotions.vo.ColumnNameAndOperatorsVo;

@Component
public class ColumnNameAndOperatorMapper {

	public ColumnNameAndOperators columnNameAndOperatorVoToEntity(ColumnNameAndOperatorsVo vo) {

		ColumnNameAndOperators covo = new ColumnNameAndOperators();

		covo.setColumnName(vo.getColumnName());
		covo.setDomainId(vo.getDomainId());
		covo.setId(vo.getId());
		covo.setOperator(vo.getOperator());

		return covo;
	}

	public ColumnNameAndOperatorsVo columnNameAndOperatorEntityToVo(ColumnNameAndOperators columnNameAndOperators) {

		ColumnNameAndOperatorsVo covo = new ColumnNameAndOperatorsVo();

		covo.setColumnName(columnNameAndOperators.getColumnName());
		covo.setDomainId(columnNameAndOperators.getDomainId());
		covo.setId(columnNameAndOperators.getId());
		covo.setOperator(columnNameAndOperators.getOperator());

		return covo;
	}

	public List<ColumnNameAndOperatorsVo> columnNameAndOperatorEntityToVoList(
			List<ColumnNameAndOperators> columnNameAndOperators) {

		List<ColumnNameAndOperatorsVo> covo = new ArrayList<>();
		columnNameAndOperators.stream().forEach(x -> {
			ColumnNameAndOperatorsVo vo = new ColumnNameAndOperatorsVo();

			vo.setColumnName(x.getColumnName());
			vo.setDomainId(x.getDomainId());
			vo.setId(x.getId());
			vo.setOperator(x.getOperator());

			covo.add(vo);
		});

		return covo;
	}

}
