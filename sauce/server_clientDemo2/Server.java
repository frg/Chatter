package server_clientDemo2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
	// ArrayList<Socket> clientList = new ArrayList<Socket>();
	ArrayList<ObjectOutputStream> clientOutStreams = new ArrayList<ObjectOutputStream>();

	public static void main(String[] args) {
		new Server().startServer();
	}

	public void startServer() {
		try {
			ServerSocket socketConnection = new ServerSocket(11111);
			ObjectInputStream serverIn;
			ObjectOutputStream serverOut;

			while (true) {
				say("## Server Waiting..");
				Socket clientSock = socketConnection.accept();
				say("- Connection accepted");
				clientOutStreams.add(new ObjectOutputStream(clientSock.getOutputStream()));

				Thread t = new Thread(new ClientHandler(clientSock));
				t.start();
				say("- New client thread started");
				say("- Total clients connected: " + clientOutStreams.size());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	class ClientHandler implements Runnable {
		Socket clientSock;
		ObjectInputStream serverIn;

		public ClientHandler(Socket clientSocket) {
			try {
				this.clientSock = clientSocket;
				serverIn = new ObjectInputStream(clientSock.getInputStream());

				// Inform chat of new client
				tellEveryone(new Message("New client joined. " + clientSock.getInetAddress()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void run() {
			Message clientMessage = null;
			// If this does not work include termination character sent
			// from client side to indicate when transmission has ended
			while (clientSock.isConnected()) {
				try {
					clientMessage = (Message) serverIn.readObject();
					say("- Client message recieved: " + clientMessage.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				// broadcastMessage
				tellEveryone(clientMessage);
			}
		}
	}

	public void tellEveryone(Message sayThis) {
		Iterator<ObjectOutputStream> it = clientOutStreams.iterator();
		ObjectOutputStream serverOut = null;

		say("- Server sending: " + sayThis);
		while (it.hasNext()) {
			try {
				// Broadcast message
				it.next().writeObject(sayThis);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void say(String sayThis) {
		System.out.println(sayThis);
	}
}
