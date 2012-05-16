/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HellFighter
 *
 */
public class ActivityTable implements Iterable<Client>{
	
	private List<Client> _clients;
	private Object _mutex;

	/**
	 * 
	 */
	public ActivityTable() {
		_clients = Collections.synchronizedList(new LinkedList<Client>());
		_mutex = new Object();
	}

	public void add(Client entry) {
		_clients.add(entry);		
	}
	
	public int find(Client entry) {
		return _clients.indexOf(entry);
	}
	
	public boolean remove(Client entry) {
		return _clients.remove(entry);
	}

	@Override
	public Iterator<Client> iterator() {
		return _clients.iterator();
	}
	
	public void sendAll(Message msg){
		for (Client clt : _clients) {
			try {
				synchronized (_mutex) {
					clt.getObjectOutputStream().writeObject(msg);
					clt.getObjectOutputStream().flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error writing object in sendAll!");
			}
		}

	}
	
}
