package dbB;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;
import config.Configure;

public class DbBControl extends Thread {
	private static final String tabNumFile = "src/configuration/tableNumbers.txt";
	private final static int portNumber = Configure.getPortNumber("DatabaseBController");
	public static Gson jsonConverter = new Gson();
	private Socket currListener;
	public static TableList listOfTables;
	public DbBControl(Socket listener) {
		currListener=listener;
	}

	public static boolean addTable(int tabNum, int maxOccupancy, char type){
		if(listOfTables.hm.containsKey(tabNum)){
			return false;
		}
		listOfTables.hm.put(tabNum, new Table(tabNum, maxOccupancy,type));
		return true;
	}
	
	public void run(){
		String mess = "";
		try(DataInputStream in = new DataInputStream(currListener.getInputStream());
				DataOutputStream out = new DataOutputStream(currListener.getOutputStream());) {
			mess=in.readUTF();
			char first = mess.charAt(0);
			if(first=='T'){ //send the host a list of tables
				System.out.println(jsonConverter.toJson(listOfTables));
				out.writeUTF(jsonConverter.toJson(listOfTables));
			}
			in.close();
			out.close();
			currListener.close();
		} catch (Exception e) {
			System.out.println("Before Error: Read in: "+ mess);
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args){
		listOfTables = new TableList();
		loadTablesFromFile();
		if(args!=null && args.length==10){//for testing
			return;
		}
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
               new DbBControl(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: DB B failed to start. Port " + portNumber+" is in use.");
            System.exit(-1);
        }	
	}
	
	public static void loadTablesFromFile(){
		
		try (BufferedReader br = new BufferedReader(new FileReader(tabNumFile))){

			String currLine;
			int i=0;
			while ((currLine = br.readLine()) != null) {
				if(i==0){//skip the header line
					i=1;
					continue;
				}
				String[] arr = currLine.split(",");
				addTable(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),arr[2].charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public String getTabNumFile(){
		return tabNumFile;
	}
	public TableList getListOfTables(){
		return listOfTables;
	}

	public int getPortNumber(){
		return portNumber;
	}
}
