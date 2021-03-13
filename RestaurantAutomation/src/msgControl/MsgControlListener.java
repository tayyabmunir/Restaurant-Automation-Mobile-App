
package msgControl;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

public class MsgControlListener  extends Thread{
	
	private Socket currListener;
	private long empId;
	private char empPos;
	public MsgControlListener(Socket listener) {
		currListener=listener;
	}
	public void run(){
		try {
			DataInputStream in = new DataInputStream(currListener.getInputStream());
			String message =in.readUTF();
			if(message.length()==2){
				String second = in.readUTF();
				message = message+second;
			}
			System.out.println("Log in message : "+message);
			Msg m = Msg.fromString(message);
			char pos = m.receiverPosition;
			if(pos=='L'){//logging in
				empPos = m.senderPosition;
				empId = m.senderEmpID;
				if(empPos=='w'|| empPos=='c' || empPos=='h'|| empPos=='m'){
					new MsgControlSender(currListener, empPos, empId).start();
				}
				else{
					System.out.println("Position "+empPos+" is not recognized.");
					currListener.close();
					return;
				}
			}else{//issue everyone needs to first log in before using MC
				pos = m.senderPosition;
				System.out.println("Position "+pos+" tried to use MC w/o log in");
				currListener.close();
				return;
			}
			
			//listen to messages until a log out
			while(true){
				message =in.readUTF();
				if(message.length()==2){
					String second = in.readUTF();
					message = message+second;
				}
				System.out.println("Received : "+message);
				m = Msg.fromString(message);
				pos = m.receiverPosition;
				if(pos=='X'){//logging out
					if(empPos=='w'){
						MsgControl.removeWaiterSocket(empId);
					}
					else if(empPos=='c'){
						MsgControl.removeChefSocket(empId);
					}
					else if(empPos=='h'){
						MsgControl.removeHostSocket(empId);
					}
					else if(empPos=='m'){
						MsgControl.removeManagerSocket(empId);
					}
					currListener.close();
					return;
				}
				else{
					MsgControlSender sender;
					if(pos=='w'){
						if(MsgControl.waiterOut.isEmpty()){ //if no waiter logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							Iterator<Long> it = MsgControl.waiterOut.keySet().iterator();
							while(it.hasNext()){
								sender = MsgControl.waiterOut.get(it.next());
								System.out.println("Adding message:"+m+" -To "+pos);
								sender.pendingMessages.offer(m);
							}
							continue;
						}
						sender = MsgControl.waiterOut.get(m.receiverEmpID);
					}
					else if(pos=='c'){
						if(MsgControl.chefOut.isEmpty()){ 
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MsgControl.chefOut.keySet().iterator().next();
						}
						sender = MsgControl.chefOut.get(m.receiverEmpID);
					}
					else if(pos=='h'){
						if(MsgControl.hostOut.isEmpty()){ 
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MsgControl.hostOut.keySet().iterator().next();
						}
						sender = MsgControl.hostOut.get(m.receiverEmpID);
					}
					else if(pos=='m'){
						if(MsgControl.managerOut.isEmpty()){ //if no manager logged in
							continue;
						}
						if(m.receiverEmpID==-1){
							m.receiverEmpID = MsgControl.managerOut.keySet().iterator().next();
						}
						sender = MsgControl.managerOut.get(m.receiverEmpID);
					}
					else{//error with this message position
						System.out.println("Error message did not have valid receiver.");
						continue;
					}
					//Add the forwarded message to message controller
					System.out.println("Adding message:"+m+" -To "+pos);
					sender.pendingMessages.offer(m);
				}
			}	
		}catch (Exception e) {
			System.out.println("Removing "+empPos+empId+" from MC");
			if(empPos=='w'){
				MsgControl.removeWaiterSocket(empId);
			}
			else if(empPos=='c'){
				MsgControl.removeChefSocket(empId);
			}
			else if(empPos=='h'){
				MsgControl.removeHostSocket(empId);
			}
			else if(empPos=='m'){
				MsgControl.removeManagerSocket(empId);
			}
			try {
				currListener.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
		
	}
}
