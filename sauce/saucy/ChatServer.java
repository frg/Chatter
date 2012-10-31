package saucy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {
	ArrayList clientOutputStreams;
	
	public static void main(String args[]) {
		// Find server ip
		try {
			InetAddress ipAddress = InetAddress.getLocalHost();
			System.out.println(ipAddress.getHostAddress());
		} catch (UnknownHostException uhEx) {
			uhEx.printStackTrace();
		}
		
		new ChatServer().go();
	}
	
	public void go() {
		clientOutputStreams = new ArrayList();
		
		try {
			ServerSocket serverSock = new ServerSocket(5100);
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("Established new connection");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			String message;
			
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read: " + message);
					tellEveryone(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();
		
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
