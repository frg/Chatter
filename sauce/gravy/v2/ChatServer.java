package gravy.v2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {
	ArrayList<OutputStream> clientOutputStreams;
	OutputStream sockOut;
	
	public static void main(String args[]) {
		// Find server ip
		try {
			InetAddress ipAddress = InetAddress.getLocalHost();
			System.out.println(ipAddress);
		} catch (UnknownHostException uhEx) {
			uhEx.printStackTrace();
		}
		
		new ChatServer().start();
	}
	
	public void start() {
		clientOutputStreams = new ArrayList();
		Message msg = null;
		
		try {
			ServerSocket serverSock = new ServerSocket(Values.port);
			
			// Loops in order to find new requesting client connections
			while(true) {
				System.out.println("Waiting..");
				Socket clientSocket = serverSock.accept();
				// New implementation start
//				ObjectInputStream serverIn = new ObjectInputStream(clientSocket.getInputStream());
//				ObjectOutputStream serverOut = new ObjectOutputStream(clientSocket.getOutputStream());
//				
//				msg = (Message) serverIn.readObject();
//				System.out.println("read: " + msg.toString());
//				
//				serverOut.writeObject(msg);
//				
//				serverIn.close();
//				serverOut.close();
				// New implementation end
				
				sockOut = clientSocket.getOutputStream();
				// Saves new client to arraylist
				clientOutputStreams.add(sockOut);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public class ClientHandler implements Runnable {
		Socket sock;
		ObjectInputStream ois;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				ois = new ObjectInputStream(sock.getInputStream());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	
		public void run() {
			Message msg;
			while ((msg = readMessage()).getMessage() != null) {
				tellEveryone(msg.getMessage());
			}
		}
		
		public Message readMessage(){
			Message message = new Message("Message read failed.");
			
			try {
				message =  (Message)ois.readObject();
				System.out.println("received: " + message.toString());
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			} catch (ClassNotFoundException cnfEx) {
				cnfEx.printStackTrace();
			}
			
			return message;
		}
	}
	
	public void tellEveryone(String sayThis) {
		Iterator it = clientOutputStreams.iterator();
		
		// Loop through clients and send message
		while(it.hasNext()) {
			sendMessage((OutputStream)it.next(), sayThis);
		}
	}
	
	public void sendMessage(OutputStream recipient, String sendThis) {
		// Writes message to message objects and attaches data
		Message message = new Message(sendThis);
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			oos.close();
		
			byte[] bytes = baos.toByteArray();
			recipient.write(bytes);
			System.out.println("Sent: " + message.toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
