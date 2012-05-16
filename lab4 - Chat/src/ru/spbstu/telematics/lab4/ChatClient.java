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
	
	private String _ip = "192.168.1.3";
	private int _port = 12345;
	
	class Handler{
		
		private Socket _socket;
		private Object _socketMutex;
		private ObjectOutputStream _oos;
		private Object _outMutex;
		private ObjectInputStream _ois;
		private Object _inMutex;
		private String _name;
		private boolean _loggedIn;
		private boolean _loginSent;
		

		public Handler() {
			_name = null;
			_loggedIn = false;
			_loginSent = false;
//			_socket = new Socket();
			_socketMutex = new Object();
			_outMutex = new Object();
			_inMutex = new Object();
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

		/**
		 * @return the _name
		 */
		public synchronized String getName() {
			return _name;
		}

		/**
		 * @param _name the _name to set
		 */
		public synchronized void setName(String name) {
			this._name = name;
		}

		/**
		 * @return the _loginSent
		 */
		public synchronized boolean isLoginSent() {
			return _loginSent;
		}
		
		/**
		 * @param _loginSent the _loginSent to set
		 */
		public synchronized void setLoginSent(boolean loginSent) {
			this._loginSent = loginSent;
		}
		
		public void connect(String addr, int port) throws IOException{
			synchronized (_socketMutex) {
				_socket = new Socket();
				_socket.connect(new InetSocketAddress(addr, port));
				_oos = new ObjectOutputStream(_socket.getOutputStream());
				_ois = new ObjectInputStream(_socket.getInputStream());
			}
		}
		
		public void disconnect() throws IOException{
			synchronized (_socketMutex) {
				_socket.close();
			}
		}
		
		public void writeObject(Message msg) throws IOException{
			synchronized (_outMutex){
				if(_oos != null)
					_oos.writeObject(msg);
			}
		}
		
		public Message readObject() throws ClassNotFoundException, IOException{
			synchronized(_inMutex){
				return (Message)_ois.readObject();
			}
		}
		
		public String login (String login, String password) {
			
			try {
				connect(_ip, _port);
			} catch (IOException e) {
				e.printStackTrace();
				return "Can't connect to server or get IO stream!";
			}

			try {
				writeObject(new Message(1, login, password));
			} catch (IOException e) {
				e.printStackTrace();
				return "Error sending message to server. Connection troubles.";
			}
			
			setName(login);
			setLoginSent(true);
			
			return "Login request sent...";
		}
		
		public String logout() {
			
			if(isLoggedIn()){
				try {
					writeObject(new Message(2, getName(), ""));
				} catch (IOException e) {
					e.printStackTrace();
					return "Error sending message to server. Connection troubles.";
				}
				
				return "Logout request sent...";
			}
			else
				return "Not logged in!!!";
						
		}
		
		public String receiveMessage() {
			
			Message msg = null;

			try {
				while(!isLoginSent()){}
				msg = readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				return "Error reading server message!";
			}
			
			if(msg.getType() == 1 && msg.getLogin().compareTo(getName()) == 0) {
				setLoggedIn(true);
			}
			
			if(msg.getType() == 2 && msg.getLogin().compareTo(getName()) == 0) {
				
				setLoggedIn(false);
				setLoginSent(false);
				
				try {
					disconnect();
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
					Message msg = new Message(0, getName(), msgText);
					
					try {
						writeObject(msg);
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
