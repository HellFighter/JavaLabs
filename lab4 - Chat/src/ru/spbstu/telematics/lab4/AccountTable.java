/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HellFighter
 *
 */
public class AccountTable  implements Iterable<Account>{
	
	private List<Account> _accounts;

	/**
	 * 
	 */
	public AccountTable() {
		_accounts = Collections.synchronizedList(new LinkedList<Account>());
	}
	
	/**
	 * add new account to table
	 */
	public void add(Account entry) {
		_accounts.add(entry);
	}
	public void add(String login, String password) {
		_accounts.add(new Account(login,password));
	}
	
	/**
	 * find account in table
	 */
	public int find(Account entry) {
		return _accounts.indexOf(entry);
	}
	public int find(String login, String password) {
		return _accounts.indexOf(new Account(login,password));
	}

	@Override
	public Iterator<Account> iterator() {
		return _accounts.iterator();
	}
	
}
