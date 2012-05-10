/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import sun.security.action.GetLongAction;

/**
 * @author HellFighter
 *
 */
public class ChatClient {

	/**
	 * 
	 */
	public ChatClient() {
		// TODO Auto-generated constructor stub
	}

	
	class Handler{
		
		private Socket _socket;
		private ObjectOutputStream _oos;
		private ObjectInputStream _ois;
		private String _name;
		private boolean _loggedIn;
		
		public Handler() {
			_name = null;
			_loggedIn = false;
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
			
			return "Login request sent...";
		}
		
		public String logout() {
			
			try {
				_oos.writeObject(new Message(2, _name, ""));
			} catch (IOException e) {
				e.printStackTrace();
				return "Error sending message to server. Connection troubles.";
			}
			
			/*try {
				_socket.close();		//???????????????????????????????????? Дожидаться ответа???
			} catch (IOException e) {
				e.printStackTrace();
				return "Error disconnectimg. Connection troubles.";
			}*/
			
			return "Logout request sent...";
		}
		
		public String receiveMessage() {
			
			Message msg = null;

			try {
				msg = (Message)_ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				return "Error reading server message!";
			}
			
			return "OK";
			
//			if(msg.getType() == 1 && msg.getLogin().compareTo(_name) == 0)
//				setLoggedIn(true);
//			
//			if(msg.getType() == 2 && msg.getLogin().compareTo(_name) == 0) {
//				
//				setLoggedIn(false);
//				
//				try {
//					_socket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//					return msg.toString() + "\nError disconnecting. Connection troubles.";
//				}
//				
//			}
//			
////			if(isLoggedIn())
//				return msg.toString();
//			else
//				return null;
		}
		
		public String sendMessage(String msgText) {
			
			if(isLoggedIn()){
				Message msg = new Message(0, _name, msgText);
				
				try {
					_oos.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
					return "Error sending message! Connection troubles!";
				}
			}
			else
				return "You are not logged in!";
			
			return null;
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
		    		chatArea.append( _hndlr.sendMessage(inputField.getText()) + "\n" );
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
		    			if(_hndlr.isLoggedIn()){
		    				EventQueue.invokeLater(new Runnable() {
		    					@Override
		    					public void run() {
		    						logInOut.setText("Logout");
		    					}
		    				});
		    				sendButton.setEnabled(true);
		    			}
		    			
		    		} else {
		    			chatArea.append( _hndlr.logout() + "\n" );
		    			
		    			if(!(_hndlr.isLoggedIn())){
		    				EventQueue.invokeLater(new Runnable() {
		    					@Override
		    					public void run() {
		    						logInOut.setText("Login");
		    					}
		    				});
		    				sendButton.setEnabled(false);
		    			}
		    			
		    		}
		    	}
		    });

		    add(southPanel, BorderLayout.SOUTH);

		    new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
//						if( _hndlr.isLoggedIn() ) {
							chatArea.append( _hndlr.receiveMessage() + "\n" );
//						}
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
		
		
		/*Socket s = new Socket();
		s.connect(new InetSocketAddress("192.168.1.3", 12345));

		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

		Message msg = null;
		try {
			msg = (Message)ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(msg);*/
		/*Message msg = new Message(1, "Artem", "qwerty");
		oos.writeObject(msg);

		Message msg1 = new Message(0, "Artem", "Hi!");
		oos.writeObject(msg1);

		Message msg2 = new Message(3, "Artem", "Hey yall!!");
		oos.writeObject(msg2);

		Message msg3 = new Message(2, "Artem", "qwertyuiop");
		oos.writeObject(msg3);*/
		
		final Handler hndlr = (new ChatClient()).new Handler();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChatFrame chatFrame = (new ChatClient()).new ChatFrame(hndlr);
				chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				chatFrame.setVisible(true);
			}
		});
				
//		System.out.println(msg.toString());
		
	}

}
