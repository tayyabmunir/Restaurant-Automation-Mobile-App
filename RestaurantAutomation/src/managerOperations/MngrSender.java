package managerOperations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import msgControl.Msg;

public class MngrSender extends Thread {

	private long empID;
	private Socket sock;
	ConcurrentLinkedQueue<Msg> pendingMessages;
	
	public MngrSender(Socket listener, long empID) {
		sock=listener;
		this.empID=empID;
		pendingMessages = new ConcurrentLinkedQueue<Msg>();
	}

	public void sendMessage(Msg m){
		m.senderPosition='m';
		m.senderEmpID=empID;
		pendingMessages.offer(m);
	}
	
	public void run(){
		DataOutputStream out;
		try {
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Failed to start up sender for Manager.");
			return;
		}
		
		
		while(true){
			Msg m =pendingMessages.poll();
			if(m!=null){
				try {
					System.out.println("Manager sending message:"+m);
					
					out.writeUTF(m.toString());
					
				} catch (IOException e) {
					System.out.println("Manager Messsage Sender shutting down.");
					return;
				}
				
			}
		}
	}
}
