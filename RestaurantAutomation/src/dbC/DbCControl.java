package dbC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import config.Configure;
public class DbCControl {
	private static final String menuFile = "src/config/menu.txt";
	private final static int portNumber = Configure.getPortNumber("DbCControl");
	private static HashMap<String, Ingredient> inventory;
	private static HashMap<String, DishData> dishData;
	public static HashMap<Integer,Sender> waiterSenders;
	public static Sender chefSender;
	public static Menu menu;
	public static void main(String[] args){
		menu = new Menu();
		waiterSenders= new HashMap<Integer,Sender>();
		loadMenuFromFile();
		if(args!=null && args.length==10){//for testing
			return;
		}
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
               new Listener(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: DB C failed to start. Port " + portNumber+" is in use.");
            System.exit(-1);
        }	
	}
	public static int addDishtoMenu(String type, String dishname, double price){
		Dish newdish = new Dish(dishname,price,type);
		if(menu.menu.containsKey(type)){
			HashMap<String,Dish> tem = menu.menu.get(type);
			if(tem.containsKey(dishname)){
				return -1;
			}else{
				tem.put(dishname, newdish);     
                return 0;			
			}
		}else{
			HashMap<String,Dish> temp= new HashMap<String,Dish>();
			temp.put(dishname, newdish);
			menu.menu.put(type,temp);               
			return 0;
		}
	}
	
	public static void recordTicket(String tick){
		try (BufferedWriter br = new BufferedWriter(new FileWriter("TicketRecords.txt"))){
			String date = getDate();
			br.write(date+tick+"\n");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static String getDate() {
		return Calendar.MONTH +"/"+Calendar.DAY_OF_MONTH +"/"+ Calendar.YEAR+" - ";
	}
	public static void decrementDish(String dishName){
		DishData d =dishData.get(dishName);
		Iterator<String> ingIT = d.listOfIngredients.keySet().iterator();
		while(ingIT.hasNext()){
			String ingName = ingIT.next();
			Ingredient ing=inventory.get(ingName);
			ing.decrementAmountBy(d.amtOfIngredient.get(ingName));
			//Threshold met!!!!
			if(ing.checkThreshold()<=0){
				sendLowInventoryNotifications(ing);	
			}	
		}
	}
	public static void loadMenuFromFile(){
		
		try (BufferedReader br = new BufferedReader(new FileReader(menuFile))){

			String currLine;
			int i=0;
			while ((currLine = br.readLine()) != null) {
				if(i==0){//skip the header line
					i=1;
					continue;
				}
				String[] arr = currLine.split(",");
				addDishtoMenu(arr[0],arr[1], Double.parseDouble(arr[2]) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	public static void sendLowInventoryNotifications(Ingredient i ){
		if(chefSender!=null){
			chefSender.pendingMessages.offer("Low on Ingredient +"+i.name+".");
		}
		String dishes = "L";
		Iterator<String> it = dishData.keySet().iterator();
		while(it.hasNext()){
			DishData d = dishData.get(it.next());
			if(d.listOfIngredients.containsKey(i.name)){
				dishes = dishes+d.name+",";
			}
		}
		if(dishes.length()==1){
			return;
		}
		Iterator<Integer> itw = waiterSenders.keySet().iterator();
		while(itw.hasNext()){
			waiterSenders.get(itw.next()).pendingMessages.offer(dishes);
		}
	}
	public static boolean addIngredientToInventory(String ingredientName,Double amountLeft, String unitOfAmount, Double threshold ){
		if(inventory.containsKey(ingredientName)){
			return false;
		}
		if(threshold>=amountLeft || threshold<0){
			return false;
		}
		inventory.put(ingredientName, new Ingredient(ingredientName, amountLeft, unitOfAmount, threshold));
		return true;
	}
	
	public HashMap<String, Ingredient> getInventory(){
		return inventory;
	}
	
	public HashMap<String, DishData> getDishData(){
		return dishData;
	}
	
	public String getMenuFile(){
		return menuFile;
	}
	
	public int getPortNumber(){
		return portNumber;
	}
	
}
