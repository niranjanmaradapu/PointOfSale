package com.otsi.retail.promotions.calculate.benefits;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.check.pools.CheckPoolRules;
import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountSubTypes;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.common.PoolType;
import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.CalculatedDiscountsVo;
import com.otsi.retail.promotions.vo.LineItemVo;
import com.otsi.retail.promotions.vo.ProductVO;

@Component
public class CalculateBenifits {

	@Autowired
	private CheckPoolRules checkPoolRules;

	public CalculatedDiscountsVo calculate(List<BenefitVo> benifitVos, ProductVO productTextileVo) {

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

	private String calculateBeniftForFlatDIscount(BenefitVo benifitVo, ProductVO productTextileVo) {

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

	private String calculateBeniftForPercentageDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {

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
			int purchasedQuantity = productTextileVo.getQuantity();
			float valueOfMRP = productTextileVo.getItemMrp();
			// calculatedDiscountAmount=
			// (discountPercentage/100)*(purchasedQuantity*valueOfMRP) +"";
			calculatedDiscountAmount = (valueOfMRP / 100) * purchasedQuantity * discountPercentage + "";

		}
		return calculatedDiscountAmount;

	}

	private String calculateBeniftForRupeesDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {
		String calculatedDiscountAmount = "";
		if (/* benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP) || */ benifitVo.getDiscountSubType()
				.equals(DiscountSubTypes.ItemMRP)) {
			int discountAmount = Integer.valueOf(benifitVo.getDiscount()).intValue();
			int purchasedQuantity = productTextileVo.getQuantity();
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

	private String calculateBenifitsForXUnitsFromBuyPool(BenefitVo benifitVo, ProductVO productTextileVo) {

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

	private String calculateBenifitsForXUBAndPercentageDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {

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

	private String calculateBenifitsForXUBAndRupeesDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {

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

	private String calculateBenifitsForXUBAndFixedAmountOn(BenefitVo benifitVo, ProductVO productTextileVo) {

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

	private String calculateBenifitsForXUnitsFromGetPool(BenefitVo benifitVo, ProductVO productTextileVo) {
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

	private String calculateBenifitsForXUGAndPercentageDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {
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

	private String calculateBenifitsForXUGAndRupeesDiscountOn(BenefitVo benifitVo, ProductVO productTextileVo) {
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

	private String calculateBenifitsForXUGAndFixedAmountOn(BenefitVo benifitVo, ProductVO productTextileVo) {
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
//		for (LineItemVo itemVo : listofLineItems) {

		switch (benfitEntity.getBenfitType()) {

		case FlatDiscount:
			resultList = calcolateFlatDiscountForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems,
					promoEligibleLineItems);
			break;

		case XunitsFromBuyPool:
			resultList = calculateXUBForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems, promo,
					promoEligibleLineItems);
			break;

		case XunitsFromGetPool:
			resultList = calculateXUGForDiscountType(benfitEntity, totalQuantityAndMrp, listofLineItems, promo,
					promoEligibleLineItems);
			break;

		default:
			resultList = listofLineItems;
			break;

		}
		// }
		return resultList;

	}

	private List<LineItemVo> calculateXUGForDiscountType(BenfitEntity benfitEntity, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofLineItems, PromotionsEntity promo, List<LineItemVo> promoEligibleLineItems) {

		List<LineItemVo> resultList = null;

		switch (benfitEntity.getDiscountType()) {

		case PercentageDiscountOn:
			resultList = calcolateXUGDiscountForPercentageDiscountOn(benfitEntity, totalQuantityAndMrp,
					promoEligibleLineItems, listofLineItems, promo);
			break;

		case RupeesDiscountOn:
			resultList = calculateXUGDiscountForRupeesDiscountOn(benfitEntity, promoEligibleLineItems,
					totalQuantityAndMrp, listofLineItems, promo);
			break;

		case FixedAmountOn:
			resultList = calculateXUGDiscountForFixedAmountOn(benfitEntity, totalQuantityAndMrp, promoEligibleLineItems,
					listofLineItems, promo);
			break;

		default:
			resultList = listofLineItems;
			break;

		}

		return resultList;
	}

	private List<LineItemVo> calculateXUGDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems,
			List<LineItemVo> listofAllLineItems, PromotionsEntity promo) {

		double invoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = promo.getPoolEntity();

		// get the eligible line items from get pool
		List<LineItemVo> eligibleLineItemsFromGetPools = fetchBenefitEligibleLineItems(listofAllLineItems,
				poolEntities);

		// get the number of items from get pool
		int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());

		// checking the get pools line items size
		if (eligibleLineItemsFromGetPools.size() >= numOfItemsFromGetPool) {

			if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {

				invoiceLevelDiscount = (benfitEntity.getNumOfItemsFromGetPool()
						* Long.parseLong(benfitEntity.getDiscount()));

			} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {

				invoiceLevelDiscount = Long.parseLong(benfitEntity.getDiscount());

			}

		} else {

			for (LineItemVo lineItemVo : promoEligibleLineItems)
				lineItemVo.setDescription(
						"The customer will get discount if he buys products from get pools beacuase the customer is buying products from the respective buy pools");

			for (LineItemVo lineItemVo : eligibleLineItemsFromGetPools) {
				int noOfProductsYetToBeBought = numOfItemsFromGetPool - eligibleLineItemsFromGetPools.size();
				lineItemVo.setDescription("If the customer buys " + noOfProductsYetToBeBought
						+ " more products from get pools, he will get the discount.");

			}

		}

		return distributeDiscountToAllProducts(listofAllLineItems, invoiceLevelDiscount);

	}

	private List<LineItemVo> calculateXUGDiscountForRupeesDiscountOn(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems, PromotionsEntity promo) {

		double calculatedInvoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities = benfitEntity.getPoolEntities();
		
		PoolType poolType = null;

		for (PoolEntity poolEntity : poolEntities) {

			poolType = poolEntity.getPoolType();
		}
		if (poolType.equals(PoolType.Get)) {

		// get the eligible line items from get pool
		List<LineItemVo> eligibleLineItemsFromGetPools = fetchBenefitEligibleLineItems(listofAllLineItems,
				poolEntities);

		// get the number of items from get pool
		int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());
		long qty = 0l;
		for (LineItemVo lineItemVo : eligibleLineItemsFromGetPools) {

			qty = lineItemVo.getQuantity();
		}

		if (eligibleLineItemsFromGetPools.size() >= numOfItemsFromGetPool || qty >= numOfItemsFromGetPool) {

			calculatedInvoiceLevelDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		} else {

			for (LineItemVo lineItemVo : promoEligibleLineItems)
				lineItemVo.setDescription(
						"The customer will get discount if he buys products from get pools beacuase the customer is buying products from the respective buy pools");

			for (LineItemVo lineItemVo : eligibleLineItemsFromGetPools) {
				int noOfProductsYetToBeBought = numOfItemsFromGetPool - eligibleLineItemsFromGetPools.size();
				lineItemVo.setDescription("If the customer buys " + noOfProductsYetToBeBought
						+ " more products from get pools, he will get the discount.");

			}
		}

		}

		return distributeDiscountToAllProducts(listofAllLineItems, calculatedInvoiceLevelDiscount);

	}

	private List<LineItemVo> calcolateXUGDiscountForPercentageDiscountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems,
			List<LineItemVo> listofAllLineItems, PromotionsEntity promo) {

		List<LineItemVo> results = null;

		switch (benfitEntity.getItemValue()) {
		case MinValue: {

			results = getXugPercentageDiscountForMinimumValue(benfitEntity, promoEligibleLineItems, totalQuantityAndMrp,
					listofAllLineItems, promo);

			break;

		}

		case MaxValue: {

			results = getXugPercentageDiscountForMaximumValue(benfitEntity, promoEligibleLineItems, totalQuantityAndMrp,
					listofAllLineItems, promo);

			break;
		}

		default:
			results = listofAllLineItems;
			break;
		}

		return results;
	}

	private List<LineItemVo> getXugPercentageDiscountForMaximumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems, PromotionsEntity promo) {

		double calculatedInvoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		List<PoolEntity> poolEntities= benfitEntity.getPoolEntities();

		PoolType poolType = null;
		for (PoolEntity poolEntity : poolEntities) {

			poolType = poolEntity.getPoolType();

		}
		if (poolType.equals(PoolType.Get)) {
			double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

			List<LineItemVo> orderedLineItems = sortLineItemsByPrice(
					fetchBenefitEligibleLineItems(listofAllLineItems, poolEntities));

			Long numOfItemsFromGetPool = benfitEntity.getNumOfItemsFromGetPool();

			long qty = 0l;
			for (LineItemVo lineItemVo : orderedLineItems) {

				qty = lineItemVo.getQuantity();
			}

			if (orderedLineItems.size() >= numOfItemsFromGetPool || qty >= numOfItemsFromGetPool) {

				for (int i = numOfItemsFromGetPool.intValue() - 1; i > -1; i--) {

					LineItemVo maxValueMRP = orderedLineItems.get(i);

					Double individualLineItemTotalPrice = maxValueMRP.getItemPrice().doubleValue()
							* maxValueMRP.getQuantity();

					calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
							+ (percentageDiscount * individualLineItemTotalPrice) / 100;
					if (i < numOfItemsFromGetPool)
						break;

				}
			} else {

				for (LineItemVo lineItemVo : promoEligibleLineItems)
					lineItemVo.setDescription(
							"The customer will get discount if he buys products from get pools beacuase the customer is buying products from the respective buy pools");

				for (LineItemVo lineItemVo : orderedLineItems) {
					long noOfProductsYetToBeBought = numOfItemsFromGetPool - orderedLineItems.size();
					lineItemVo.setDescription("If the customer buys " + noOfProductsYetToBeBought
							+ " more products from get pools, he will get the discount.");

				}

			}
		} else {

			for (LineItemVo lineItemVo : promoEligibleLineItems) {
				lineItemVo.setDescription("Please choose the items from respective get pools only!!");
			}
		}

		return distributeDiscountToAllProducts(listofAllLineItems, calculatedInvoiceLevelDiscount);
	}

	// calculate x units from get pool discount for minimum value

	private List<LineItemVo> getXugPercentageDiscountForMinimumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofAllLineItems, PromotionsEntity promo) {

		double calculateInvoiceLevelDiscount = 0.0;

		// get the getPools from the benefits
		
		List<PoolEntity> poolEntities= benfitEntity.getPoolEntities();
		
		PoolType poolType = null;

		for (PoolEntity poolEntity : poolEntities) {

			poolType = poolEntity.getPoolType();
		}
		List<LineItemVo> benefitEligibleLineItems = null;
		if (poolType.equals(PoolType.Get)) {
			// get the eligible line items from get pool
			benefitEligibleLineItems = fetchBenefitEligibleLineItems(listofAllLineItems, poolEntities);

			// sort the eligibleLineItemsFromGetPools
			List<LineItemVo> orderedGetItems = sortLineItemsByPrice(benefitEligibleLineItems);

			// get the number of items from get pool
			int numOfItemsFromGetPool = Integer.valueOf(benfitEntity.getNumOfItemsFromGetPool().intValue());

			// get the discount from benefit
			double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

			long qty = 0l;

			for (LineItemVo lineItemVo : orderedGetItems) {

				qty = lineItemVo.getQuantity();

			}

			// checking the eligible line items size
			if (benefitEligibleLineItems.size() >= numOfItemsFromGetPool || qty >= numOfItemsFromGetPool) {

				// loop through, discount calculation

				for (int i = 0; i < numOfItemsFromGetPool; i++) {

					LineItemVo minValueMRP = orderedGetItems.get(i);

					Double individualLineItemTotalPrice = minValueMRP.getItemPrice().doubleValue()
							* minValueMRP.getQuantity();

					calculateInvoiceLevelDiscount = calculateInvoiceLevelDiscount
							+ (percentageDiscount * individualLineItemTotalPrice) / 100;
					if (i < numOfItemsFromGetPool)
						break;

				}

			} else {

				for (LineItemVo lineItemVo : promoEligibleLineItems)
					lineItemVo.setDescription(
							"The customer will get discount if he buys products from get pools beacuase the customer is buying products from the respective buy pools");

				for (LineItemVo lineItemVo : benefitEligibleLineItems) {
					int noOfProductsYetToBeBought = numOfItemsFromGetPool - benefitEligibleLineItems.size();
					lineItemVo.setDescription("If the customer buys " + noOfProductsYetToBeBought
							+ " more products from get pools, he will get the discount.");

				}

			}
		} else {

			for (LineItemVo lineItemVo : promoEligibleLineItems) {
				lineItemVo.setDescription("please choose the items from respective get pools only");
			}
		}

		return distributeDiscountToAllProducts(listofAllLineItems, calculateInvoiceLevelDiscount);

	}

	private List<LineItemVo> calculateXUBForDiscountType(BenfitEntity benfitEntity, List<Double> totalQuantityAndMrp,
			List<LineItemVo> listofLineItems, PromotionsEntity promo, List<LineItemVo> promoEligibleLineItems) {

		List<LineItemVo> resultList = null;

		if (promoEligibleLineItems.size() > 0) {

			switch (benfitEntity.getDiscountType()) {

			case PercentageDiscountOn:
				resultList = calcolateXUBDiscountForPercentageDiscountOn(benfitEntity, totalQuantityAndMrp,
						promoEligibleLineItems, listofLineItems);
				break;

			case RupeesDiscountOn:
				resultList = calculateXUBDiscountForRupeesDiscountOn(benfitEntity, promoEligibleLineItems,
						totalQuantityAndMrp);
				break;

			case FixedAmountOn:
				resultList = calculateXUBDiscountForFixedAmountOn(benfitEntity, totalQuantityAndMrp,
						promoEligibleLineItems);
				break;

			default:
				resultList = listofLineItems;
				break;

			}
		}

		return resultList;
	}

	private List<LineItemVo> calculateXUBDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> promoEligibleLineItems) {

		double invoiceLevelDiscount = 0.0;

		if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {

			invoiceLevelDiscount = (benfitEntity.getNumOfItemsFromBuyPool()
					* Long.parseLong(benfitEntity.getDiscount()));

		} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {

			invoiceLevelDiscount = Long.parseLong(benfitEntity.getDiscount());

		}

		return distributeDiscountToAllProducts(promoEligibleLineItems, invoiceLevelDiscount);

	}

	private List<LineItemVo> calculateXUBDiscountForRupeesDiscountOn(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp) {

		return distributeDiscountToAllProducts(promoEligibleLineItems,
				Double.valueOf(benfitEntity.getDiscount()).doubleValue());

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
			results = distributeDiscountToAllProducts(listofAllLineItems, xubDiscount);

			break;

		}

		case MaxValue:

			xubDiscount = getPercentageDiscountForMaximumValue(benfitEntity, promoEligibleLineItems,
					totalQuantityAndMrp);
			results = distributeDiscountToAllProducts(listofAllLineItems, xubDiscount);

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

		long qty = 0l;
		for (LineItemVo lineItemVo : orderedLineItems) {

			qty = lineItemVo.getQuantity();
		}

		if (orderedLineItems.size() >= numOfItemsFromBuyPool || qty >= numOfItemsFromBuyPool) {
			for (int i = numOfItemsFromBuyPool.intValue() - 1; i > -1; i--) {
				LineItemVo maxValueMRP = orderedLineItems.get(i);

				double individualLineItemTotalPrice = maxValueMRP.getItemPrice().doubleValue()
						* maxValueMRP.getQuantity();

				calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
						+ (percentageDiscount * individualLineItemTotalPrice) / 100;

				if (i < numOfItemsFromBuyPool)
					break;

			}
		} else {

			for (LineItemVo lineItemVo : orderedLineItems) {
				lineItemVo.setDescription("If you select a more items from buy pool then you will get a discount");

			}

		}

		return calculatedInvoiceLevelDiscount;
	}

	// for getDiscountForManimumValue method
	private Double getPercentageDiscountForMinimumValue(BenfitEntity benfitEntity,
			List<LineItemVo> promoEligibleLineItems, List<Double> totalQuantityAndMrp) {

		double calculatedInvoiceLevelDiscount = 0.0;

		List<LineItemVo> orderedLineItems = sortLineItemsByPrice(promoEligibleLineItems);

		double percentageDiscount = Double.valueOf(benfitEntity.getDiscount()).doubleValue();

		int numOfItemsFromBuyPool = Integer.valueOf(benfitEntity.getNumOfItemsFromBuyPool().intValue());

		long qty = 0l;
		for (LineItemVo lineItemVo : orderedLineItems) {

			qty = lineItemVo.getQuantity();
		}

		if (orderedLineItems.size() >= numOfItemsFromBuyPool || qty >= numOfItemsFromBuyPool) {

			for (int i = 0; i < numOfItemsFromBuyPool; i++) {

				LineItemVo minValueMRP = orderedLineItems.get(i);

				Double individualLineItemTotalPrice = minValueMRP.getItemPrice().doubleValue()
						* minValueMRP.getQuantity();

				calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount
						+ (percentageDiscount * individualLineItemTotalPrice) / 100;

				if (i < numOfItemsFromBuyPool)
					break;
			}
		} else {

			for (LineItemVo lineItemVo : orderedLineItems) {

				lineItemVo.setDescription("If you take more items from buy pool then you will get a discount");

			}
		}

		return calculatedInvoiceLevelDiscount;
	}

	private List<LineItemVo> sortLineItemsByPrice(List<LineItemVo> promoEligibleLineItems) {

		Comparator<LineItemVo> lineItemComparatoItemPrice = Comparator.comparing(LineItemVo::getItemPrice);
		Collections.sort(promoEligibleLineItems, lineItemComparatoItemPrice);

		return promoEligibleLineItems;
	}

	public List<LineItemVo> getPromoEligibleLineItems(List<LineItemVo> lineItems, PromotionsEntity promotion,
			BenfitEntity benefitEntity) {
		List<LineItemVo> promoEligibleLineItems = new ArrayList<>();

		for (LineItemVo lineItemVo : lineItems) {

			ProductVO productTextile = convertLineItemsIntoProductTextile(lineItemVo);

			if (checkPoolRules.checkPools(promotion.getPoolEntity(), productTextile)) {

				promoEligibleLineItems.add(lineItemVo);

			}

		}

		return promoEligibleLineItems;

	}

	public List<LineItemVo> fetchBenefitEligibleLineItems(List<LineItemVo> allLineItems,
			List<PoolEntity> listOfGetPools) {
		List<LineItemVo> benefitEligibleLineItems = new ArrayList<>();

		for (LineItemVo lineItemVo : allLineItems) {

			ProductVO productTextile = convertLineItemsIntoProductTextile(lineItemVo);

			if (checkPoolRules.checkPools(listOfGetPools, productTextile)) {

				benefitEligibleLineItems.add(lineItemVo);

			}

		}

		return benefitEligibleLineItems;

	}

	// converting listOfLineItemsToProductTextile
	public ProductVO convertLineItemsIntoProductTextile(LineItemVo lineItem) {

		ProductVO productTextile = new ProductVO();

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

	// converting listOfProductTextileToLineItems

	public List<LineItemVo> convertProductTextileIntoLineItems(List<ProductVO> pvos) {

		List<LineItemVo> lineItemsList = new ArrayList<>();

		pvos.stream().forEach(p -> {

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
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {

		List<LineItemVo> finalResultList = null;

		switch (benfitEntity.getDiscountType()) {

		case PercentageDiscountOn:
			finalResultList = calcolateFlatDiscountForPercentageDiscountOn(benfitEntity, totalQuantityAndMrp,
					listofLineItems, promoEligibleLineItems);
			break;
		case RupeesDiscountOn:
			finalResultList = calculateFlatDiscountForAllItems(benfitEntity, totalQuantityAndMrp, listofLineItems,
					promoEligibleLineItems);
			break;
		case FixedAmountOn:
			finalResultList = calculateFlatDiscountForFixedAmountOn(benfitEntity, totalQuantityAndMrp, listofLineItems,
					promoEligibleLineItems);
			break;

		default:
			finalResultList = listofLineItems;
			break;

		}

		return finalResultList;

	}

	private List<LineItemVo> calculateFlatDiscountForFixedAmountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {
		List<LineItemVo> resultList = new LinkedList<>();

		if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.EachItem)) {
			resultList = calculateFlatDiscountForEachItem(benfitEntity, totalQuantityAndMrp, listofLineItems,
					promoEligibleLineItems);

		} else if (benfitEntity.getDiscountSubTypes().equals(DiscountSubTypes.AllItems)) {
			resultList = calculateFlatDiscountForAllItems(benfitEntity, totalQuantityAndMrp, listofLineItems,
					promoEligibleLineItems);

		}

		return resultList;
	}

	private List<LineItemVo> calculateFlatDiscountForEachItem(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {

		double calculatedInvoiceLevelDiscount = 0.0;

		for (LineItemVo lineItemVo : promoEligibleLineItems) {
			int quantity = lineItemVo.getQuantity();
			String discount = benfitEntity.getDiscount();
			calculatedInvoiceLevelDiscount = calculatedInvoiceLevelDiscount + (quantity * (Double.valueOf(discount)));

		}

		return distributeDiscountToAllProducts(listofLineItems, calculatedInvoiceLevelDiscount);

	}

	private List<LineItemVo> calculateFlatDiscountForAllItems(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {
		// double rupees = Double.valueOf(benfitEntity.getDiscount()).doubleValue();
		// int discountAmount= Integer.valueOf(benfitEntity.getDiscount()).intValue();
		// Double totalMrp = totalQuantityAndMrp.get(1);
		List<LineItemVo> results = distributeDiscountToAllProducts(listofLineItems,
				Double.valueOf(benfitEntity.getDiscount()).doubleValue());

		return results;
	}

	private List<LineItemVo> calcolateFlatDiscountForPercentageDiscountOn(BenfitEntity benfitEntity,
			List<Double> totalQuantityAndMrp, List<LineItemVo> listofLineItems,
			List<LineItemVo> promoEligibleLineItems) {

		double percentage = Double.valueOf(benfitEntity.getDiscount()).doubleValue();
		Double totalMrp = totalQuantityAndMrp.get(1);
		double calculatedInvoiceLevelDiscount = (percentage * totalMrp) / 100;
		List<LineItemVo> results = distributeDiscountToAllProducts(listofLineItems, calculatedInvoiceLevelDiscount);
		// System.out.println("Total discount : "+calculatedDiscountAmount);

		return results;
	}

	public List<LineItemVo> distributeDiscountToAllProducts(List<LineItemVo> listofLineItems,
			double calculatedInvoiceLevelDiscount) {

		double barcodeLevelTotalDiscount = 0.0;
		double totalMrp = 0.0;

		for (LineItemVo lineItemVo : listofLineItems) {

			barcodeLevelTotalDiscount = barcodeLevelTotalDiscount + lineItemVo.getDiscount().doubleValue();
			totalMrp = totalMrp + (lineItemVo.getItemPrice().doubleValue() * lineItemVo.getQuantity());

		}

		double totalDiscount = calculatedInvoiceLevelDiscount + barcodeLevelTotalDiscount;

		List<LineItemVo> resultList = new LinkedList<>();
		for (LineItemVo lineItemVo : listofLineItems) {

			Double individualLineItemTotalPrice = lineItemVo.getItemPrice().doubleValue() * lineItemVo.getQuantity();
			double discountPercentagePerItem = (100 * individualLineItemTotalPrice) / totalMrp;
			Double discountAmountForThisItem = (discountPercentagePerItem * totalDiscount) / 100;
			lineItemVo.setDiscount(discountAmountForThisItem.longValue());

			resultList.add(lineItemVo);
		}

		return resultList;
	}

}
