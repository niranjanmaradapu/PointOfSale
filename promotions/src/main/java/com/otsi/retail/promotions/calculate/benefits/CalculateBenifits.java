package com.otsi.retail.promotions.calculate.benefits;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.otsi.retail.promotions.check.pools.CheckPoolRules;
import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountSubTypes;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.CalculatedDiscountsVo;
import com.otsi.retail.promotions.vo.LineItemVo;
import com.otsi.retail.promotions.vo.ProductTextileVo;

@Component
public class CalculateBenifits {

	@Autowired
	private CheckPoolRules checkPoolRules;

	public CalculatedDiscountsVo calculate(List<BenefitVo> benifitVos, ProductTextileVo productTextileVo) {
		
		

		// Loop through benifitVos
		CalculatedDiscountsVo calculatedDiscountsVo = new CalculatedDiscountsVo();
		calculatedDiscountsVo.setDiscountAvailable(true);
		for (BenefitVo benifitVo : benifitVos) {
			// if benifit type is flat discount call calculateBenifit for flat discount

			if (benifitVo.getBenfitType().equals(BenfitType.FlatDiscount)) {
				calculatedDiscountsVo
						.setCalculatedDiscount(calculateBeniftForFlatDIscount(benifitVo, productTextileVo));
			} else if (benifitVo.getBenfitType().equals(BenfitType.XunitsFromBuyPool)) {
				calculatedDiscountsVo.setCalculatedDiscountDetails(
						calculateBenifitsForXUnitsFromBuyPool(benifitVo, productTextileVo));

			} else if (benifitVo.getBenfitType().equals(BenfitType.XunitsFromGetPool)) {
				calculatedDiscountsVo.setCalculatedDiscountDetails(
						calculateBenifitsForXUnitsFromGetPool(benifitVo, productTextileVo));

			}
		}
		return calculatedDiscountsVo;
	}

	private String calculateBeniftForFlatDIscount(BenefitVo benifitVo, ProductTextileVo productTextileVo) {

		String calculatedDiscountAmount = "";

		if (benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			calculatedDiscountAmount = calculateBeniftForPercentageDiscountOn(benifitVo, productTextileVo);
		} else if (benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			calculatedDiscountAmount = calculateBeniftForRupeesDiscountOn(benifitVo, productTextileVo);
		} else if (benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			// calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			calculatedDiscountAmount = benifitVo.getDiscount().toString();
		}
		return calculatedDiscountAmount;
	}

	private String calculateBeniftForPercentageDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {

		String calculatedDiscountAmount = "";
//		if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP)){
//	          int discountPercentage= Integer.valueOf(benifitVo.getDiscount()).intValue() ;
//	          int purchasedQuantity= productTextileVo.getQty();
//	          
//	        /*comments added by sudheer */
//	          
//	        // float valueOfRSP= barcodeTextileVo.getProductTextile().getItemRsp();
//	        // calculatedDiscountAmount= (discountPercentage/100)*(purchasedQuantity*valueOfRSP) +"";
//	         
//		}
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)) {
			int discountPercentage = Integer.valueOf(benifitVo.getDiscount()).intValue();
			int purchasedQuantity = productTextileVo.getQty();
			float valueOfMRP = productTextileVo.getItemMrp();
			// calculatedDiscountAmount=
			// (discountPercentage/100)*(purchasedQuantity*valueOfMRP) +"";
			calculatedDiscountAmount = (valueOfMRP / 100) * purchasedQuantity * discountPercentage + "";

		}
		return calculatedDiscountAmount;

	}

	private String calculateBeniftForRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		String calculatedDiscountAmount = "";
		if (/* benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP) || */ benifitVo.getDiscountSubType()
				.equals(DiscountSubTypes.ItemMRP)) {
			int discountAmount = Integer.valueOf(benifitVo.getDiscount()).intValue();
			int purchasedQuantity = productTextileVo.getQty();
			calculatedDiscountAmount = discountAmount * purchasedQuantity + "";

		}

		return calculatedDiscountAmount;
	}

	/*
	 * private double calculateBeniftForFixedAmountOn(BenifitVo benifitVo,
	 * BarcodeTextileVo barcodeTextileVo) { double calculatedDiscountAmount=0.0;
	 * if(benifitVo.getEachItem().equalsIgnoreCase("eachItem") ||
	 * benifitVo.getEachItem().equalsIgnoreCase("itemRSP") ||
	 * benifitVo.getEachItem().equalsIgnoreCase("allItems") ){ int discountAmount=
	 * benifitVo.getDiscountOn(); int purchasedQuantity=
	 * barcodeTextileVo.getQuantity(); int valueOfRSP= barcodeTextileVo.getRSP();
	 * calculatedDiscountAmount= (discountAmount)-(purchasedQuantity*valueOfRSP);
	 * 
	 * } else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){ int
	 * discountAmount= benifitVo.getDiscountOn(); int purchasedQuantity=
	 * barcodeTextileVo.getQuantity(); int valueOfMRP= barcodeTextileVo.getMRP();
	 * calculatedDiscountAmount= (discountAmount)-(purchasedQuantity*valueOfMRP);
	 * 
	 * }
	 * 
	 * return calculatedDiscountAmount; }
	 */

	private String calculateBenifitsForXUnitsFromBuyPool(BenefitVo benifitVo, ProductTextileVo productTextileVo) {

		String discountDetails = new String();

		if (benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			// calculatedDiscountAmount=calculateBeniftForPercentageDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails = calculateBenifitsForXUBAndPercentageDiscountOn(benifitVo, productTextileVo);
		} else if (benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			// calculatedDiscountAmount=calculateBeniftForRupeesDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails = calculateBenifitsForXUBAndRupeesDiscountOn(benifitVo, productTextileVo);

		} else if (benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			// calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			// calculatedDiscountAmount=benifitVo.getDiscountOn();
			discountDetails = calculateBenifitsForXUBAndFixedAmountOn(benifitVo, productTextileVo);

		}
		return discountDetails.toString();

	}

	private String calculateBenifitsForXUBAndPercentageDiscountOn(BenefitVo benifitVo,
			ProductTextileVo productTextileVo) {

		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription.append(
				"Discount will be given on probucts of the related Buy pools. Quantity should be greater than or equal to ");
		discountDiscription.append(benifitVo.getNumOfItemsFromBuyPool() + ". ");
		discountDiscription.append("The percentage of discount is " + benifitVo.getDiscount() + " ");

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinValuedOnEachItemDiscountDiscription(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaxValuedOnEachItemDiscountDiscription(benifitVo));

		}
		return discountDiscription.toString();

	}

	private String calculateBenifitsForXUBAndRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {

		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription.append("Discount will be given on probucts of the related Buy pools.");
		discountDiscription.append("The Amount of discount is " + benifitVo.getDiscount() + " ");

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinimumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaximumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));

		}
		return discountDiscription.toString();

	}

	private String calculateBenifitsForXUBAndFixedAmountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {

		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription.append("Discount will be given on probucts of the related Buy pools.");
		// discountDiscription.append("The Fixed-Amount of discount is
		// "+benifitVo.getDiscountOn() +" ");

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMinValued(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMaxValued(benifitVo));

		}
		return discountDiscription.toString();
	}

	private String calculateBenifitsForXUnitsFromGetPool(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		String discountDetails = new String();

		if (benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			// calculatedDiscountAmount=calculateBeniftForPercentageDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails = calculateBenifitsForXUGAndPercentageDiscountOn(benifitVo, productTextileVo);
		} else if (benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			// calculatedDiscountAmount=calculateBeniftForRupeesDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails = calculateBenifitsForXUGAndRupeesDiscountOn(benifitVo, productTextileVo);

		} else if (benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			// calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			// calculatedDiscountAmount=benifitVo.getDiscountOn();
			discountDetails = calculateBenifitsForXUGAndFixedAmountOn(benifitVo, productTextileVo);

		}
		return discountDetails.toString();
	}

	private String calculateBenifitsForXUGAndPercentageDiscountOn(BenefitVo benifitVo,
			ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription.append(
				"Discount will be given on probucts of the related Get pools. Quantity should be greater than or equal to ");
		discountDiscription.append(benifitVo.getNumOfItemsFromGetPool() + ". ");
		discountDiscription.append("The percentage of discount is " + benifitVo.getDiscount() + " ");

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinValuedOnEachItemDiscountDiscription(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaxValuedOnEachItemDiscountDiscription(benifitVo));

		}
		return discountDiscription.toString();

	}

	private String calculateBenifitsForXUGAndRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription.append("Discount will be given on probucts of the related Get pools.");
		discountDiscription.append("The Amount of discount is " + benifitVo.getDiscount() + ".");
		discountDiscription.append("Discount Amount is on the following Get Pool"/* +benifitVo.getPoolName()+" " */);

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinimumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaximumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));

		}
		return discountDiscription.toString();
	}

	private String calculateBenifitsForXUGAndFixedAmountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription = new StringBuilder();

		discountDiscription.append("Discount will be given on probucts of the related Get pools.");
		discountDiscription
				.append("and Discount will be given on the following probucts."/* +benifitVo.getPoolName() */);

		// discountDiscription.append("The Fixed-Amount of discount is
		// "+benifitVo.getDiscountOn() +" ");

		if (benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMinValued(benifitVo));
		} else if (benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMaxValued(benifitVo));

		}
		return discountDiscription.toString();

	}

	/*
	 * private String getEachItemDiscountDiscription(BenifitVo benifitVo) {
	 * 
	 * StringBuilder eachItemDiscountDiscription =new StringBuilder();
	 * 
	 * if( benifitVo.getEachItem().equalsIgnoreCase("itemRSP") ){
	 * eachItemDiscountDiscription.append("on the RSP of each Quantity.");
	 * 
	 * } else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){
	 * eachItemDiscountDiscription.append("on the MRP of each Quantity.");
	 * 
	 * 
	 * } return eachItemDiscountDiscription.toString(); }
	 */

	private String getMaxValuedOnEachItemDiscountDiscription(BenefitVo benifitVo) {

		StringBuilder eachItemDiscountDiscription = new StringBuilder();

//		if( benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the Maximum RSP of each Quantity.");
//	         
//		}
//		else
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)) {
			eachItemDiscountDiscription.append("on the Maximum MRP of each Quantity.");

		}
		return eachItemDiscountDiscription.toString();
	}

	private String getMinValuedOnEachItemDiscountDiscription(BenefitVo benifitVo) {

		StringBuilder eachItemDiscountDiscription = new StringBuilder();

//		if( benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the Minimum RSP of each Quantity.");
//	         
//		}
//		else 

		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)) {
			eachItemDiscountDiscription.append("on the Minimum MRP of each Quantity.");

		}
		return eachItemDiscountDiscription.toString();
	}

	/*
	 * private String getEachItemDiscountDiscriptionInRupees(BenifitVo benifitVo) {
	 * 
	 * StringBuilder eachItemDiscountDiscription =new StringBuilder();
	 * 
	 * if( benifitVo.getEachItem().equalsIgnoreCase("itemRSP") ){
	 * eachItemDiscountDiscription.append("on the Minimum RSP of each Quantity.");
	 * 
	 * } else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){
	 * eachItemDiscountDiscription.append("on the Minimum MRP of each Quantity.");
	 * 
	 * 
	 * } return eachItemDiscountDiscription.toString(); }
	 */

	private String getMaximumValuedOnEachItemDiscountDiscriptionInRupees(BenefitVo benifitVo) {

		StringBuilder eachItemDiscountDiscription = new StringBuilder();

//			if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP)  ){
//				eachItemDiscountDiscription.append("on the Maximum RSP of each Quantity.");
//		         
//			}
//			else
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)) {
			eachItemDiscountDiscription.append("on the Maximum MRP of each Quantity.");
		}
		eachItemDiscountDiscription.append(" The maximum number of products that should be bought is "
				+ benifitVo.getNumOfItemsFromBuyPool() + ".");

		return eachItemDiscountDiscription.toString();
	}

	private String getMinimumValuedOnEachItemDiscountDiscriptionInRupees(BenefitVo benifitVo) {

		StringBuilder eachItemDiscountDiscription = new StringBuilder();

//		if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the RSP of each Quantity.");
//	         
//		}
//		else
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)) {
			eachItemDiscountDiscription.append("on the MRP of each Quantity.");

		}
		eachItemDiscountDiscription.append(" The minimum number of products that should be bought is "
				+ benifitVo.getNumOfItemsFromBuyPool() + ".");

		return eachItemDiscountDiscription.toString();
	}

	private Object getDiscountDiscriptionForFixedAmountOnAndMaxValued(BenefitVo benifitVo) {

		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription
				.append("The customer should buy a maximum of " + benifitVo.getNumOfItemsFromGetPool() + " Units.//n");
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.EachItem)) {
			discountDiscription.append(
					"If customer buys more than the specified Maximum number of quantity, every item will get the discount. ");
			discountDiscription.append("The discount will be given on each item and the discount amount per item is "
					+ benifitVo.getDiscount());

		} else if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.AllItems)) {
			discountDiscription.append("The discount amount will be " + benifitVo.getDiscount() + ".");

		}
		return discountDiscription.toString();
	}

	private String getDiscountDiscriptionForFixedAmountOnAndMinValued(BenefitVo benifitVo) {

		StringBuilder discountDiscription = new StringBuilder();
		discountDiscription
				.append("The customer should buy a minimum of " + benifitVo.getNumOfItemsFromGetPool() + " Units.//n");
		if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.EachItem)) {
			discountDiscription.append(
					"If customer buys more than the specified Minimum number of quantity, every item will get the discount. ");
			discountDiscription.append("The discount will be given on each item and the discount amount per item is "
					+ benifitVo.getDiscount());

		} else if (benifitVo.getDiscountSubType().equals(DiscountSubTypes.AllItems)) {
			discountDiscription.append("The discount amount will be " + benifitVo.getDiscount() + ".");

		}
		return discountDiscription.toString();
	}

	// For Invoice Level Benefits Calculation

	public List<LineItemVo> calculateInvoiceLevelBenefits(PromotionsEntity promo, BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {

		List<LineItemVo> resultList = null;
		for (LineItemVo itemVo : listofLineItems) {

			switch (benfitEntity.getBenfitType()) {

			case FlatDiscount:
				resultList = calcolateFlatDiscountForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems, promoEligibleLineItems);
				break;

			case XunitsFromBuyPool:
				resultList = calculateXUBForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems, promo, promoEligibleLineItems);
				break;

			case XunitsFromGetPool:
				resultList = calculateXUGForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems, promo,
						promoEligibleLineItems);
				break;

			default:
				break;

			}
		}
		return resultList;

	}

	private List<LineItemVo> calculateXUGForDiscountType(BenfitEntity benfitEntity, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofLineItems, PromotionsEntity promo, List<LineItemVo> promoEligibleLineItems) {

//		List<LineItemVo> promoEligibleLineItems = getPromoEligibleLineItems(listofLineItems, promo, totalQuantityAndMrp,
//				benfitEntity);
		List<LineItemVo> calculateXUBDiscountForPercentageDiscountOn = null;
		List<LineItemVo> calculateXUBDiscountForRupeesDiscountOn = null;
		List<LineItemVo> calculateXUBDiscountForFixedAmountOn = null;

		switch (benfitEntity.getDiscountType()) {

		case PercentageDiscountOn:
			calculateXUBDiscountForPercentageDiscountOn = calcolateXUGDiscountForPercentageDiscountOn(benfitEntity,
					totalQuantityAndMrp, promoEligibleLineItems, listofLineItems);
			break;

		case RupeesDiscountOn:
			calculateXUBDiscountForRupeesDiscountOn = calculateXUGDiscountForRupeesDiscountOn(benfitEntity,
					promoEligibleLineItems, totalQuantityAndMrp, listofLineItems);
			break;

		case FixedAmountOn:
			calculateXUBDiscountForFixedAmountOn = calculateXUGDiscountForFixedAmountOn(benfitEntity,
					totalQuantityAndMrp, promoEligibleLineItems, listofLineItems);
			break;

		default:
			break;

		}

		return listofLineItems;
	}

	private List<LineItemVo> calculateXUGDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems,
			List<LineItemVo> listofAllLineItems) {

		double invoiceLevelDiscount = 0.0;
		List<LineItemVo> distributeXUGDiscount = null;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = benfitEntity.getPoolEntities();

		// get the eligible line items from get pool
		List<LineItemVo> eligibleLineItemsFromGetPools = fetchEligibleLineItemsFromGetPools(listofAllLineItems,
				poolEntities);

		// get the number of items from get pool
		int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());

		// checking the get pools line items size
		if (eligibleLineItemsFromGetPools.size() >= numOfItemsFromGetPool) {

			if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {

				invoiceLevelDiscount = (benfitEntity.getNumOfItemsFromGetPool()
						* Long.parseLong(benfitEntity.getDiscount()));
				distributeXUGDiscount = distributeXUBDiscountToAllProductsInFixedAmountOnForEachItemAndAllItems(
						promoEligibleLineItems, invoiceLevelDiscount, totalQuantityAndMrp);

			} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {

				invoiceLevelDiscount = Long.parseLong(benfitEntity.getDiscount());
				distributeXUGDiscount = distributeXUBDiscountToAllProductsInFixedAmountOnForEachItemAndAllItems(
						promoEligibleLineItems, invoiceLevelDiscount, totalQuantityAndMrp);

			}
		} else {

			for (LineItemVo lineItemVo : listofAllLineItems) {

				if (promoEligibleLineItems.indexOf(lineItemVo) >= 0) {

					lineItemVo.setDescription(
							"The customer can buy products from get pools beacuase the customer is buying products from buy pool");

				}

			}

		}

		return distributeXUGDiscount;

	}

	private List<LineItemVo> calculateXUGDiscountForRupeesDiscountOn(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems) {

		double calculatedInvoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = benfitEntity.getPoolEntities();

		// get the eligible line items from get pool
		List<LineItemVo> eligibleLineItemsFromGetPools = fetchEligibleLineItemsFromGetPools(listofAllLineItems,
				poolEntities);

		// get the number of items from get pool
		int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());

		double rupeesDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		if (eligibleLineItemsFromGetPools.size() >= numOfItemsFromGetPool) {

			calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount + rupeesDiscount;

		} else {

			for (LineItemVo lineItemVo : listofAllLineItems) {

				if (promoEligibleLineItems.indexOf(lineItemVo) >= 0) {

					lineItemVo.setDescription(
							"The customer can buy products from get pools beacuase the customer is buying products from buy pool");

				}

			}

		}

		return distributeDiscountToAllProductsInRupees(eligibleLineItemsFromGetPools, calculatedInvoiceLevelDiscount,
				rupeesDiscount);

	}

	private List<LineItemVo> calcolateXUGDiscountForPercentageDiscountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems,
			List<LineItemVo> listofAllLineItems) {

		List<LineItemVo> results = null;

		switch (benfitEntity.getItemValue()) {
		case MinValue: {

			results = getXugPercentageDiscountForMinimumValue(benfitEntity, promoEligibleLineItems, totalQuantityAndMrp,
					listofAllLineItems);

			break;

		}

		case MaxValue: {

			results = getXugPercentageDiscountForMaximumValue(benfitEntity, promoEligibleLineItems, totalQuantityAndMrp,
					listofAllLineItems);

			break;
		}

		default:
			results = listofAllLineItems;
			break;
		}

		return null;
	}

	private List<LineItemVo> getXugPercentageDiscountForMaximumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems) {

		double calculatedInvoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = benfitEntity.getPoolEntities();

		// LineItemVo maxValueMRP = orderedLineItems.get(orderedLineItems.size());

		double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		List<LineItemVo> orderedLineItems = sortLineItemsByPrice(
				fetchEligibleLineItemsFromGetPools(promoEligibleLineItems, poolEntities));

		Long numOfItemsFromGetPool = benfitEntity.getNumOfItemsFromGetPool();

		if (orderedLineItems.size() >= numOfItemsFromGetPool) {

			for (int i = orderedLineItems.size(); i > (orderedLineItems.size() - numOfItemsFromGetPool); i--) {

				LineItemVo maxValueMRP = orderedLineItems.get(i - 1);

				calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
						+ (percentageDiscount * maxValueMRP.getItemPrice()) / 100;

			}
		} else {

			for (LineItemVo lineItemVo : listofAllLineItems) {

				if (promoEligibleLineItems.indexOf(lineItemVo) >= 0) {

					lineItemVo.setDescription(
							"The customer can buy products from get pools beacuase the customer is buying products from buy pool");

				}

			}

		}

		return distributeDiscountToAllProductsInPercentage(listofAllLineItems, calculatedInvoiceLevelDiscount,
				percentageDiscount);
	}

	// calculate x units from get pool discount for minimum value

	private List<LineItemVo> getXugPercentageDiscountForMinimumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems) {

		double calculateInvoiceLevelDiscount = 0.0;
		// double barcodeLevelTotalDiscount = 0.0;

		// get the number of items from get pool and check the all line items

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = benfitEntity.getPoolEntities();

		// get the eligible line items from get pool
		List<LineItemVo> eligibleLineItemsFromGetPools = fetchEligibleLineItemsFromGetPools(promoEligibleLineItems,
				poolEntities);

		// sort the eligibleLineItemsFromGetPools
		List<LineItemVo> orderedGetItems = sortLineItemsByPrice(eligibleLineItemsFromGetPools);

		// get the number of items from get pool
		int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());

		// get the discount from benefit
		double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		// checking the eligible line items size
		if (eligibleLineItemsFromGetPools.size() >= numOfItemsFromGetPool) {

			int val = 0;

			// loop through, discount calculation

			for (int i = numOfItemsFromGetPool; i > val; i--) {

				LineItemVo minValueMRP = orderedGetItems.get(i - 1);

				calculateInvoiceLevelDiscount = calculateInvoiceLevelDiscount
						+ (percentageDiscount * minValueMRP.getItemPrice()) / 100;

			}

		} else {

			for (LineItemVo lineItemVo : listofAllLineItems) {

				if (promoEligibleLineItems.indexOf(lineItemVo) >= 0) {

					lineItemVo.setDescription(
							"The customer can buy products from get pools beacuase the customer is buying products from buy pool");

				}

//				barcodeLevelTotalDiscount = barcodeLevelTotalDiscount + lineItemVo.getDiscount();

			}

		}

		return distributeDiscountToAllProductsInPercentage(listofAllLineItems, calculateInvoiceLevelDiscount,
				percentageDiscount);

	}

	private List<LineItemVo> calculateXUBForDiscountType(BenfitEntity benfitEntity, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofLineItems, PromotionsEntity promo, List<LineItemVo> promoEligibleLineItems) {

//		List<LineItemVo> promoEligibleLineItems = getPromoEligibleLineItems(listofLineItems, promo, totalQuantityAndMrp,
//				benfitEntity);

		List<LineItemVo> calculateXUBDiscountForPercentageDiscountOn = null;
		List<LineItemVo> calculateXUBDiscountForRupeesDiscountOn = null;
		List<LineItemVo> calculateXUBDiscountForFixedAmountOn = null;

		if (promoEligibleLineItems.size() > 0) {

			switch (benfitEntity.getDiscountType()) {

			case PercentageDiscountOn:
				calculateXUBDiscountForPercentageDiscountOn = calcolateXUBDiscountForPercentageDiscountOn(benfitEntity,
						totalQuantityAndMrp, promoEligibleLineItems, listofLineItems);
				break;

			case RupeesDiscountOn:
				calculateXUBDiscountForRupeesDiscountOn = calculateXUBDiscountForRupeesDiscountOn(benfitEntity,
						promoEligibleLineItems, totalQuantityAndMrp);
				break;

			case FixedAmountOn:
				calculateXUBDiscountForFixedAmountOn = calculateXUBDiscountForFixedAmountOn(benfitEntity,
						totalQuantityAndMrp, promoEligibleLineItems);
				break;

			default:
				break;

			}
		}

		// through we are are not calculating invoice level discount when promo eligible
		// line items are zero, we have to
		// distribute barcode level discount to all the line items

		return listofLineItems;
	}

	private List<LineItemVo> calculateXUBDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems) {

		double invoiceLevelDiscount = 0.0;

		List<LineItemVo> distributeXUBDiscount = null;

		if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {

			invoiceLevelDiscount = (benfitEntity.getNumOfItemsFromBuyPool()
					* Long.parseLong(benfitEntity.getDiscount()));
			distributeXUBDiscount = distributeXUBDiscountToAllProductsInFixedAmountOnForEachItemAndAllItems(
					promoEligibleLineItems, invoiceLevelDiscount, totalQuantityAndMrp);

		} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {

			invoiceLevelDiscount = Long.parseLong(benfitEntity.getDiscount());
			distributeXUBDiscount = distributeXUBDiscountToAllProductsInFixedAmountOnForEachItemAndAllItems(
					promoEligibleLineItems, invoiceLevelDiscount, totalQuantityAndMrp);

		}

		return distributeXUBDiscount;
	}

	// XUB For Fixed AmountOn in EachItem, All Items distribution
	private List<LineItemVo> distributeXUBDiscountToAllProductsInFixedAmountOnForEachItemAndAllItems(
			List<LineItemVo> promoEligibleLineItems, double invoiceLevelDiscount, List<Double> totalQuantityAndMrp) {

		double barcodeLevelTotalDiscount = promoEligibleLineItems.stream().mapToDouble(item -> item.getDiscount())
				.sum();

		List<LineItemVo> resultList = new LinkedList<>();

		for (LineItemVo lineItemVo : promoEligibleLineItems) {
			// x = (100 * currentItemPrice)/ totalMRP
			// discountAmountForThisItem = x/100 * totalDiscount
			double discountPercentagePerItem = (100 * lineItemVo.getItemPrice()) / totalQuantityAndMrp.get(1);
			Double discountAmountForThisItem = (discountPercentagePerItem * invoiceLevelDiscount)/100;
			lineItemVo.setDiscount(discountAmountForThisItem.longValue());
			resultList.add(lineItemVo);
		}

		return resultList;
	}

	private List<LineItemVo> calculateXUBDiscountForRupeesDiscountOn(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp) {

		double rupeesDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		List<LineItemVo> distributeXUBDiscountToAllProductsInRupees = distributeXUBDiscountToAllProductsInRupees(
				promoEligibleLineItems, rupeesDiscount, totalQuantityAndMrp);

		return distributeXUBDiscountToAllProductsInRupees;
	}

	private List<LineItemVo> distributeXUBDiscountToAllProductsInRupees(List<LineItemVo> promoEligibleLineItems,
			double rupeesDiscount, List<Double> totalQuantityAndMrp) {
		double barcodeLevelTotalDiscount = promoEligibleLineItems.stream().mapToDouble(item -> item.getDiscount())
				.sum();

		List<LineItemVo> resultList = new LinkedList<>();

		for (LineItemVo lineItemVo : promoEligibleLineItems) {
			// x = (100 * currentItemPrice)/ totalMRP
			// discountAmountForThisItem = x/100 * totalDiscount
			double discountPercentagePerItem = (100 * lineItemVo.getItemPrice()) / totalQuantityAndMrp.get(1);
			Double discountAmountForThisItem = (discountPercentagePerItem * rupeesDiscount)/100;
			lineItemVo.setDiscount(discountAmountForThisItem.longValue());
			resultList.add(lineItemVo);
		}

		return resultList;
	}

	private List<LineItemVo> calcolateXUBDiscountForPercentageDiscountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems,
			List<LineItemVo> listofAllLineItems) {

		Double xubDiscount = 0.0;

		List<LineItemVo> results = null;

		switch (benfitEntity.getItemValue()) {
		case MinValue: {

			xubDiscount = getPercentageDiscountForMinimumValue(benfitEntity, promoEligibleLineItems,
					totalQuantityAndMrp);
			results = distributeDiscountToAllProductsInPercentage(listofAllLineItems, xubDiscount,
					Double.valueOf(benfitEntity.getDiscount()).doubleValue());

			break;

		}

		case MaxValue:

			xubDiscount = getPercentageDiscountForMaximumValue(benfitEntity, promoEligibleLineItems,
					totalQuantityAndMrp);
			results = distributeDiscountToAllProductsInPercentage(listofAllLineItems, xubDiscount,
					Double.valueOf(benfitEntity.getDiscount()).doubleValue());

			break;

		default:
			results = listofAllLineItems;
			break;
		}

		return results;
	}

	// for getDiscountForMaximumValue method
	private Double getPercentageDiscountForMaximumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp) {

		List<LineItemVo> orderedLineItems = sortLineItemsByPrice(promoEligibleLineItems);

		double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();
		// LineItemVo maxValueMRP = orderedLineItems.get(orderedLineItems.size());

		double calculatedInvoiceLevelDiscount = 0.0;

		Long numOfItemsFromBuyPool = benfitEntity.getNumOfItemsFromBuyPool();

		for (int i = orderedLineItems.size(); i > (orderedLineItems.size() - numOfItemsFromBuyPool); i--) {

			LineItemVo maxValueMRP = orderedLineItems.get(i - 1);

			calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
					+ (percentageDiscount * maxValueMRP.getItemPrice()) / 100;

		}

		return calculatedInvoiceLevelDiscount;
	}

	// for getDiscountForManimumValue method
	private Double getPercentageDiscountForMinimumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp) {

		List<LineItemVo> orderedLineItems = sortLineItemsByPrice(promoEligibleLineItems);

		double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		int numOfItemsFromBuyPool = Integer.valueOf(benfitEntity.getNumOfItemsFromBuyPool().intValue());

		double calculatedInvoiceLevelDiscount = 0.0;
		int val = 0;

		for (int i = numOfItemsFromBuyPool; i > val; i--) {

			LineItemVo minValueMRP = orderedLineItems.get(i - 1);

			calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
					+ (percentageDiscount * minValueMRP.getItemPrice()) / 100;

		}

		return calculatedInvoiceLevelDiscount;
	}

	private List<LineItemVo> sortLineItemsByPrice(List<LineItemVo> promoEligibleLineItems) {

		Comparator<LineItemVo> lineItemComparatoItemPrice = Comparator.comparing(LineItemVo::getItemPrice);
		Collections.sort(promoEligibleLineItems, lineItemComparatoItemPrice);

		return promoEligibleLineItems;
	}

	public List<LineItemVo> getPromoEligibleLineItems(List<LineItemVo> lineItems, PromotionsEntity promotion,
			List<Double> totalQuantityAndMrp, BenfitEntity benefitEntity) {
		List<LineItemVo> promoEligibleLineItems = new ArrayList<>();

		for (LineItemVo lineItemVo : lineItems) {

			ProductTextileVo productTextile = convertLineItemsIntoProductTextile(lineItemVo);

			if (checkPoolRules.checkPools(promotion.getPoolEntity(), productTextile)) {

				promoEligibleLineItems.add(lineItemVo);

			}

		}

		return promoEligibleLineItems;

	}

	public List<LineItemVo> fetchEligibleLineItemsFromGetPools(List<LineItemVo> allLineItems,
			List<PoolEntity> listOfGetPools) {
		List<LineItemVo> promoEligibleLineItems = new ArrayList<>();

		for (LineItemVo lineItemVo : allLineItems) {

			ProductTextileVo productTextile = convertLineItemsIntoProductTextile(lineItemVo);

			if (checkPoolRules.checkPools(listOfGetPools, productTextile)) {

				promoEligibleLineItems.add(lineItemVo);

			}

		}

		return promoEligibleLineItems;

	}
  
	//converting listOfLineItemsToProductTextile
	public ProductTextileVo convertLineItemsIntoProductTextile(LineItemVo lineItem) {

		ProductTextileVo productTextile = new ProductTextileVo();

		productTextile.setQty(lineItem.getQuantity());
		productTextile.setItemMrp(lineItem.getItemPrice());
		productTextile.setSection(lineItem.getSection());
		productTextile.setSubSection(lineItem.getSubSection());
		productTextile.setCostPrice(lineItem.getGrossValue());
		productTextile.setDivision(lineItem.getDivision());

		// We need to find out relevant fields of line item vo to set to the respective
		// fields to the lineItemTextile

		productTextile.setBatchNo("Batch No");
		productTextile.setUom("Units");
		productTextile.setOriginalBarcodeCreatedAt(LocalDate.now());

		return productTextile;

	}
	
	//converting listOfProductTextileToLineItems
	
	public List<LineItemVo> convertProductTextileIntoLineItems(List<ProductTextileVo> pvos) {

		 List<LineItemVo> lineItemsList = new ArrayList<>();
		
		pvos.stream().forEach(p->{
			
		       LineItemVo lineItemsVo = new LineItemVo();

				lineItemsVo.setQuantity(p.getQty());
				lineItemsVo.setItemPrice(Float.valueOf(p.getItemMrp()).longValue());
				lineItemsVo.setSection(p.getSection());
				lineItemsVo.setSubSection(p.getSubSection());
				lineItemsVo.setDivision(p.getDivision());
				lineItemsList.add(lineItemsVo);
		
		});

//
//		// We need to find out relevant fields of line item vo to set to the respective
//		// fields to the lineItemTextile
//
////		lineItemsVo.sB("Batch No");
////		lineItemsVo.setUom("Units");
////		lineItemsVo.setOriginalBarcodeCreatedAt(LocalDate.now());
//
		return lineItemsList;

	}

	private List<LineItemVo> calcolateFlatDiscountForDiscountType(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems, List<LineItemVo> promoEligibleLineItems) {

		List<LineItemVo> finalResultList = null;

		switch (benfitEntity.getDiscountType()) {

		case PercentageDiscountOn:
			finalResultList = calcolateFlatDiscountForPercentageDiscountOn(benfitEntity,
					totalQuantityAndMrp, listofLineItems, promoEligibleLineItems);
			break;
		case RupeesDiscountOn:
			finalResultList = calculateFlatDiscountForAllItems(benfitEntity,
					totalQuantityAndMrp, listofLineItems, promoEligibleLineItems);
			break;
		case FixedAmountOn:
			finalResultList = calculateFlatDiscountForFixedAmountOn(benfitEntity,
					totalQuantityAndMrp, listofLineItems, promoEligibleLineItems);
			break;

		default:
			break;

		}

		return finalResultList;

	}

	private List<LineItemVo> calculateFlatDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems, List<LineItemVo> promoEligibleLineItems) {
		if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {
			return calculateFlatDiscountForEachItem(benfitEntity, totalQuantityAndMrp, listofLineItems, promoEligibleLineItems);

		} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {
			return calculateFlatDiscountForAllItems(benfitEntity, totalQuantityAndMrp, listofLineItems,promoEligibleLineItems);

		}

		return null;
	}

	private List<LineItemVo> calculateFlatDiscountForEachItem(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems, List<LineItemVo> promoEligibleLineItems) {
		List<LineItemVo> resultList = new LinkedList<>();
		Double totalMrp = totalQuantityAndMrp.get(1);
		for (LineItemVo lineItemVo : resultList) {
			int quantity = lineItemVo.getQuantity();
			String discount = benfitEntity.getDiscount();
			double calculatedInvoiceLevelDiscount = quantity * (Double.valueOf(discount)) - totalMrp;
			lineItemVo.setDiscount(Double.valueOf(calculatedInvoiceLevelDiscount).longValue());
			resultList.add(lineItemVo);
		}

		return resultList;

	}

	private List<LineItemVo> calculateFlatDiscountForAllItems(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems, List<LineItemVo> promoEligibleLineItems) {
		// double rupees = Double.valueOf(benfitEntity.getDiscount()).doubleValue();
		// int discountAmount= Integer.valueOf(benfitEntity.getDiscount()).intValue();
		// Double totalMrp = totalQuantityAndMrp.get(1);
		List<LineItemVo> results = distributeDiscountToAllProductsInRupees(listofLineItems,
				Double.valueOf(benfitEntity.getDiscount()).doubleValue(), totalQuantityAndMrp.get(1));

		return results;
	}

	private List<LineItemVo> distributeDiscountToAllProductsInRupees(List<LineItemVo> listofLineItems,
			double calculatedInvoiceLevelDiscount, double totalMrp) {
		double barcodeLevelTotalDiscount = listofLineItems.stream().mapToDouble(item -> item.getDiscount()).sum();
		double totalDiscount = calculatedInvoiceLevelDiscount + barcodeLevelTotalDiscount;
		List<LineItemVo> resultList = new LinkedList<>();

		for (LineItemVo lineItemVo : listofLineItems) {
			// x = (100 * currentItemPrice)/ totalMRP
			// discountAmountForThisItem = x/100 * totalDiscount
			double discountPercentagePerItem = (100 * lineItemVo.getItemPrice()) / totalMrp;
			
			//need to check this equation
			Double discountAmountForThisItem = (discountPercentagePerItem * totalDiscount)/100;
			lineItemVo.setDiscount(discountAmountForThisItem.longValue());
			resultList.add(lineItemVo);
		}

		return resultList;
	}

	private List<LineItemVo> calcolateFlatDiscountForPercentageDiscountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems, List<LineItemVo> promoEligibleLineItems) {

		double percentage = Double.valueOf(benfitEntity.getDiscount()).doubleValue();
		Double totalMrp = totalQuantityAndMrp.get(1);
		double calculatedInvoiceLevelDiscount = (percentage * totalMrp) / 100;
		List<LineItemVo> results = distributeDiscountToAllProductsInPercentage(listofLineItems,
				calculatedInvoiceLevelDiscount, percentage);
		// System.out.println("Total discount : "+calculatedDiscountAmount);

		return results;
	}

	private List<LineItemVo> distributeDiscountToAllProductsInPercentage(List<LineItemVo> listofLineItems,
			double calculatedInvoiceLevelDiscount, double percentage) {

		//double barcodeLevelTotalDiscount = listofLineItems.stream().mapToDouble(item -> item.getDiscount().doubleValue()).sum();
		//double totalDiscount = calculatedInvoiceLevelDiscount + barcodeLevelTotalDiscount;
		List<LineItemVo> resultList = new LinkedList<>();
		for (LineItemVo lineItemVo : listofLineItems) {

			Double discountAmountToBeAddedToTheProduct = (percentage * lineItemVo.getItemPrice()) / 100;
			lineItemVo.setDiscount(discountAmountToBeAddedToTheProduct.longValue());
			resultList.add(lineItemVo);
		}

		return resultList;
	}

	/*
	 * public static void main(String[] args) { CalculateBenifits
	 * calculateBenifits=new CalculateBenifits(); List<Double>
	 * totalQuantityAndMrp=new LinkedList<>(); totalQuantityAndMrp.add(10.00);
	 * totalQuantityAndMrp.add(10000.00); BenfitEntity benfitEntity=new
	 * BenfitEntity(); benfitEntity.setDiscount("50");
	 * calculateBenifits.calcolateFlatDiscountForPercentageDiscountOn(benfitEntity,
	 * totalQuantityAndMrp, null);
	 * 
	 * }
	 */

	// distribute discount to all line items
	public List<LineItemVo> distributeDiscountToAllProductsAndAllLineItems(List<LineItemVo> listofLineItemsTxt,
			List<Double> totalQuantityAndMrp) {
		List<LineItemVo> resultList = new LinkedList<>();

		for (LineItemVo lineItemVo : listofLineItemsTxt) {
			// x = (100 * currentItemPrice)/ totalMRP
			// discountAmountForThisItem = x/100 * totalDiscount
			double discountPercentagePerItem = (100 * lineItemVo.getItemPrice()) / totalQuantityAndMrp.get(1);
			Double discountAmountForThisItem = (discountPercentagePerItem / 100);
			lineItemVo.setDiscount(discountAmountForThisItem.longValue());
			resultList.add(lineItemVo);
		}

		return resultList;
	}

}
