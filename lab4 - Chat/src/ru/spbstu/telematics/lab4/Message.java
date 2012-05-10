/**
 * 
 */
package ru.spbstu.telematics.lab4;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


/**
 * @author HellFighter
 *
 */
@SuppressWarnings("serial")
public class Message implements Serializable {
	
	private int _type;	// 0 - chat message, 1 - login, 2 - logout
	
	private String _sendTime;
	private String _user;
	private String _message;
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Message(int type, String user, String message) {
		Calendar sendTimeCalendar = Calendar.getInstance();
		sendTimeCalendar.getTime();
		Date sendTime = sendTimeCalendar.getTime();
		_sendTime = sendTime.getHours() + ":" + sendTime.getMinutes() + ":" + sendTime.getSeconds();
		
		_type = type;
		_user = user;
		
		if(_type != 0 && _type != 1 && _type != 2)
			_message = "Error message type!";
		else
			_message = message;
	}
	
	/**
	 * @return the _type
	 */
	public int getType() {
		return _type;
	}

	/**
	 * @return the _sendTime
	 */
	public String getSendTime() {
		return _sendTime;
	}

	/**
	 * @return the _user
	 */
	public String getLogin() {
		if(_type == 1 || _type == 2)
			return _user;
		else
			return null;
	}
	
	/**
	 * @param _user the _user to set
	 */
//	public void setLogin(String login) {
//		if(_type == 1)
//			this._user = login;
//	}
	
	/**
	 * @return the _message
	 */
	public String getPassword() {
		if(_type == 1)
			return _message;
		else
			return null;
	}
	
	/**
	 * @param _message the _message to set
	 */
//	public void setPassword(String password) {
//		if(_type == 1)
//			this._message = password;
//	}

	@Override
	public String toString() {
		if(_type == 0)
			return _user + "(" + _sendTime + "):   " +  _message;
		else if(_type == 1)
			return _user + "(" + _sendTime + "):          logged in";
		else if(_type == 2)
			return _user + "(" + _sendTime + "):          logged out";
		else
			return _user + "(" + _sendTime + "): Error message type!";
	}

}
