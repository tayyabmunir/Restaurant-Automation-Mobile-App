
package managerOperations;

import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JFrame;

import config.Configure;
import login.LoginPage;
import login.TabApp;
import msgControl.Msg;

public class MngrI {
	
	private final static String MCdomainName = Configure.getDomainName("MsgControl");
	private final static int MCportNumber = Configure.getPortNumber("MsgControl");

	public LinkedList<Msg> listOfMessages;
	public MngrSender sender;
	
	private JFrame frame;	
	long empID;
	String name;
	MngrPage manScreen;
	LoginPage loginPanel;


	public MngrI(LoginPage lp){
		loginPanel=lp;
		name=lp.empName;
		empID = lp.currIDEntry;
		listOfMessages = new LinkedList<Msg>();
		this.frame=lp.frame;
		setUpMessageController();
		manScreen = new MngrPage(this);
		this.frame.setContentPane(manScreen);
		frame.revalidate();
	}
	
	
	public void updateScreen() {
		manScreen.updateScreen();
		frame.revalidate();
	}
	private void setUpMessageController() {
		Socket listener;
		try {
			listener = new Socket(MCdomainName, MCportNumber);
			Thread t= new MngrListener(listener, this);
			t.start();
			sender = new MngrSender(listener,empID);
			sender.start();
			sender.sendMessage(new Msg('L',-1, "Signing in"));
			
		} catch (Exception e) {
			System.out.println("Problem setting up Manager MC.");
		}
		
	}
	
	public void sendMassNotification(String content){
		sender.sendMessage(new Msg('h',-1, content));
		sender.sendMessage(new Msg('c',-1, content));
		sender.sendMessage(new Msg('w',-1, content));
	}

	public void deleteMessage(int index){
		listOfMessages.remove(index);
		updateScreen();
	}
	
	public void addMessageToList(Msg m) {
		if(m==null){
			return;
		}
		listOfMessages.add(m);
		updateScreen();
		
	}
	
	public void generateMessages(){
		
		Msg m =new Msg();
		m.content= "At table6, the waiter needs assistance";
		addMessageToList(m);
		Msg m1 =new Msg();
		m1.content= "At the host stand, host needs assistance.";
		addMessageToList(m1);
	}
	
	public int getMCPortNumber(){
		return MCportNumber;
	}
	
	public long getEmpID(){
		return empID;
	}
	
	public void logOut(){
		if(sender!=null)
		sender.sendMessage(new Msg('X',-1, "Signing out"));
		TabApp.logOut(loginPanel);
	}
}
