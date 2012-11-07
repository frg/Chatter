package server_clientDemo2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	static Socket serverSocket = null;
	static ObjectOutputStream clientOut = null;

	public static void main(String[] args) throws InterruptedException {
		new Client().setupNetworking();

		// Broadcasting test
		while (serverSocket.isConnected()) {
			say("## Broadcasting object");
			try {
				// Test object send/receive
				Message outmsg = new Message("Heya!");
				clientOut.writeObject(outmsg);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Sleep for 5s
			Thread.sleep(5000);
		}
	}

	public void setupNetworking() {
		try {
			say("## Connecting to server");
			// Setup server socket
			serverSocket = new Socket(InetAddress.getLocalHost(), 11111);
			say("- Socket established");

			// Initialise output stream
			clientOut = new ObjectOutputStream(serverSocket.getOutputStream());
			say("- Out stream established");

			// Start listening thread
			Thread listenerThread = new Thread(new serverListener());
			listenerThread.start();
			say("- Listener thread started");

			say("## Established Connection!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class serverListener implements Runnable {
		ObjectInputStream serverIn = null;
		Message serverMessage = null;

		public serverListener() {
			// Setup listener
			try {
				serverIn = new ObjectInputStream(serverSocket.getInputStream());
				say("- Input stream established");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void run() {
			say("- Listener started");

			while (serverSocket.isConnected()) {
				try {
					serverMessage = (Message) serverIn.readObject();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				} catch (ClassNotFoundException cnfEx) {
					cnfEx.printStackTrace();
				}

				// Display message
				say("- Server said: " + serverMessage.toString());
			}
		}
	}

	public static void say(String sayThis) {
		System.out.println(sayThis);
	}
}
