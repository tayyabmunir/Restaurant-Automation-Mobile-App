package config;

import java.io.BufferedReader;
import java.io.FileReader;

public class Configure {

	private static final String portNumFile = "src/config/portNumbers.txt";
	private static final String domainNameFile = "src/config/domainNames.txt";
	
	public static int getPortNumber(String serverName){
		
		try (BufferedReader br = new BufferedReader(new FileReader(portNumFile))){

			String currLine;

			while ((currLine = br.readLine()) != null) {
				
				String[] arr = currLine.split("=");
				if(arr[0].equals(serverName.toLowerCase())){
					return Integer.parseInt(arr[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return -1;
	}
		
	public static String getDomainName(String serverName){
		
		try (BufferedReader br = new BufferedReader(new FileReader(domainNameFile))){

			String currLine;

			while ((currLine = br.readLine()) != null) {
				
				String[] arr = currLine.split("=");
				if(arr[0].equals(serverName.toLowerCase())){
					return arr[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
