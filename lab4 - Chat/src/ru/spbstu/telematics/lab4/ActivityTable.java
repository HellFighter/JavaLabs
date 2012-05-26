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
	private List<String> _cNames;

	/**
	 * 
	 */
	public ActivityTable() {
		_clients = Collections.synchronizedList(new LinkedList<Client>());
		_cNames = Collections.synchronizedList(new LinkedList<String>());
		_mutex = new Object();
	}

	public void add(Client entry, String cName) {
		_clients.add(entry);	
		_cNames.add(cName);
	}
	
	public int find(Client entry) {
		return _clients.indexOf(entry);
	}
	
	public boolean remove(Client entry, String cName) {
//		return _clients.remove(entry);
		if("".compareTo(cName) == 0){ // Unexpected disconnection (unknown cName)
			int index = _clients.indexOf(entry);
			if(index >= 0){
				_clients.remove(index);
				_cNames.remove(index);
				return true;
			}
			else
				return false;
		}
		
		if(_clients.remove(entry)){
			_cNames.remove(cName);
			return true;
		}
		
		return false;
	}

	@Override
	public Iterator<Client> iterator() {
		return _clients.iterator();
	}
	
	public boolean findName(String Name){
		for (String cName : _cNames) {
			if(cName.compareTo(Name) == 0)
				return true;
		}
		
		return false;
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
	
	public void writeObject(Client clt, Message msg){
		try {
			synchronized (_mutex) {
				clt.getObjectOutputStream().writeObject(msg);
				clt.getObjectOutputStream().flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing object in sendBack!");
		}
	}
	
}
