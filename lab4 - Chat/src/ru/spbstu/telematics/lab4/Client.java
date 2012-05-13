/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author HellFighter
 *
 */
public class Client {
	
	Socket _socket;
	ObjectInputStream _ois;
	ObjectOutputStream _oos;

	/**
	 * @throws IOException 
	 * 
	 */
	public Client(Socket client) {
		_socket = client;
		try {
			_ois = new ObjectInputStream(_socket.getInputStream());
			_oos = new ObjectOutputStream(_socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error getting IO stream");
		}
	}
	
	public InetAddress getIP(){
		return _socket.getInetAddress();
	}
	
	public int getPort(){
		return _socket.getPort();
	}
	
	public ObjectInputStream getObjectInputStream(){
		return _ois;
	}

	public ObjectOutputStream getObjectOutputStream(){
		return _oos;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		Client tmp = (Client)obj;
		
			if(getPort() == tmp.getPort()) {
				return true;
			}
		
		return false;
	}
}
