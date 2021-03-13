package dbC;

import java.util.ArrayList;
public class Ticket {
	public int tableNumber;
	public String waiterName;
	public long waiterID;
	public char status;
	public ArrayList<Dish> listOfDishes;
	public long ticketNumber;
	public double price;
	public int amountOfDishesUnstarted;
	public int amountOfDishesStarted;
	public int amountOfDishesFinished;
	public int amountOfDishes;
	public boolean recentlySat;
	public boolean hotFood;
	public  boolean priority;

	public Ticket(String waiterName,int tableNum, long waiterID){
		this.waiterName = waiterName;
		this.tableNumber=tableNum;
		this.waiterID= waiterID;
		this.status='u';
		this.listOfDishes= new ArrayList<Dish>();
		amountOfDishes = 0;
		amountOfDishesUnstarted = 0;
		this.price=0;
		amountOfDishesStarted = 0;
		amountOfDishesFinished = 0;
	}
	public Ticket() {
		this.status='u';
		this.listOfDishes= new ArrayList<Dish>();
		amountOfDishes = 0;
		amountOfDishesUnstarted = 0;
		amountOfDishesStarted = 0;
		amountOfDishesFinished = 0;
	}
	public void addDishToTicket(Dish d){
		listOfDishes.add(d);
		amountOfDishesUnstarted= amountOfDishesUnstarted+1;
		amountOfDishes=amountOfDishes+1;
		this.price=this.price + d.price;
		updateStatusOfTicket();	
	}
	public boolean removeDishFromTicket(int indexOfDishInTickList){
		if(indexOfDishInTickList<0 || indexOfDishInTickList>=listOfDishes.size()){
			return false;
		}
		
		Dish del =listOfDishes.get(indexOfDishInTickList);
		if(del==null || del.sent){
			return false;
		}
		this.price=this.price - del.price;
		if(del.getStatus() =='c'){
			listOfDishes.remove(indexOfDishInTickList);
			return true;
		}
		amountOfDishes= amountOfDishes-1;
		char s = del.getStatus();
		if(s=='u'){
			amountOfDishesUnstarted= amountOfDishesUnstarted-1;
		}
		else if(s=='s'){
			amountOfDishesStarted= 	amountOfDishesStarted-1;
		}
		else{
			amountOfDishesFinished=amountOfDishesFinished-1;
		}
		listOfDishes.remove(indexOfDishInTickList);
		updateStatusOfTicket();
		return true;
	}
	public char updateStatusOfTicket(){
		char oldstatus = status;
		if(amountOfDishesUnstarted == amountOfDishes){
			status='u';
		}
		else if(amountOfDishesUnstarted>0){
			status='s';
		}
		else if(amountOfDishesFinished==amountOfDishes){
			status='f';
		}
		else{
			status='S';
		}
		return oldstatus;
	}
	public String toStringForDBC(){
		String stprice = ""+price;
		stprice = stprice.substring(0,stprice.indexOf('.')+3);
		String ans = "Waiter Name:"+waiterName+" Waiter ID:"+waiterID+" Table Number:"+tableNumber+" Price:$"+stprice;
		return ans;
	}
	public String toStringForChef(){
		String ans;
		if(priority){
			ans = "P:"+waiterName+":"+waiterID+":"+tableNumber+":";
		}
		else{
			ans = "N:"+waiterName+":"+waiterID+":"+tableNumber+":";
		}
		for(int i=0; i<listOfDishes.size();i++){
			Dish d = listOfDishes.get(i);
			//only send dishes that have not been sent
			if(!d.sent){
				ans = ans+";"+ d.name+"-";
				d.sent=true;
				if(d.comments!=null && d.comments.size()>0)
					for(int j =0; j< d.comments.size();j++){
						ans = ans+","+d.comments.get(j);
					}	
			}
		}
		return ans;
	}
	public static Ticket fromString(String tick){
		Ticket ans = new Ticket();
		String[] parts= tick.split(":");
		if(parts[0].charAt(0)=='P'){
			ans.priority = true;
		}
		ans.waiterName = parts[1];
		ans.waiterID = Integer.parseInt(parts[2]);
		ans.tableNumber = Integer.parseInt(parts[3]);
		String[] dishANDcomment = parts[4].split(";");
		for(int j=1; j<dishANDcomment.length; j++){
			String[] namelist = dishANDcomment[j].split("-");
			Dish d = new Dish(namelist[0],0,null);
			//add the name
			ans.listOfDishes.add(d);
			ans.amountOfDishes++;
			ans.amountOfDishesUnstarted++;
			if(namelist.length!=1){
				String[] eachComm = namelist[1].split(",");
				for(int c =1; c<eachComm.length; c++){
					d.comments.add(eachComm[c]);
				}
			}		
		}	
		return ans;
	}
}
