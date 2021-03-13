package dbC;
import java.util.ArrayList;
public class Dish {
	public boolean sent;
	public String name;
	public double price;
	private char status;
	public String typeOfDish;
	public ArrayList<String> comments;
	public Dish(String name, double price, String typeOfDish){
		status='u';
		this.name=name;
		this.price = price;
		this.typeOfDish=typeOfDish;
		comments = new ArrayList<String>();
	}
	public Dish makeCopyOfDish(){
		return new Dish(this.name, this.price, this.typeOfDish);
	}
	public int changeStatus(char newStatus){
		if(newStatus=='c'){
			status = newStatus;
			return 0;
		}
		if(status == 'u' && newStatus=='s'){
			status = newStatus;
			return 0;
		}
		if(status == 's' && newStatus=='u'){
			status = newStatus;
			//increment each ingredient
			return 0;
		}
		if(status == 's' && newStatus=='f'){
			status = newStatus;
			return 0;
		}
		
		return -2;
	}
	public char getStatus(){
		return status;
	}
}
