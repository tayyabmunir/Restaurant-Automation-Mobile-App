
package dbC;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Sender extends Thread {
	
	private DataOutputStream out;
	ConcurrentLinkedQueue<String> pendingMessages;
	private char empPos;
	public Sender(DataOutputStream oneTablet, char empPos){
		pendingMessages = new ConcurrentLinkedQueue<String>();
		out = oneTablet;
		this.empPos = empPos;
	}
	
	public void run(){
		while(true){
			String m =pendingMessages.poll();
			if(m!=null){
				try {
					out.writeUTF(m);
				} catch (IOException e) {
					System.out.println("DBC Messsage Sender for Pos = "+ empPos+" shutting down.");
					return;
				}
				
			}
		}
	}
}
