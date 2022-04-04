package com.otsi.retail.promotions.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.otsi.retail.promotions.entity.ColumnNameAndOperators;
import com.otsi.retail.promotions.repository.ColumnNameAndOperatorRepository;

@Service
@Configuration
public class ColumnNameAndOperatorServiceImpl implements ColumnNameAndOperatorService {

	private Logger log = LogManager.getLogger(ColumnNameAndOperatorServiceImpl.class);

	@Autowired
	ColumnNameAndOperatorRepository columnNameAndOperatorRepository;

	@Override
	public ColumnNameAndOperators saveColumnNameAndOperator(ColumnNameAndOperators columnNameAndOperators) {

		ColumnNameAndOperators saveColumnNameAndOperator = columnNameAndOperatorRepository.save(columnNameAndOperators);

		return saveColumnNameAndOperator;
	}

	@Override
	public List<ColumnNameAndOperators> getListofColumnNames() {

		return columnNameAndOperatorRepository.findAll();
	}

	@Override
	public ColumnNameAndOperators getColumnNameById(Long Id) {

		Optional<ColumnNameAndOperators> findById = columnNameAndOperatorRepository.findById(Id);

		if (findById.isPresent()) {
			return findById.get();

		}

		return null;
	}

	@Override
	public String deleteColumnNameId(Long Id) {

		Optional<ColumnNameAndOperators> findById = columnNameAndOperatorRepository.findById(Id);

		if (findById.isPresent()) {
			columnNameAndOperatorRepository.delete(findById.get());
		}
		return null;

	}

	@Override
	public List<ColumnNameAndOperators> getAnyMatchingData(String columnName, String operator) {
		
		if(columnName != null&& operator !=null) {
			List<ColumnNameAndOperators> findAllByColumnNameAndOperator = columnNameAndOperatorRepository.findAllByColumnNameAndOperator(columnName,operator);
			return findAllByColumnNameAndOperator;
		}else {
		List<ColumnNameAndOperators> findByColumnNameOrOperator = columnNameAndOperatorRepository.findAllByColumnNameOrOperator(columnName,operator);
		
		return findByColumnNameOrOperator;
		}
	
	}
}


	

	


	


		
	
	
	


