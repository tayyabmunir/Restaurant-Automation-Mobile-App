package dbA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import config.Configure;

public class DbAControl extends Thread {
	
	private static final String empFile = "src/config/employeeInfo.txt";
	private final static int portNumber = Configure.getPortNumber("DbAControl");
	private Socket currListener;
	private static long currentID;
	private static ArrayList<Employee> employeeList;
	
	public DbAControl(Socket listener) {
		currListener=listener;
	}
	public static boolean addEmployee(String name, char position){
		char[] possiblePositions = {'w','h','c','m','o'};
		if(name==null){
			return false;
		}
		for(int i =0;i<possiblePositions.length;i++){
			if(position == possiblePositions[i]){
				employeeList.add(new Employee(name, currentID, position));
				employeeList.get((int) currentID).loggedIn=false;
				currentID++;
				return true;
			}
		}
		
		return false;
	}
	public void run(){

		try(DataInputStream in = new DataInputStream(currListener.getInputStream());
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream())) {
			
				String mess =in.readUTF();
				char first = mess.charAt(0);
				if(first=='L'){
					String num = mess.substring(2);
					int number = Integer.parseInt(num);
					if(number>=employeeList.size() || number<0){
						System.out.println("0");
						out.writeUTF("0");
					}
					else{
						Employee curE = employeeList.get(number);
						String ans = curE.position + curE.name;
						if(curE.loggedIn){
							System.out.println("L");
							out.writeUTF("L");
						}
						else{
							curE.loggedIn=true;
							System.out.println(ans);
							out.writeUTF(ans);
						}
					}
				}
				else if(first=='O'){ 
					String num = mess.substring(2);
					int number = Integer.parseInt(num);
					if(number<employeeList.size() && number>=0){
						Employee curE = employeeList.get(number);
						curE.loggedIn=false;
					}
				}
				else if(first =='W'){
					out.writeUTF(sendWaiters());
				}
				currListener.close();
			}catch (Exception e) {
				e.printStackTrace();
			} 
	}
	
	private String sendWaiters() {
		String ans ="";
		for(int i=0; i< employeeList.size(); i++){
			Employee e = employeeList.get(i);
			if(e.position=='w'){
				ans = ans+i+","+e.name+":";
			}
		}
		return ans;
		
	}

	public static void main(String[] args){
		currentID=0;
		employeeList = new ArrayList<Employee>();
		loadEmployees();
		
		if(args!=null && args.length==10){
			return;
		}
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
			System.out.println("Actual name="+serverSocket.getInetAddress());
			while (true) {
               new DbAControl(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: DB A failed to start. Port " + portNumber+" is in use.");
            System.exit(-1);
        }
		
	}
	public static void loadEmployees(){
		try (BufferedReader br = new BufferedReader(new FileReader(empFile))){

			String currLine;
			int i=0;
			while ((currLine = br.readLine()) != null) {
				if(i==0){//skip the header line
					i=1;
					continue;
				}
				String[] arr = currLine.split(",");
				addEmployee(arr[0],arr[1].charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static ArrayList<Employee> getEmployeeListForTesting(){
		return employeeList;
	}
	public long getCurrentID(){
		return currentID;
	}
	
	public ArrayList<Employee> getEmployeeList(){
		return employeeList;
	}
	public Socket getCurrListener(){
		return currListener;
	}
	public String getEmpFile(){
		return empFile;
	}
	public int getPortNumber(){
		return portNumber;
	}
}
