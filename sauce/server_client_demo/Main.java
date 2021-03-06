package server_client_demo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

public class Main {
	public static void main(String args[]){
		Date d = new Date();
		System.out.println(Calendar.getInstance().getTime());
		
		try {
		    InetAddress addr = InetAddress.getLocalHost();

		    // Get IP Address
		    byte[] ipAddr = addr.getAddress();

		    // Get hostname
		    String hostname = addr.getHostName();
		    

		    System.out.println(addr.getHostAddress());
		    
		    System.out.println(hostname + " " + ipAddr);
		} catch (UnknownHostException e) {
		}
		
		try {
		    // Get hostname by textual representation of IP address
		    InetAddress addr = InetAddress.getByName("127.0.0.1");

		    // Get hostname by a byte array containing the IP address
		    byte[] ipAddr = new byte[]{127, 0, 0, 1};
		    addr = InetAddress.getByAddress(ipAddr);

		    // Get the host name
		    String hostname = addr.getHostName();

		    // Get canonical host name
		    String hostnameCanonical = addr.getCanonicalHostName();
		    
		    System.out.println(hostname + " " + hostnameCanonical);
		} catch (UnknownHostException e) {
		}
	}
}
