package server_clientDemo2;

import java.net.Socket;

public class ClientSocket extends Socket{
	private String clientID;
	
	public ClientSocket(Socket clientSock){
		setClientID(clientSock.getRemoteSocketAddress().toString());
	}
	
	public void setClientID(String ip) {
		this.clientID = ip;
	}
	
	public String getClientID() {
		return clientID;
	}
}
