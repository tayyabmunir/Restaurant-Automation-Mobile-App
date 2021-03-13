package msgControl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import config.Configure;


public class MsgControl {
	
	private final static int portNumber = Configure.getPortNumber("MessageController");
	
	static HashMap<Long,MsgControlSender> waiterOut; 
	
	static HashMap<Long,MsgControlSender> hostOut;
	
	static HashMap<Long,MsgControlSender> chefOut;
	
	static HashMap<Long,MsgControlSender> managerOut;

	public static void addWaiterSender(long id, MsgControlSender sender){
		waiterOut.put(id,sender);
	}
	public static void addHostSender(long id,MsgControlSender sender){
		hostOut.put(id,sender);
	}
	public static void addChefSender(long id,MsgControlSender sender){
		chefOut.put(id,sender);
	}
	
	public static void addManagerSender(long id,MsgControlSender sender){
		managerOut.put(id,sender);
	}
	
	public static void removeWaiterSocket(long id){
		waiterOut.remove(id);
	}
	
	public static void removeHostSocket(long id){
		hostOut.remove(id);
	}
	public static void removeChefSocket(long id){
		chefOut.remove(id);
	}
	
	public static void removeManagerSocket(long id){
		managerOut.remove(id);
	}
	public static void main(String[] args){
		
		waiterOut= new HashMap<Long,MsgControlSender>();
		hostOut= new HashMap<Long,MsgControlSender>();
		chefOut= new HashMap<Long,MsgControlSender>();
		managerOut= new HashMap<Long,MsgControlSender>();
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
            	Socket oneTablet = serverSocket.accept();
				new MsgControlListener(oneTablet).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }	
	}
	public int getPortNumber(){
		return portNumber;
	}
	
	
}
