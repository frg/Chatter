package saucy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChatClient {
	JScrollPane qScroller;
	JButton sendBtn;
	JTextArea incoming;
	JTextField outgoing;
	JTextField ipField;
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
		frame.setResizable(false);
		
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		
		qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		ipField = new JTextField(10);
		ipField.setText("Server ip..");
		ipField.setForeground(new Color(100, 100, 100));
		ipField.addFocusListener(new ipFieldListener());
		ipField.addActionListener(new ipFieldListener());
		
		outgoing = new JTextField(20);
		outgoing.setEnabled(false);
		
		sendBtn = new JButton("Send");
		sendBtn.addActionListener(new SendButtonListener());
		sendBtn.setEnabled(false);
		
		mainPnl.add(qScroller);
		mainPnl.add(ipField);
		mainPnl.add(outgoing);
		mainPnl.add(sendBtn);
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPnl);
		frame.setSize(600, 315);
		frame.setVisible(true);
	}
	
	public void setUpNetworking(String serverIp) {
		try {
			sock = new Socket(serverIp, 5100);
			
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking established");
			
			// Start listening thread
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
			
			ipField.setEditable(false);
			outgoing.setEnabled(true);
			sendBtn.setEnabled(true);
		} catch(IOException ioEx) {
			ioEx.printStackTrace();
			incoming.append("=== Could not establish network using (ip: " + ipField.getText() + ") ===\n");
		}
	}
	
	// Instructions to field format
	public class ipFieldListener implements FocusListener, ActionListener {
		// Check if enter is pressed
		@Override
		public void actionPerformed(ActionEvent e) {
			incoming.append("Connecting..\n");
			// TODO Bug: appended text does not appear before trying to connect
			
			// Try establish networking
			setUpNetworking(ipField.getText());
		}
		
		@Override
        public void focusGained(FocusEvent e) {
			if (ipField.getText().equals("Server ip..")) {
				ipField.setText("");
				ipField.setForeground(new Color(0, 0, 0));
			}
        }

        @Override
        public void focusLost(FocusEvent e) {
        	if (ipField.getText().isEmpty()) {
				ipField.setText("Server ip..");
				ipField.setForeground(new Color(100, 100, 100));
			}
        }
	}
	
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			// If field not empty do
			if (outgoing.getText() != "") {
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


