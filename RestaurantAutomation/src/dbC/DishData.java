package dbC;

import java.util.HashMap;
public class DishData {
	String name;
	HashMap<String, Ingredient> listOfIngredients;
	HashMap<String, Double> amtOfIngredient;
	public double price;
	public DishData(String dishName){
		this.name = dishName;
		this.listOfIngredients= new HashMap<String, Ingredient>();
		this.amtOfIngredient = new HashMap<String, Double>();
	}
	public boolean addIngredient(Ingredient ingredientData){
		String ingredientName = ingredientData.name;
		if(listOfIngredients.get(ingredientName) ==null){
			listOfIngredients.put(ingredientName,ingredientData);
			return true;
		}
		return false;
	}
	public double getAmount(String ing){
		Double d= amtOfIngredient.get(ing);
		if(d==null){
			d=-1.0;
		}
		return d;
	}
}
