
package dbC;
public class Ingredient {
	Double amountLeftInInventory;
	String unit;
	String name;
	Double threshold;
	public Ingredient(String ingredientName,Double amountLeft, String unitOfAmount, Double threshold ){
		amountLeftInInventory=amountLeft;
		unit=unitOfAmount;
		name = ingredientName;
		this.threshold=threshold;
	}
	public Double checkThreshold(){
		if(amountLeftInInventory==null || threshold==null){
			return null;
		}
		return amountLeftInInventory - threshold;
	}
	public Double decrementAmountBy(Double amount){
		if(amount==null){
			return null;
		}
		amountLeftInInventory = amountLeftInInventory - amount;
		return amountLeftInInventory;
	}	
}
