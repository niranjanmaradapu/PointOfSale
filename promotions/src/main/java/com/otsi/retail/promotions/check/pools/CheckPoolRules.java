package com.otsi.retail.promotions.check.pools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.Operator;
import com.otsi.retail.promotions.entity.Condition;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
import com.otsi.retail.promotions.vo.BarcodeTextileVo;

@Component
public class CheckPoolRules {

	final private String dateFormat = "dd/mm/yyyy";

	private boolean checkCondition(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		Operator operatorSymbol = condition.getOperatorSymbol();

		switch (operatorSymbol) {

		case Equals:
			return checkEquals(condition, barcodeTextileVo);

		case NotEquals:
			return checkNotEquals(condition, barcodeTextileVo);

		case GreaterThan:
			return checkGreaterThan(condition, barcodeTextileVo);

		case LessThan:
			return checkLessThan(condition, barcodeTextileVo);

		case GreaterThanOrEquals:
			return checkGreaterThanAndEquals(condition, barcodeTextileVo);

		case LessThanOrEquals:
			return checkLessThanAndEquals(condition, barcodeTextileVo);

		default:
			return false;

		}

	}

	private boolean checkLessThanAndEquals(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkLessThanAndEqualsFloatDataTypeValues(condition,
					barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:
			return checkLessThanAndEqualDateDataType(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanAndEqualLongDataType(condition, barcodeTextileVo.getBatchNo());
		default:
			return false;

		}

	}

	private boolean checkLessThanAndEqualLongDataType(Condition condition, String batchNo) {
		if (Long.valueOf(batchNo).longValue() <= Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;
		return false;
	}

	private boolean checkLessThanAndEqualDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {
		LocalDate parse = LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat));
		if (originalBarcodeCreatedAt.isBefore(parse) || originalBarcodeCreatedAt.isEqual(parse))
			return true;
		return false;
	}

	private boolean checkLessThanAndEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() <= purchasedValue)
			return true;

		return false;
	}

	private boolean checkGreaterThanAndEquals(Condition condition, BarcodeTextileVo barcodeTextileVo) {
		switch (condition.getColumnName()) {

		case Mrp:
			return checkGreaterThanAndEqualsFloatDataTypeValues(condition,
					barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:
			return checkGreaterThanAndEqualDateDataType(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanAndEqualLongDataType(condition, barcodeTextileVo.getBatchNo());
		default:
			return false;
		}

	}

	private boolean checkGreaterThanAndEqualDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {
		LocalDate parse = LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat));
		if (originalBarcodeCreatedAt.isAfter(parse) || originalBarcodeCreatedAt.isEqual(parse))
			return true;
		return false;
	}

	private boolean checkGreaterThanAndEqualLongDataType(Condition condition, String batchNo) {
		if (Long.valueOf(batchNo).longValue() >= Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;
		return false;
	}

	private boolean checkGreaterThanAndEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() >= purchasedValue)
			return true;

		return false;
	}

	private boolean checkLessThan(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkLessThanFloatDataTypeValues(condition, barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:

			return checkLessThanDateDataType(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanLongDataType(condition, barcodeTextileVo.getBatchNo());

		default:
			return false;

		}

	}

	private boolean checkLessThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt
				.isBefore(LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat))))
			return true;

		return false;
	}

	private boolean checkLessThanLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() < Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;
		return false;
	}

	private boolean checkLessThanFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() < purchasedValue)
			return true;

		return false;
	}

	private boolean checkGreaterThan(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkGreaterThanFloatDataTypeValues(condition, barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:

			return checkGreaterThanDateDataType(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanLongDataType(condition, barcodeTextileVo.getBatchNo());

		default:
			return false;

		}

	}

	private boolean checkGreaterThanLongDataType(Condition condition, String batchNo) {
		if (Long.valueOf(batchNo).longValue() > Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt
				.isAfter(LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat))))
			return true;

		return false;
	}

	private boolean checkGreaterThanFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() > purchasedValue)
			return true;
		return false;
	}

	private boolean checkNotEquals(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkNotEqualsFloatDataTypeValues(condition, barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:

			return checkNotEqualsDateDataType(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkNotEqualsLongDataType(condition, barcodeTextileVo.getBatchNo());

		default:
			return false;
		}

	}

	private boolean checkNotEqualsLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() != Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;

		return false;
	}

	private boolean checkNotEqualsDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (!originalBarcodeCreatedAt
				.isEqual(LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat))))
			return true;

		return false;
	}

	private boolean checkNotEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {
		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() != purchasedValue)
			return true;

		return false;
	}

	private boolean checkEquals(Condition condition, BarcodeTextileVo barcodeTextileVo) {

		switch (condition.getColumnName()) {

		case Mrp:
			return checkEqualsFloatDataTypeValues(condition, barcodeTextileVo.getProductTextile().getItemMrp());

		case BarcodeCreatedDate:

			return checkEqualsOnDateDataTypeValues(condition,
					barcodeTextileVo.getProductTextile().getOriginalBarcodeCreatedAt());

		case BatchNo:

			return checkEqualsOnBatchNumberData(condition, barcodeTextileVo.getBatchNo());

		default:
			return false;
		}

	}

	private boolean checkEqualsOnDateDataTypeValues(Condition condition, LocalDate originalBarcodeCreatedAt) {
		if (originalBarcodeCreatedAt
				.isEqual(LocalDate.parse(condition.getGivenValues().get(0), DateTimeFormatter.ofPattern(dateFormat))))
			return true;

		return false;
	}

	private boolean checkEqualsOnBatchNumberData(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() == Long.valueOf(condition.getGivenValues().get(0)).longValue())
			return true;

		return false;
	}

	private boolean checkEqualsFloatDataTypeValues(Condition condition, float purchasedValue) {

		if (Float.valueOf(condition.getGivenValues().get(0)).floatValue() == purchasedValue)
			return true;

		return false;
	}

	private boolean checkRule(List<Pool_Rule> poolRules, BarcodeTextileVo barcodeTextileVo) {

		for (Pool_Rule rule : poolRules) {

			List<Condition> conditions = rule.getConditions();
			boolean areAllConditionsPassed = true;

			for (Condition condition : conditions) {

				if (!checkCondition(condition, barcodeTextileVo)) {
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

	public boolean checkPools(List<PoolEntity> poolEntities, BarcodeTextileVo barcodeTextileVo) {

		for (PoolEntity poolEntity : poolEntities) {
			return checkRule(poolEntity.getPool_Rule(), barcodeTextileVo);
		}
		return false;

	}

}
