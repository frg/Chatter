package server_client_demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	
	public static void main(String args[]){
		Client client = new Client();
		
		client.start();
	}
	
	public void start(){
		try {
			InetAddress ipAddress = InetAddress.getLocalHost();
			
			Socket sock = new Socket(ipAddress.getHostAddress(), 5321);
			
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			String listener = reader.readLine();
			System.out.println(listener);
			
			reader.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
