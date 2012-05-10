/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.net.Socket;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HellFighter
 *
 */
public class ActivityTable implements Iterable<Client>{
	
	private List<Client> _cli;
	private List<Client> _clients;

	/**
	 * 
	 */
	public ActivityTable() {
		_cli = new LinkedList<Client>();
		_clients = Collections.synchronizedList(_cli);
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
}
