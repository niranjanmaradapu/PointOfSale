package com.otsi.retail.promotions.calculate.benefits;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountSubTypes;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.CalculatedDiscountsVo;
import com.otsi.retail.promotions.vo.ProductTextileVo;

@Component
public class CalculateBenifits {

	
	
	public CalculatedDiscountsVo calculate(List<BenefitVo> benifitVos,ProductTextileVo productTextileVo) {
		
		//Loop through benifitVos
		CalculatedDiscountsVo calculatedDiscountsVo=new CalculatedDiscountsVo();
		calculatedDiscountsVo.setDiscountAvailable(true);
		for (BenefitVo benifitVo : benifitVos) {
			//if benifit type is flat discount call calculateBenifit for flat discount
			
			
			if(benifitVo.getBenfitType().equals(BenfitType.FlatDiscount)) {
				calculatedDiscountsVo.setCalculatedDiscount(calculateBeniftForFlatDIscount(benifitVo,productTextileVo));
			}
			else if(benifitVo.getBenfitType().equals(BenfitType.XunitsFromBuyPool)) {
				calculatedDiscountsVo.setCalculatedDiscountDetails(calculateBenifitsForXUnitsFromBuyPool(benifitVo,productTextileVo));
				
			}
			else if(benifitVo.getBenfitType().equals(BenfitType.XunitsFromGetPool)) {
				calculatedDiscountsVo.setCalculatedDiscountDetails(calculateBenifitsForXUnitsFromGetPool(benifitVo,productTextileVo));
				
			}	
		}
		return calculatedDiscountsVo;	
	}

	
	private String calculateBeniftForFlatDIscount(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		
		String calculatedDiscountAmount= "";
		
        if(benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			calculatedDiscountAmount=calculateBeniftForPercentageDiscountOn(benifitVo,productTextileVo);
		}
		else if(benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			calculatedDiscountAmount=calculateBeniftForRupeesDiscountOn(benifitVo,productTextileVo);
		}
		else if(benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			//calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			calculatedDiscountAmount=benifitVo.getDiscount().toString();
		}
		return calculatedDiscountAmount;
	}

	private String calculateBeniftForPercentageDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
	     
		String calculatedDiscountAmount="";
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
		if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)){
	          int discountPercentage= Integer.valueOf(benifitVo.getDiscount()).intValue();
	         int purchasedQuantity= productTextileVo.getQty();
	        float valueOfMRP= productTextileVo.getItemMrp();
	         calculatedDiscountAmount= (discountPercentage/100)*(purchasedQuantity*valueOfMRP) +"";
	         
		}
		return calculatedDiscountAmount;
		
	}
	
	private String calculateBeniftForRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		String calculatedDiscountAmount="";
		if(/*benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP) ||*/ benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)){
	          int discountAmount= Integer.valueOf(benifitVo.getDiscount()).intValue();
	         int purchasedQuantity= productTextileVo.getQty();
	         calculatedDiscountAmount= discountAmount*purchasedQuantity +"";
	         
		}
			
		return calculatedDiscountAmount;
	}
	
	/*
	private double calculateBeniftForFixedAmountOn(BenifitVo benifitVo, BarcodeTextileVo barcodeTextileVo) {
		double calculatedDiscountAmount=0.0;
		if(benifitVo.getEachItem().equalsIgnoreCase("eachItem") || benifitVo.getEachItem().equalsIgnoreCase("itemRSP") 
				|| benifitVo.getEachItem().equalsIgnoreCase("allItems") ){
	          int discountAmount= benifitVo.getDiscountOn();
	         int purchasedQuantity= barcodeTextileVo.getQuantity();
	        int valueOfRSP= barcodeTextileVo.getRSP();
	         calculatedDiscountAmount= (discountAmount)-(purchasedQuantity*valueOfRSP);
	         
		}
			else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){
		          int discountAmount= benifitVo.getDiscountOn();
		         int purchasedQuantity= barcodeTextileVo.getQuantity();
		        int valueOfMRP= barcodeTextileVo.getMRP();
		         calculatedDiscountAmount= (discountAmount)-(purchasedQuantity*valueOfMRP);
		         
		}
		
		return calculatedDiscountAmount;
	}
	*/

	private String calculateBenifitsForXUnitsFromBuyPool(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		
		String discountDetails= new String();
		
		if(benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			//calculatedDiscountAmount=calculateBeniftForPercentageDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails=calculateBenifitsForXUBAndPercentageDiscountOn(benifitVo,productTextileVo);
		}
		else if(benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			//calculatedDiscountAmount=calculateBeniftForRupeesDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails=calculateBenifitsForXUBAndRupeesDiscountOn(benifitVo,productTextileVo);

		}
		else if(benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			//calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			//calculatedDiscountAmount=benifitVo.getDiscountOn();
			discountDetails=calculateBenifitsForXUBAndFixedAmountOn(benifitVo,productTextileVo);

			
		}
	return discountDetails.toString();
			
	}


	
	
	private String calculateBenifitsForXUBAndPercentageDiscountOn(BenefitVo benifitVo,
			ProductTextileVo productTextileVo) {
		
		StringBuilder discountDiscription =new StringBuilder();
        discountDiscription.append("Discount will be given on probucts of the related Buy pools. Quantity should be greater than or equal to ");
        discountDiscription.append(benifitVo.getNumOfItemsFromBuyPool()+". ");
        discountDiscription.append("The percentage of discount is "+benifitVo.getDiscount() +" ");
              
        if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinValuedOnEachItemDiscountDiscription(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaxValuedOnEachItemDiscountDiscription(benifitVo));

		}
		return discountDiscription.toString();

	}
	
	
    private String calculateBenifitsForXUBAndRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		
		StringBuilder discountDiscription =new StringBuilder();
        discountDiscription.append("Discount will be given on probucts of the related Buy pools.");
        discountDiscription.append("The Amount of discount is "+benifitVo.getDiscount() +" ");

       
		 if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinimumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaximumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));

		}
			return discountDiscription.toString();

	}
	
	

   private String calculateBenifitsForXUBAndFixedAmountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		
		StringBuilder discountDiscription =new StringBuilder();
        discountDiscription.append("Discount will be given on probucts of the related Buy pools.");
       // discountDiscription.append("The Fixed-Amount of discount is "+benifitVo.getDiscountOn() +" ");

        if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMinValued(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMaxValued(benifitVo));

		}
   return discountDiscription.toString();
	}

   private String calculateBenifitsForXUnitsFromGetPool(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		String discountDetails= new String();


		if(benifitVo.getDiscountType().equals(DiscountType.PercentageDiscountOn)) {
			//calculatedDiscountAmount=calculateBeniftForPercentageDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails=calculateBenifitsForXUGAndPercentageDiscountOn(benifitVo,productTextileVo);
		}
		else if(benifitVo.getDiscountType().equals(DiscountType.RupeesDiscountOn)) {
			//calculatedDiscountAmount=calculateBeniftForRupeesDiscountOn(benifitVo,barcodeTextileVo);
			discountDetails=calculateBenifitsForXUGAndRupeesDiscountOn(benifitVo,productTextileVo);

		}
		else if(benifitVo.getDiscountType().equals(DiscountType.FixedAmountOn)) {
			//calculateBeniftForFixedAmountOn(benifitVo,barcodeTextileVo);
			//calculatedDiscountAmount=benifitVo.getDiscountOn();
			discountDetails=calculateBenifitsForXUGAndFixedAmountOn(benifitVo,productTextileVo);

			
		}
		return discountDetails.toString();
	}

	private String calculateBenifitsForXUGAndPercentageDiscountOn(BenefitVo benifitVo,
			ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription =new StringBuilder();
        discountDiscription.append("Discount will be given on probucts of the related Get pools. Quantity should be greater than or equal to ");
        discountDiscription.append(benifitVo.getNumOfItemsFromGetPool()+". ");
        discountDiscription.append("The percentage of discount is "+benifitVo.getDiscount() +" ");

        if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinValuedOnEachItemDiscountDiscription(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaxValuedOnEachItemDiscountDiscription(benifitVo));

		}
		return discountDiscription.toString();

	}
	
	private String calculateBenifitsForXUGAndRupeesDiscountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription =new StringBuilder();
        discountDiscription.append("Discount will be given on probucts of the related Get pools.");
        discountDiscription.append("The Amount of discount is "+benifitVo.getDiscount() +".");
        discountDiscription.append("Discount Amount is on the following Get Pool"/*+benifitVo.getPoolName()+" " */);

       
		 if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getMinimumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getMaximumValuedOnEachItemDiscountDiscriptionInRupees(benifitVo));

		}
		return discountDiscription.toString();
	}
	
	private String calculateBenifitsForXUGAndFixedAmountOn(BenefitVo benifitVo, ProductTextileVo productTextileVo) {
		StringBuilder discountDiscription =new StringBuilder();
        
        discountDiscription.append("Discount will be given on probucts of the related Get pools.");
        discountDiscription.append("and Discount will be given on the following probucts."/*+benifitVo.getPoolName()*/);

       // discountDiscription.append("The Fixed-Amount of discount is "+benifitVo.getDiscountOn() +" ");

        if(benifitVo.getItemValue().equals(ItemValue.MinValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMinValued(benifitVo));
		}
		else if(benifitVo.getItemValue().equals(ItemValue.MaxValue)) {
			discountDiscription.append(getDiscountDiscriptionForFixedAmountOnAndMaxValued(benifitVo));

		}
   return discountDiscription.toString();
		
	}
	
   /* private String getEachItemDiscountDiscription(BenifitVo benifitVo) {
		
		StringBuilder eachItemDiscountDiscription =new StringBuilder();
		
		if( benifitVo.getEachItem().equalsIgnoreCase("itemRSP")  ){
			eachItemDiscountDiscription.append("on the RSP of each Quantity.");
	         
		}
		else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){
				eachItemDiscountDiscription.append("on the MRP of each Quantity.");


		}
		return eachItemDiscountDiscription.toString();
	}
    */
    
    
    private String getMaxValuedOnEachItemDiscountDiscription(BenefitVo benifitVo) {
    	
        StringBuilder eachItemDiscountDiscription =new StringBuilder();
		
//		if( benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the Maximum RSP of each Quantity.");
//	         
//		}
//		else
        if(benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemMRP)){
				eachItemDiscountDiscription.append("on the Maximum MRP of each Quantity.");


		}
		return eachItemDiscountDiscription.toString();
	}
    

	private String getMinValuedOnEachItemDiscountDiscription(BenefitVo benifitVo) {
      
		StringBuilder eachItemDiscountDiscription =new StringBuilder();
		
//		if( benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the Minimum RSP of each Quantity.");
//	         
//		}
//		else 
		
		if(benifitVo.getDiscountSubType().equals( DiscountSubTypes.ItemMRP)){
				eachItemDiscountDiscription.append("on the Minimum MRP of each Quantity.");


		}
		return eachItemDiscountDiscription.toString();
	}
	
	
	/*private String getEachItemDiscountDiscriptionInRupees(BenifitVo benifitVo) {
		
        StringBuilder eachItemDiscountDiscription =new StringBuilder();
		
		if( benifitVo.getEachItem().equalsIgnoreCase("itemRSP")  ){
			eachItemDiscountDiscription.append("on the Minimum RSP of each Quantity.");
	         
		}
		else if(benifitVo.getEachItem().equalsIgnoreCase("itemMRP")){
				eachItemDiscountDiscription.append("on the Minimum MRP of each Quantity.");


		}
		return eachItemDiscountDiscription.toString();
	}
   */	
	
	private String getMaximumValuedOnEachItemDiscountDiscriptionInRupees(BenefitVo benifitVo) {
		
		 StringBuilder eachItemDiscountDiscription =new StringBuilder();
			
//			if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP)  ){
//				eachItemDiscountDiscription.append("on the Maximum RSP of each Quantity.");
//		         
//			}
//			else
		   if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)){
					eachItemDiscountDiscription.append("on the Maximum MRP of each Quantity.");
			}
			eachItemDiscountDiscription.append(" The maximum number of products that should be bought is "+benifitVo.getNumOfItemsFromBuyPool()+".");

			return eachItemDiscountDiscription.toString();
	}

	private String getMinimumValuedOnEachItemDiscountDiscriptionInRupees(BenefitVo benifitVo) {
		
       StringBuilder eachItemDiscountDiscription =new StringBuilder();
				
//		if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemRSP)  ){
//			eachItemDiscountDiscription.append("on the RSP of each Quantity.");
//	         
//		}
//		else
       if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.ItemMRP)){
				eachItemDiscountDiscription.append("on the MRP of each Quantity.");


		}
		eachItemDiscountDiscription.append(" The minimum number of products that should be bought is "
		+benifitVo.getNumOfItemsFromBuyPool()+".");
		
		return eachItemDiscountDiscription.toString();
	}
	
	
	private Object getDiscountDiscriptionForFixedAmountOnAndMaxValued(BenefitVo benifitVo) {
		
		StringBuilder discountDiscription =new StringBuilder();
	       discountDiscription.append("The customer should buy a maximum of "+benifitVo.getNumOfItemsFromGetPool()+" Units.//n");
	       if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.EachItem)) {
	           discountDiscription.append("If customer buys more than the specified Maximum number of quantity, every item will get the discount. ");
				discountDiscription.append("The discount will be given on each item and the discount amount per item is "
			+benifitVo.getDiscount());
				
			}
			else if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.AllItems)) {
				discountDiscription.append("The discount amount will be "+benifitVo.getDiscount()+".");

			}
			return discountDiscription.toString();
	}

	private String getDiscountDiscriptionForFixedAmountOnAndMinValued(BenefitVo benifitVo) {
		
		StringBuilder discountDiscription =new StringBuilder();
       discountDiscription.append("The customer should buy a minimum of "+benifitVo.getNumOfItemsFromGetPool()+" Units.//n");
       if(benifitVo.getDiscountSubType().equals(DiscountSubTypes.EachItem)) {
           discountDiscription.append("If customer buys more than the specified Minimum number of quantity, every item will get the discount. ");
			discountDiscription.append("The discount will be given on each item and the discount amount per item is "
		+benifitVo.getDiscount());
			
		}
		else if( benifitVo.getDiscountSubType().equals(DiscountSubTypes.AllItems)) {
			discountDiscription.append("The discount amount will be "+benifitVo.getDiscount()+".");

		}
		return discountDiscription.toString();
	}

}
