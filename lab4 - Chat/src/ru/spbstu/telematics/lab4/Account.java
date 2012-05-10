/**
 * 
 */
package ru.spbstu.telematics.lab4;

/**
 * @author HellFighter
 *
 */
public class Account {
	
	private String _login;
	private int _passwordHASH;

	/**
	 * 
	 */
	public Account(String login, String password) {
		_login = login;
		_passwordHASH = password.hashCode();
	}

	/**
	 * @return the _login
	 */
	public String getLogin() {
		return _login;
	}

	/**
	 * @return the _passwordHASH
	 */
	public int getPasswordHASH() {
		return _passwordHASH;
	}
	
	@Override
	public boolean equals(Object obj) {

		Account tmp = (Account)obj;
		
		if(tmp.getLogin().compareTo(_login) == 0){
			if(tmp.getPasswordHASH() == _passwordHASH){
				return true;
			}
		}
		
		return false;
	}

}
