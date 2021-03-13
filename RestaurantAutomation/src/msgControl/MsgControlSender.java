package msgControl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MsgControlSender extends Thread {
	
	private Socket currSender;
	ConcurrentLinkedQueue<Msg> pendingMessages;
	private long empId;
	private char empPos;
	public MsgControlSender(Socket oneTablet, char empPos, long empId){
		pendingMessages = new ConcurrentLinkedQueue<Msg>();
		currSender = oneTablet;
		this.empPos = empPos;
		this.empId = empId;
	}
	public void run(){
		DataOutputStream out;
		try {
			out = new DataOutputStream(currSender.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Failed to start up sender for pos = "+empPos);
			return;
		}
		if(empPos=='w'){
			MsgControl.addWaiterSender(empId, this);
		}
		else if(empPos=='c'){
			MsgControl.addChefSender(empId, this);
		}
		else if(empPos=='h'){
			MsgControl.addHostSender(empId, this);
		}
		else if(empPos=='m'){
			MsgControl.addManagerSender(empId, this);
		}
		
		
		while(true){
			Msg m =pendingMessages.poll();
			if(m!=null){
				try {
					System.out.println("Sending: "+ m +" - to "+empPos +empId);
					out.writeUTF(m.toString());
				} catch (IOException e) {
					System.out.println("Messsage Controller for Pos = "+ empPos+" shutting down.");
					return;
				}
				
			}
		}
	}
}
