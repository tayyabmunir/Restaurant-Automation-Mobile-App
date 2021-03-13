package login;
import java.awt.Dimension;
import javax.swing.JFrame;

import managerOperations.MngrI;


public class TabApp {
	
	public static void logOut(LoginPage logInPanel){
		logInPanel.logOut(logInPanel.currIDEntry);
		logInPanel.loggedIn='0';
		logInPanel.currIDEntry=-1;
		logInPanel.frame.setContentPane(logInPanel);
		logInPanel.frame.revalidate();
	}
	public static void login(LoginPage logInPanel){
		
		if(logInPanel.loggedIn=='m'){
			new MngrI(logInPanel);
		}	
	}
	public static void main(String[] args) {
		JFrame frame= new JFrame("SWE");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginPage logInPanel= new LoginPage(frame);
		frame.setContentPane(logInPanel);
		frame.pack();
		frame.setSize(new Dimension(1200,650));
	}
}

