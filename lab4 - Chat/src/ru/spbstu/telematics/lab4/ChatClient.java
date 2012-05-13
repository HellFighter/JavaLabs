/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author HellFighter
 *
 */
public class ChatClient {
	
	class Handler{
		
		private Socket _socket;
		private ObjectOutputStream _oos;
		private ObjectInputStream _ois;
		private String _name;
		private boolean _loggedIn;
		private boolean _loginSent;
		
		public Handler() {
			_name = null;
			_loggedIn = false;
			_loginSent = false;
		}
		
		/**
		 * @return the _loggedIn
		 */
		public synchronized boolean isLoggedIn() {
			return _loggedIn;
		}

		/**
		 * @param loggedIn the _loggedIn to set
		 */
		public synchronized void setLoggedIn(boolean loggedIn) {
			this._loggedIn = loggedIn;
		}

		public String login (String login, String password) {
			
			_socket = new Socket();
			
			try {
				_socket.connect(new InetSocketAddress("192.168.1.3", 12345));
			} catch (IOException e) {
				e.printStackTrace();
				return "Can't connect to server!";
			}

			try {
				_oos = new ObjectOutputStream(_socket.getOutputStream());
				_ois = new ObjectInputStream(_socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return "Can't get IO stream!";
			}
			
			try {
				_oos.writeObject(new Message(1, login, password));
			} catch (IOException e) {
				e.printStackTrace();
				return "Error sending message to server. Connection troubles.";
			}
			
			_name = login;
			_loginSent = true;
			
			return "Login request sent...";
		}
		
		public String logout() {
			
			try {
				_oos.writeObject(new Message(2, _name, ""));
			} catch (IOException e) {
				e.printStackTrace();
				return "Error sending message to server. Connection troubles.";
			}
						
			return "Logout request sent...";
		}
		
		public String receiveMessage() {
			
			Message msg = null;

			try {
				while(!_loginSent){}
				msg = (Message)_ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				return "Error reading server message!";
			}
			
			if(msg.getType() == 1 && msg.getLogin().compareTo(_name) == 0) {
				setLoggedIn(true);
			}
			
			if(msg.getType() == 2 && msg.getLogin().compareTo(_name) == 0) {
				
				setLoggedIn(false);
				_loginSent = false;
				
				try {
					_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					return msg.toString() + "\nError disconnecting. Connection troubles.";
				}
				
			}
			
			return msg.toString();
		}
		
		public String sendMessage(String msgText) {
			
			if(isLoggedIn()){
				if("".compareTo(msgText) != 0){
					Message msg = new Message(0, _name, msgText);
					
					try {
						_oos.writeObject(msg);
					} catch (IOException e) {
						e.printStackTrace();
						return "Error sending message! Connection troubles!";
					}
				}
			}
			else
				return "You are not logged in!";
			
			return "";
		}

	}
	
	
	@SuppressWarnings("serial")
	class ChatFrame extends JFrame{
		
		public static final int WIDTH = 640;
		public static final int HEIGHT = 480;
		
		private Handler _hndlr;

		public ChatFrame(final Handler hndlr) {
			
			_hndlr = hndlr;
			
			setTitle("Chat");
		    setSize(WIDTH, HEIGHT);

		    final JTextField usernameField = new JTextField();
		    final JPasswordField passwordField = new JPasswordField();
		    
		    JPanel northPanel = new JPanel();
		    northPanel.setLayout(new GridLayout(1, 5));
		    northPanel.add(new JLabel("Username: ", SwingConstants.RIGHT));
		    northPanel.add(usernameField);
		    northPanel.add(new JLabel("Password: ", SwingConstants.RIGHT));
		    northPanel.add(passwordField);
		    northPanel.add(new JLabel("                         ", SwingConstants.RIGHT));
		    
		    final JTextArea chatArea = new JTextArea(23, 54);
		    chatArea.setEditable(false);
		    JScrollPane scrollPane = new JScrollPane(chatArea);

		    add(scrollPane, BorderLayout.CENTER);
		    
		    add(northPanel, BorderLayout.NORTH);

		    JPanel southPanel = new JPanel();
		    southPanel.setLayout(new GridLayout(2, 1));
		    
		    final JTextField inputField = new JTextField(48);
		    southPanel.add(inputField);
		    
		    final JButton sendButton = new JButton("Send");
		    sendButton.setEnabled(false);
		    southPanel.add(sendButton);
		    sendButton.addActionListener(new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent e) {
		    		String snd = _hndlr.sendMessage(inputField.getText());
		    		if("".compareTo(snd) != 0){
			    		chatArea.append( snd + "\n" );
		    		}
		    		inputField.setText(null);
		    	}
		    });
		    
		    final JButton logInOut = new JButton("Login");
		    
		    northPanel.add(logInOut);
		    
		    logInOut.addActionListener(new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent e) {
		    		if( !(_hndlr.isLoggedIn()) ) {
		    			chatArea.append( _hndlr.login(usernameField.getText(), new String(passwordField.getPassword())) + "\n" );
		    		} else {
		    			chatArea.append( _hndlr.logout() + "\n" );
		    		}
		    	}
		    });

		    add(southPanel, BorderLayout.SOUTH);

		    new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						
						boolean oldL = _hndlr.isLoggedIn();
						
						chatArea.append( _hndlr.receiveMessage() + "\n" );
						
						boolean newL = _hndlr.isLoggedIn();
						if(oldL != newL){
							if(_hndlr.isLoggedIn()){
			    				EventQueue.invokeLater(new Runnable() {
			    					@Override
			    					public void run() {
			    						logInOut.setText("Logout");
			    						sendButton.setEnabled(true);
			    					}
			    				});
			    			}
			    			else{
			    				EventQueue.invokeLater(new Runnable() {
			    					@Override
			    					public void run() {
			    						logInOut.setText("Login");
			    						sendButton.setEnabled(false);
			    					}
			    				});
			    			}
						}
					}
				}
			}).start();
		    
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final Handler hndlr = (new ChatClient()).new Handler();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChatFrame chatFrame = (new ChatClient()).new ChatFrame(hndlr);
				chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				chatFrame.setVisible(true);
			}
		});
		
	}

}
