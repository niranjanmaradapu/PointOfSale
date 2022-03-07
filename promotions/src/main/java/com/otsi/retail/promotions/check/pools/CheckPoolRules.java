package com.otsi.retail.promotions.check.pools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.ColumnName;
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
			return checkLessThanOrEqualsFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:
			return checkLessThanOrEqualDateDataType(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanOrEqualLongDataType(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkLessThanOrEqualsCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:

			return checkLessThanOrEqualsSectionDataType(condition, productTextileVo.getSection());

		case SubSection:
			return checkLessThanOrEqualsSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkLessThanOrEqualsDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:

			return checkLessThanOrEqualsUomDataType(condition, productTextileVo.getUom());

		case Dcode:
			return checkLessThanOrEqualsDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:
			return checkLessThanOrEqualsSCodeDataType(condition, productTextileVo.getHsnCode());
			
		case SubSectionId:
			return checkLessThanOrEqualsSSIdDataType(condition, productTextileVo.getHsnCode());
			
		case DiscountType:
			return checkLessThanOrEqualsDisDataType(condition,productTextileVo.getHsnCode());
			

		default:
			return false;

		}

	}

	private boolean checkLessThanOrEqualsDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		
		return false;
	}

	private boolean checkLessThanOrEqualsSSIdDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsSCodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsDcodeDataType(Condition condition, String hsnCode) {
		if (Float.parseFloat(hsnCode) <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkLessThanOrEqualsUomDataType(Condition condition, String uom) {
		if (Float.parseFloat(uom) <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsSubSectionDataType(Condition condition, Long subSection) {

		if (Float.valueOf(subSection).floatValue() <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsSectionDataType(Condition condition, Long section) {
		if (Float.valueOf(section).floatValue() <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThanOrEqualsCostDataValues(Condition condition, float costPrice) {

		if (costPrice <= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
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
			return checkGreaterThanOrEqualsFloatDataTypeValues(condition, productTextileVo.getItemMrp());

		case BarcodeCreatedOn:
			return checkGreaterThanOrEqualDateDataType(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanOrEqualLongDataType(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkGreaterThanOrEqulasCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:
			return checkGreaterThanOrEqualsSectionDataType(condition, productTextileVo.getSection());

		case SubSection:
			return checkGreaterThanOrEqualsSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkGreaterThanOrEqualsDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:
			return checkGreaterThanOrEqualsUomDataType(condition, productTextileVo.getUom());

		case Dcode:
			return checkGreaterThanOrEqualsDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:
			return checkGreaterThanOrEqualsSCodeDataType(condition, productTextileVo.getHsnCode());

		case SubSectionId:
			return checkGreaterThanOrEqualsSSIdDataType(condition, productTextileVo.getHsnCode());
			
		case DiscountType:
			return checkGreaterThanOrEqualsDisDataType(condition,productTextileVo.getHsnCode());
			

		default:
			return false;
		}

	}

	private boolean checkGreaterThanOrEqualsDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqualsSSIdDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkGreaterThanOrEqualsSCodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkGreaterThanOrEqualsDcodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqualsUomDataType(Condition condition, String uom) {
		if (Float.parseFloat(uom) >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqualsDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqualsSubSectionDataType(Condition condition, Long subSection) {
		if (Float.valueOf(subSection).floatValue() >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanOrEqualsSectionDataType(Condition condition, Long section) {
		if (Float.valueOf(section).floatValue() >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkGreaterThanOrEqulasCostDataValues(Condition condition, float costPrice) {
		if (costPrice >= Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
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

			return checkLessThanDateDataType(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkLessThanLongDataType(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkLessThanCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:
			return checkLessThanSectionDataType(condition, productTextileVo.getSection());
			
		case SubSection:
			return checkLessThanSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkLessThanDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:
			return checkLessThanUomDataType(condition, productTextileVo.getUom());

		case Dcode:
			return checkLessThanDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:

			return checkLessThanSCodeDataType(condition, productTextileVo.getHsnCode());

		case SubSectionId:
			return checkLessThanSSIdDataType(condition, productTextileVo.getHsnCode());
			
		case DiscountType:
			
			return checkLessThanDisDataType(condition,productTextileVo.getHsnCode());

		default:
			return false;

		}

	}

	private boolean checkLessThanSSIdDataType(Condition condition, String hsnCode) {
        if (Float.parseFloat(hsnCode) < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			
			return true;
		
		return false;
	}

	private boolean checkLessThanDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			
			return true;
		
		return false;
		
	}

	private boolean checkLessThanSCodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkLessThanDcodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkLessThanUomDataType(Condition condition, String uom) {

		if (Float.parseFloat(uom) < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkLessThanDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThanSubSectionDataType(Condition condition, Long subSection) {
		if (Float.valueOf(subSection).floatValue() < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThanSectionDataType(Condition condition, Long section) {
		if (Float.valueOf(section).floatValue() < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkLessThanCostDataValues(Condition condition, float costPrice) {

		if (costPrice < Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkLessThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt.isBefore(LocalDate.parse(condition.getGivenValues().get(0))))
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

			return checkGreaterThanDateDataType(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkGreaterThanLongDataType(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkGreaterThanCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:

			return checkGreaterThanSectionDataType(condition, productTextileVo.getSection());

		case SubSection:
			return checkGreaterThanSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkGreaterThanDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:
			return checkGreaterThanUomDataType(condition, productTextileVo.getUom());

		case Dcode:
			return checkGreaterThanDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:
			return checkGreaterThanSCodeDataType(condition, productTextileVo.getHsnCode());
			
		case SubSectionId:
			return checkGreaterThanSSIdDataType(condition,productTextileVo.getHsnCode());
			
		case DiscountType:
			return checkGreaterThanDisDataType(condition,productTextileVo.getHsnCode());
			

		default:
			return false;

		}

	}

	private boolean checkGreaterThanDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanSSIdDataType(Condition condition, String hsnCode) {
		if (Float.parseFloat(hsnCode) > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanSCodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanDcodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanUomDataType(Condition condition, String uom) {

		if (Float.parseFloat(uom) > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanSubSectionDataType(Condition condition, Long subSection) {
		if (Float.valueOf(subSection).floatValue() > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanSectionDataType(Condition condition, Long section) {
		if (Float.valueOf(section).floatValue() > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkGreaterThanCostDataValues(Condition condition, float costPrice) {
		if (costPrice > Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkGreaterThanLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() > Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkGreaterThanDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (originalBarcodeCreatedAt.isAfter(LocalDate.parse(condition.getGivenValues().get(0))))
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

			return checkNotEqualsDateDataType(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:
			return checkNotEqualsLongDataType(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkNotEqualsCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:
			return checkNotEqualsSectionDataType(condition, productTextileVo.getSection());

		case SubSection:
			return checkNotEqualsSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkNotEqualsDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:
			return checkNotEqualsUomDataType(condition, productTextileVo.getUom());
		case Dcode:
			return checkNotEqualsDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:
			return checkNotEqualsSCodeDataType(condition, productTextileVo.getHsnCode());
			
		case SubSectionId:
			
			return checkNotEqualsSSIdDataType(condition,productTextileVo.getHsnCode());
			
		case DiscountType:
			return checkNotEqualsDisDataType(condition,productTextileVo.getHsnCode());
			
			
	
		default:
			return false;
		}

	}

	private boolean checkNotEqualsDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		
		return false;
	}

	private boolean checkNotEqualsSSIdDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		
		return false;
	}

	private boolean checkNotEqualsSCodeDataType(Condition condition, String hsnCode) {
		if (Float.parseFloat(hsnCode) != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkNotEqualsDcodeDataType(Condition condition, String hsnCode) {
		if (Float.parseFloat(hsnCode) != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkNotEqualsUomDataType(Condition condition, String uom) {

		if (Float.parseFloat(uom) != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkNotEqualsDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() != Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkNotEqualsSubSectionDataType(Condition condition, Long subSection) {
		if (Float.valueOf(subSection).floatValue() != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkNotEqualsSectionDataType(Condition condition, Long section) {

		if (Float.valueOf(section).floatValue() != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkNotEqualsCostDataValues(Condition condition, float costPrice) {
		if (costPrice != Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkNotEqualsLongDataType(Condition condition, String batchNo) {

		if (Long.valueOf(batchNo).longValue() != Long.parseLong(condition.getGivenValues().get(0)))
			return true;

		return false;
	}

	private boolean checkNotEqualsDateDataType(Condition condition, LocalDate originalBarcodeCreatedAt) {

		if (!originalBarcodeCreatedAt.isEqual(LocalDate.parse(condition.getGivenValues().get(0))))
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

			return checkEqualsOnDateDataTypeValues(condition, productTextileVo.getOriginalBarcodeCreatedAt());

		case BatchNo:

			return checkEqualsOnBatchNumberData(condition, productTextileVo.getBatchNo());

		case CostPrice:

			return checkEqualsCostDataValues(condition, productTextileVo.getCostPrice());

		case Section:

			return checkEqualsSectionDataType(condition, productTextileVo.getSection());

		case SubSection:
			return checkEqualsSubSectionDataType(condition, productTextileVo.getSubSection());

		case Division:
			return checkEqualsDivisionDataType(condition, productTextileVo.getDivision());

		case Uom:
			return checkEqualsUomDatatype(condition, productTextileVo.getUom());

		case Dcode:
			return checkEqualsDcodeDataType(condition, productTextileVo.getHsnCode());

		case StyleCode:

			return checkEqualsSCodeDataType(condition, productTextileVo.getHsnCode());
			
		case SubSectionId:
			return checkEqualsSSIdDataType(condition,productTextileVo.getHsnCode());
			
		case DiscountType:
			return checkEqualsDisDataType(condition,productTextileVo.getHsnCode());

		default:
			return false;
		}

	}

	private boolean checkEqualsDisDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		
		return false;
	}

	private boolean checkEqualsSSIdDataType(Condition condition, String hsnCode) {
		
		if (Float.parseFloat(hsnCode) == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		
		return false;
	}

	private boolean checkEqualsSCodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkEqualsDcodeDataType(Condition condition, String hsnCode) {

		if (Float.parseFloat(hsnCode) == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkEqualsUomDatatype(Condition condition, String uom) {
		if (Float.parseFloat(uom) == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;
		return false;
	}

	private boolean checkEqualsDivisionDataType(Condition condition, Long division) {

		if (Float.valueOf(division).floatValue() == Float.valueOf(condition.getGivenValues().get(0)).floatValue())

			return true;

		return false;
	}

	private boolean checkEqualsSubSectionDataType(Condition condition, Long subSection) {

		if (Float.valueOf(subSection).floatValue() == Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;

		return false;
	}

	private boolean checkEqualsSectionDataType(Condition condition, Long section) {
		if (Float.valueOf(section).floatValue() == Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
	}

	private boolean checkEqualsCostDataValues(Condition condition, float costPrice) {
		if (costPrice == Float.valueOf(condition.getGivenValues().get(0)).floatValue())
			return true;
		return false;
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