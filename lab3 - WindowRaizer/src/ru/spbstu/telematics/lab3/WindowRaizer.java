/**
 * 
 */
package ru.spbstu.telematics.lab3;

import java.util.Calendar;

/**
 * @author HellFighter
 *
 */
public class WindowRaizer implements Runnable {
	
	private Object _mutex;	//Запрет одновременного нажатия 2х кнопок
	private boolean _close;
	private boolean _closeEna;
	private boolean _open;
	private boolean _openEna;
	private Integer _fixTime;	//Время после которого состояние кнопки запоминается
	private Float _fullMoveTime;	//Время необходимое для полного перемещения окна из одного граничнгого состояния в другое
	private Float _currentPosition;
	private boolean _isOpened;
	private boolean _isClosed;

	public WindowRaizer(float moveTime, int fixTime) {
		
		_mutex = new Object();
		_open = false;
		_openEna = true;
		_close = false;
		_closeEna = true;
		_fixTime = fixTime;
		_fullMoveTime = moveTime;
		_currentPosition = (float) moveTime;
		_isOpened = false;
		_isClosed = true;
		
		System.out.println("Window CLOSED (" + _fullMoveTime + ")");
		
		new Thread(this,"controller").start();
		
	}
	public WindowRaizer() {
		
		_mutex = new Object();
		_open = false;
		_openEna = true;
		_close = false;
		_closeEna = true;
		_fixTime = 3;
		_fullMoveTime = (float) 10;
		_currentPosition = (float) 10;
		_isOpened = false;
		_isClosed = true;
		
		System.out.println("Window CLOSED (" + _fullMoveTime + ")");
		
		new Thread(this,"controller").start();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)  throws java.io.IOException {

		WindowRaizer window = new WindowRaizer();
		window._close = true;
		
		try {
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window._close = false;
		try {
			Thread.sleep(500);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window._open = true;
		try {
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window._open = false;
		try {
			Thread.sleep(500);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window._close = true;
		try {
			Thread.sleep(4000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window._close = false;

	}

	@Override
	public void run() {
		
		if("controller".compareTo(Thread.currentThread().getName()) == 0){	// Поток контроллера
			System.out.println("Controller thread start");
			
			Calendar c = Calendar.getInstance();
			
			boolean lastOpen = false;
			boolean lastClose = false;
			long OpenStartTime = 0;
			long CloseStartTime = 0;
			
			while(true){
				
				try {
					Thread.sleep(20);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if((_open == true) && (lastOpen == false)){	//Нажали на открытие
					lastOpen = true;
					OpenStartTime = c.getTimeInMillis();
					start_open();
				}
				else if((_close == true) && (lastClose == false)){	//Нажали на закрытие
					lastClose = true;
					CloseStartTime = c.getTimeInMillis();
					start_close();
				}
				else if((_open == false) && (lastOpen == true)){	//Отпустили "открытие"
					lastOpen = false;
					if((c.getTimeInMillis() - OpenStartTime) < (_fixTime*1000))
						stop_open();
				}
				else if((_close == false) && (lastClose == true)){	//Отпустили "закрытие"
					lastClose = false;
					if((c.getTimeInMillis() - CloseStartTime) < (_fixTime*1000))
						stop_close();
				}
				
			}
		}
		else if("close".compareTo(Thread.currentThread().getName()) == 0){
			synchronized (_mutex) {
				while(_closeEna == true){
					
					if(!_isClosed){
						//Открываем окно
						try {
							Thread.sleep(200);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						_currentPosition = (float) (_currentPosition + 0.2);
						if(_currentPosition >= _fullMoveTime){
							_currentPosition = _fullMoveTime;
							_isClosed = true;
						}
						System.out.println("OnClose: Window opened on *" + (_fullMoveTime - _currentPosition) + '*');
						
					}
					else{
						System.out.println("OnClose: Window already closed");
						while(_closeEna == true){
							try {
								Thread.sleep(20);
							} 
							catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				_closeEna = true;
				
				System.out.println();
				
				_mutex.notify();
			}
			
		}
		else if("open".compareTo(Thread.currentThread().getName()) == 0){
			synchronized (_mutex) {
				while(_openEna == true){
					
					if(!_isOpened){
						//Открываем окно
						try {
							Thread.sleep(200);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						_currentPosition = (float) (_currentPosition - 0.2);
						if(_currentPosition <= 0){
							_currentPosition = (float) 0;
							_isOpened = true;
						}
						System.out.println("OnOpen: Window opened on *" + (_fullMoveTime - _currentPosition) + '*');
						
					}
					else{
						System.out.println("OnOpen: Window already opened");
						while(_openEna == true){
							try {
								Thread.sleep(20);
							} 
							catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				_openEna = true;
				
				System.out.println();
				
				_mutex.notify();
			}
			
		}
	        
	}

	public void start_close(){
		
		System.out.println();
		
		if(_isOpened == true)
			_isOpened = false;
		new Thread(this, "close").start();
	}
	
	public void stop_close(){
		_closeEna = false;
	}

	public void start_open(){

		System.out.println();
		
		if(_isClosed == true)
			_isClosed = false;
		new Thread(this, "open").start();
	}
	
	public void stop_open(){
		_openEna = false;		
	}

}
