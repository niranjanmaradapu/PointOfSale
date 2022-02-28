package com.otsi.retail.promotions.check.pools;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.Operator;
import com.otsi.retail.promotions.entity.Condition;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
import com.otsi.retail.promotions.vo.ProductTextileVo;

@Component
public class CheckPoolRules {

	private boolean checkCondition(Condition condition, ProductTextileVo productTextileVo) {

		Operator operatorSymbol = condition.getOperatorSymbol();

		switch (operatorSymbol) {

		case Equals:
			return checkEquals(condition, productTextileVo);

		case NotEquals:
			return checkNotEquals(condition, productTextileVo);

		case GreaterThan:
			return checkGreaterThan(condition, productTextileVo);

		case LessThan:
			return checkLessThan(condition, productTextileVo);

		case GreaterThanOrEquals:
			return checkGreaterThanOrEquals(condition, productTextileVo);

		case LessThanOrEquals:
			return checkLessThanAndEquals(condition, productTextileVo);

		default:
			return false;

		}

	}

	private boolean checkLessThanAndEquals(Condition condition, ProductTextileVo productTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkLessThanOrEqualsFloatDataTypeValues(condition,
					productTextileVo.getItemMrp());

		case BarcodeCreatedOn:
			return checkLessThanOrEqualDateDataType(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanOrEqualLongDataType(condition, productTextileVo.getBatchNo());
		default:
			return false;

		}

	}

	private boolean checkLessThanOrEqualLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() <= Long.parseLong(condition.getGivenValues().get(0)))
			return true;
		return false;
	}

	private boolean checkLessThanOrEqualDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {
		LocalDate givenDate = LocalDate.parse(condition.getGivenValues().get(0));

		if (originalBarcodeCreatedAt.isBefore(givenDate) || originalBarcodeCreatedAt.isEqual(givenDate))
			return true;
		return false;
	}

	private boolean checkLessThanOrEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (purchasedValue <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanOrEquals(Condition condition, ProductTextileVo productTextileVo) {
		switch (condition.getColumnName()) {

		case Mrp:
			return checkGreaterThanOrEqualsFloatDataTypeValues(condition,
					productTextileVo.getItemMrp());

		case BarcodeCreatedOn:
			return checkGreaterThanOrEqualDateDataType(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanOrEqualLongDataType(condition, productTextileVo.getBatchNo());
		default:
			return false;
		}

	}

	private boolean checkGreaterThanOrEqualDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {
		LocalDate givenDate = LocalDate.parse(condition.getGivenValues().get(0));
		if (originalBarcodeCreatedAt.isAfter(givenDate) || originalBarcodeCreatedAt.isEqual(givenDate))
			return true;
		return false;
	}

	 
	
	private boolean checkGreaterThanOrEqualLongDataType(Condition condition, String batchNo)
			throws NumberFormatException {

		if (Long.valueOf(batchNo).longValue() >= Long.parseLong(condition.getGivenValues().get(0)))
			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (purchasedValue >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThan(Condition condition, ProductTextileVo productTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkLessThanFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:

			return checkLessThanDateDataType(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanLongDataType(condition, productTextileVo.getBatchNo());

		default:
			return false;

		}

	}

	private boolean checkLessThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt
				.isBefore(LocalDate.parse(condition.getGivenValues().get(0))))
			return true;

		return false;
	}

	private boolean checkLessThanLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() < Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkLessThanFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (purchasedValue < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThan(Condition condition, ProductTextileVo productTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkGreaterThanFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:

			return checkGreaterThanDateDataType(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanLongDataType(condition, productTextileVo.getBatchNo());

		default:
			return false;

		}

	}

	private boolean checkGreaterThanLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() > Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkGreaterThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt
				.isAfter(LocalDate.parse(condition.getGivenValues().get(0))))
			return true;

		return false;
	}

	private boolean checkGreaterThanFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (purchasedValue > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkNotEquals(Condition condition, ProductTextileVo productTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkNotEqualsFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:

			return checkNotEqualsDateDataType(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkNotEqualsLongDataType(condition, productTextileVo.getBatchNo());

		default:
			return false;
		}

	}

	private boolean checkNotEqualsLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() != Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkNotEqualsDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (!originalBarcodeCreatedAt
				.isEqual(LocalDate.parse(condition.getGivenValues().get(0))))
			return true;

		return false;
	}

	private boolean checkNotEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (purchasedValue != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkEquals(Condition condition, ProductTextileVo productTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkEqualsFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:

			return checkEqualsOnDateDataTypeValues(condition,
					productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:

			return checkEqualsOnBatchNumberData(condition, productTextileVo.getBatchNo());

		default:
			return false;
		}

	}

	private boolean checkEqualsOnDateDataTypeValues(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt.isEqual(LocalDate.parse(condition.getGivenValues().get(0))))
			return true;

		return false;
	}

	private boolean checkEqualsOnBatchNumberData(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() == Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {

		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() == purchasedValue)
			return true;

		return false;
	}

	private boolean checkRule(List<Pool_Rule> poolRules, ProductTextileVo productTextileVo) {

		for (Pool_Rule rule : poolRules) {

			List<Condition> conditions = rule.getConditions();
			boolean areAllConditionsPassed = true;

			for (Condition condition : conditions) {

				if (!checkCondition(condition, productTextileVo)) {
					areAllConditionsPassed = false;
					break;
				}

			}
			if (areAllConditionsPassed && rule.getRuleType().equalsIgnoreCase("Include"))
				return true;
			else if (areAllConditionsPassed && rule.getRuleType().equalsIgnoreCase("Exclude"))
				return false;

		}

		return false;
	}

	public boolean checkPools(List<PoolEntity> poolEntities, ProductTextileVo productTextileVo) {

		for (PoolEntity poolEntity : poolEntities) {
			return checkRule(poolEntity.getPool_Rule(), productTextileVo);
		}
		return false;

	}


}
