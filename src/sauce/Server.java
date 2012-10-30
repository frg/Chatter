package sauce;

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
		StopWatch timer = new StopWatch();
		timer.start();
		
		try {
			ServerSocket serverSock = new ServerSocket(5359);
			
			while(true) {
				Socket sock = serverSock.accept();
				
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				// Write running time
				writer.println(broadcast + " " + timer.getElapsedTime() + "ms");
				writer.close();
				System.out.println(timer.getElapsedTime() + "ms");
			}
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
