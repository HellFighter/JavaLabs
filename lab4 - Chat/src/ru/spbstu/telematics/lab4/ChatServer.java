/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HellFighter
 *
 */
public class ChatServer {
	
	private ExecutorService _pool;
	private AccountTable _accountTable;
	private ActivityTable _activityTable;

	/**
	 * 
	 */
	public ChatServer(int poolSize) {
		_pool = Executors.newFixedThreadPool(poolSize);
		_accountTable = new AccountTable();
		_activityTable = new ActivityTable();
	}
	
	public synchronized ExecutorService getPool() {
		return _pool;
	}
	
	private void serve(Client client) {

		Message firstMsg = null;
		try {
			firstMsg = (Message)client.getObjectInputStream().readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			System.out.println("Error reading object!");
		}
		
		if(firstMsg.getType() == 1) {
			
			if(_accountTable.find(firstMsg.getLogin(), firstMsg.getPassword()) >= 0){
				
				if(!_activityTable.findName(firstMsg.getLogin())){
			
					_activityTable.add(client, firstMsg.getLogin());
					System.out.println(firstMsg);
				
					_activityTable.sendAll(firstMsg);
				
				}
				else{
					System.out.println(firstMsg.getLogin() + " is already logged in by another client!");
					_activityTable.writeObject(client, new Message(0, "Notyfication!", "You are already logged in by another client!"));
					return;
				}

			}
			else{
				
				System.out.println(firstMsg.getLogin() + "(" + firstMsg.getSendTime() + "): login failure!");
				Message errorMsg = new Message(0, "Error!", "Invalid login information!");
				try {
					client.getObjectOutputStream().writeObject(errorMsg);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error writing object!");
				}
				
				return;
			
			}
			
		}
		
		while(true){
			
			Message msg = null;
			try {
				msg = (Message)client.getObjectInputStream().readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				System.out.println("Error reading object!");
			}
			
			boolean brk = false;
			
			switch(msg.getType()){
			
				case 0:	//chat message
					
					System.out.println(msg);

					_activityTable.sendAll(msg);
					
					break;
					
				case 1:	//Relogin message
					
					System.out.println(msg.getLogin() + " already logged in!");
					Message ntfyMsg = new Message(0, "Notyfication!", "You are already logged in!");
					
//					try {
//						client.getObjectOutputStream().writeObject(ntfyMsg);
						_activityTable.writeObject(client, ntfyMsg);
//					} catch (IOException e) {
//						e.printStackTrace();
//						System.out.println("Error writing object!");
//					}
										
					break;
					
				case 2:	//Logout message
					
					System.out.println(msg);
					
					_activityTable.sendAll(msg);
					
					System.out.println("removing: " + _activityTable.remove(client, msg.getLogin()));
					
					brk = true;
					
					break;
					
				default:	//Invalid message type
					
					System.out.println(msg);
					Message errorMsg = new Message(0, "Error!", "Invalid message type!");
//				try {
//					client.getObjectOutputStream().writeObject(errorMsg);
					_activityTable.writeObject(client, errorMsg);
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.out.println("Error writing object!");
//				}
					
					break;
				
			}
			
			if(brk)
				break;
			
		}
		
	}
	
	/**
	 * @param args
	 * @throws IOException  
	 */
	public static void main(String[] args) throws IOException {
		
		final ChatServer server = new ChatServer(10);
		
		server.addAccounts();
		
		ServerSocket serverSocket = new ServerSocket(12345);
		
		System.out.println("Server started on port " + serverSocket.getLocalPort());
		
		while(true) {
			
			System.out.println("Waiting for acception...");
			final Socket clientSocket = serverSocket.accept();
			System.out.println("Connected: client port=" + clientSocket.getPort());
			final Client client = new Client(clientSocket);
			server.getPool().submit(
					new Runnable() {

						@Override
						public void run() {
							
							try {
								server.serve(client);
							} catch (Exception e) {
								e.printStackTrace();
								server._activityTable.remove(client, "");
							}
							
						} //run
						
					} //new Runnable
			); //submit
			
		} //while(true)
		
	} //main
	
	private void addAccounts(){
		_accountTable.add("Artem","qwerty");
		_accountTable.add("Artem 1","qwerty 1");
		_accountTable.add("Artem 2","qwerty 2");
	}

}
