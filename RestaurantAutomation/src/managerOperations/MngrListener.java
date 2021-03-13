package managerOperations;

import java.io.DataInputStream;
import java.net.Socket;
import msgControl.Msg;

public class MngrListener extends Thread {
	private Socket sock;
	
	private MngrI mi;

	public MngrListener(Socket listener, MngrI wI) {
		sock=listener;
		mi=wI;
	}
	
	public void run(){
		DataInputStream in = null;
		try {
			in = new DataInputStream(sock.getInputStream());
			while(true){
				String mes = in.readUTF();
				if(mes.length()==2){
					String second = in.readUTF();
					mes = mes +second;
				}
				Msg m = Msg.fromString(mes);
				mi.addMessageToList(m);
			}		
			
		}catch (Exception e) {
			System.out.println("Manager Message Listener disconnected from MC.");
		} 
		
	}

	
}
