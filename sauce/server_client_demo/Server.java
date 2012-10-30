package server_client_demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	String broadcast = "Gravity is a lie.";
	
	public static void main(String args[]) {
		Server server = new Server();
		server.startServing();
	}

	public void startServing(){
		try {
			ServerSocket serverSock = new ServerSocket(5359);
			
			while(true) {
				Socket sock = serverSock.accept();
				
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				writer.println(broadcast);
				writer.close();
				System.out.println(broadcast);
			}
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
