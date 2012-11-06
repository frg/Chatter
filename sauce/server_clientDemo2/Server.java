package server_clientDemo2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Server {

   public static void main(String[] arg) {

         Employee employee = null;

         try {
        	ArrayList<Socket> clientList = new ArrayList<Socket>(); //
            ServerSocket socketConnection = new ServerSocket(11111);
            ObjectInputStream serverIn;
            ObjectOutputStream serverOut;

			while (true) {
            	System.out.println("Server Waiting");

            	Socket clientSock = socketConnection.accept();

            	serverIn = new ObjectInputStream(clientSock.getInputStream());
           	 	serverOut = new ObjectOutputStream(clientSock.getOutputStream());

            	employee = (Employee) serverIn.readObject();

            	employee .setEmployeeNumber(256);
            	employee .setEmployeeName("John");
		
            	serverOut.writeObject(employee);

            	serverIn.close();
            	serverOut.close();
            	
            	clientList.add(clientSock);
            	System.out.println("Clients connected: " + clientList.size());
			} // end of the while loop
         }  catch(Exception e) {
        	 System.out.println(e); 
         }
         
         
   }

}
