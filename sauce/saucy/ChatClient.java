package saucy;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatClient {
	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public static void main(String args[]){
		ChatClient client = new ChatClient();
		client.go();
	}

	private void go() {
		JFrame frame = new JFrame("Chatter biatches!");
		JPanel mainPnl = new JPanel();
		
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		outgoing = new JTextField(20);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(new SendButtonListener());
		
		mainPnl.add(qScroller);
		mainPnl.add(outgoing);
		mainPnl.add(sendBtn);
		
		setUpNetworking();
		
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPnl);
		frame.setSize(800, 500);
		frame.setVisible(true);
	}
	
	public void setUpNetworking() {
		try {
			// Get ip
			InetAddress ipAddress = InetAddress.getLocalHost();
			sock = new Socket(ipAddress.getHostAddress(), 5000);
			
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking established");
		} catch(IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				writer.println(outgoing.getText());
				writer.flush();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			outgoing.setText("");
			outgoing.requestFocus();
		}

	}
	
	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read: " + message);
					incoming.append(message + "\n");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}


