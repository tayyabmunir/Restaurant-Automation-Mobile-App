
package dbC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;
public class Listener  extends Thread{

	static int currID=0;
	private int socketid;
	private Socket currListener;
	public Gson jsonConverter;
	public Listener(Socket listener) {
		currListener=listener;
		jsonConverter = new Gson();
	}
	public void run(){
		String mess = "";
		try(DataInputStream in = new DataInputStream(currListener.getInputStream()); 
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream())) {
			mess =in.readUTF();
			char first = mess.charAt(0);
			if(first=='M'){
				currID++;
				socketid = currID;
				DbCControl.waiterSenders.put(currID,new Sender(out, 'w'));
				String jmenu = jsonConverter.toJson(DbCControl.menu);
				out.writeUTF(jmenu);
				while(true){
					mess =in.readUTF();
					if(mess.length()<3){
						mess = mess+ in.readUTF();
					}
					first = mess.charAt(0);
					if(first == 'T'){
						String tick = mess.substring(1);
						DbCControl.recordTicket(tick);
					}
				}
			}
			else{
				//set up chef interface
				DbCControl.chefSender = new Sender(out, 'c');
				socketid = -1;
				while(true){
					mess =in.readUTF();
					if(mess.length()<3){
						mess = mess+ in.readUTF();
					}
					first = mess.charAt(0);
					
					if(first=='A'){ 
					}
					else if(first=='R'){
						
					}
					else if(first =='D'){
						DbCControl.decrementDish(mess.substring(1));
					}
				}
				
				
			}
			
			
		} catch (Exception e) {
			try {
				//NEED A WAY TO REMOVE HIM FROM THE LIST!!!!!!!!!!!!
				if(socketid==-1){
					DbCControl.chefSender=null;
				}
				else{
					DbCControl.waiterSenders.remove(currID);
				}
				currListener.close();
			} catch (IOException e1) {			}
			System.out.println("DBC Listener Closing: Before closing Read in: "+ mess);
			e.printStackTrace();
		} 
	}
	
	
}
