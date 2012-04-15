/**
 * 
 */
package ru.spbstu.telematics.lab3;

import java.util.Calendar;
//import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author HellFighter
 *
 */
public class WindowRaizer implements Runnable {
		
//	private Object _mutex;	//Запрет одновременного нажатия 2х кнопок
	
	private ReadWriteLock _oc;
	private Lock _ocR;
	private Lock _ocW;
	private boolean _close;	//отслеживается в потоке контроллера
	private boolean _open;
	
	private ReadWriteLock _soc;
	private Lock _socR;
	private Lock _socW;
	private boolean _startClose;	//отслеживается в потоках открытия/зарытия
	Object _closeLock = new Object();
	private boolean _startOpen;
	Object _openLock = new Object();
	
	private Integer _fixTime;	//Время после которого состояние кнопки запоминается
	private Float _fullMoveTime;	//Время необходимое для полного перемещения окна из одного граничнгого состояния в другое
	
	private ReadWriteLock _p;
	private Lock _pR;
	private Lock _pW;
	private Float _currentPosition;
	
	private ReadWriteLock _ioc;
	private Lock _iocR;
	private Lock _iocW;
	private boolean _isOpened;
	private boolean _isClosed;

	public WindowRaizer(float moveTime, int fixTime) {
		
//		_mutex = new Object();
		
		_oc = new ReentrantReadWriteLock();
		_ocR = _oc.readLock();
		_ocW = _oc.writeLock();
		_open = false;
		_close = false;
		
		_soc = new ReentrantReadWriteLock();
		_socR = _soc.readLock();
		_socW = _soc.writeLock();
		_startOpen = false;
		_startClose = false;		
		
		_fixTime = fixTime;
		_fullMoveTime = moveTime;
		
		_p = new ReentrantReadWriteLock();
		_pR = _p.readLock();
		_pW = _p.writeLock();
		_currentPosition = (float) moveTime;
		
		_ioc = new ReentrantReadWriteLock();
		_iocR = _ioc.readLock();
		_iocW = _ioc.writeLock();
		_isOpened = false;
		_isClosed = true;
	
		System.out.println("Window CLOSED (" + _fullMoveTime + ")");
		
		new Thread(this,"controller").start();
		
		new Thread(this, "open").start();
		
		new Thread(this, "close").start();
		
	}
	
	public WindowRaizer() {
		
//		_mutex = new Object();

		_oc = new ReentrantReadWriteLock();
		_ocR = _oc.readLock();
		_ocW = _oc.writeLock();
		_open = false;
		_close = false;
		
		_soc = new ReentrantReadWriteLock();
		_socR = _soc.readLock();
		_socW = _soc.writeLock();
		_startOpen = false;
		_startClose = false;		
		
		_fixTime = 1000;
		_fullMoveTime = (float) 10;
		
		_p = new ReentrantReadWriteLock();
		_pR = _p.readLock();
		_pW = _p.writeLock();
		_currentPosition = (float) _fullMoveTime;
		
		_ioc = new ReentrantReadWriteLock();
		_iocR = _ioc.readLock();
		_iocW = _ioc.writeLock();
		_isOpened = false;
		_isClosed = true;
		
		System.out.println("Window CLOSED (" + _fullMoveTime + ")");
		
		new Thread(this,"controller").start();
		
		new Thread(this, "open").start();
		
		new Thread(this, "close").start();
		
	}

	
	public boolean getClose() {
		_ocR.lock();
		try{
			return _close;
		} finally{
			_ocR.unlock();
		}
	}
	public void setClose(boolean close) {
		_ocW.lock();
		if(close == true)
			this._open = false;
		this._close = close;
		_ocW.unlock();
	}
	public boolean getOpen() {
		_ocR.lock();
		try{
			return _open;
		} finally{
			_ocR.unlock();
		}
	}
	public void setOpen(boolean open) {
		_ocW.lock();
		if(open == true)
			this._close = false;
		this._open = open;
		_ocW.unlock();
	}
	
	public boolean getStartClose() {
		_socR.lock();
		try{
//			while (!_startClose) {
//				synchronized (_condSOC) {
//					_condSOC.wait();
//				}
//			}
			return _startClose;
//		} catch (InterruptedException e) {
//			throw new RuntimeException("");
		} finally{
			_socR.unlock();
		}
//		return true;
	}
	public void setStartClose(boolean startClose) {
		_socW.lock();
		if(startClose == true){
			this._startOpen = false;
			synchronized (_closeLock) {
				_closeLock.notify();
			}
		}
		this._startClose = startClose;
		_socW.unlock();
	}
	public boolean getStartOpen() {
		_socR.lock();
		try{
//			while (!_startOpen) {
//				synchronized (_condSOC) {
//					_condSOC.wait();
//				}
//			}
			return _startOpen;
//		} catch (InterruptedException e) {
//			throw new RuntimeException("");
		} finally{
			_socR.unlock();
		}
		
//		return true;
	}
	public void setStartOpen(boolean startOpen) {
		_socW.lock();
		if(startOpen){
			this._startClose = false;
			synchronized (_openLock) {
				_openLock.notify();
			}
		}
		this._startOpen = startOpen;
		_socW.unlock();
	}
	
	public boolean getOpened() {
		_iocR.lock();
		try{
			return _isOpened;
		} finally{
			_iocR.unlock();
		}
	}
	public void isOpened(boolean isOpened) {
		_iocW.lock();
		this._isOpened = isOpened;
		_iocW.unlock();
	}
	public boolean getClosed() {
		_iocR.lock();
		try{
			return _isClosed;
		} finally{
			_iocR.unlock();
		}
	}
	public void isClosed(boolean isClosed) {
		_iocW.lock();
		this._isClosed = isClosed;
//		System.out.println("isClose set: " + this._isClosed);
		_iocW.unlock();
	}
	
	public Float getCurrentPosition() {
		_pR.lock();
		try{
			return _currentPosition;
		} finally{
			_pR.unlock();
		}
	}
	public void setCurrentPosition(Float currentPosition) {
		_pW.lock();
		this._currentPosition = currentPosition;
		_pW.unlock();
	}
	public void addCurrentPosition(Float add) {
		_pW.lock();
		this._currentPosition += add;
		_pW.unlock();
	}
	public void subCurrentPosition(Float add) {
		_pW.lock();
		this._currentPosition -= add;
		_pW.unlock();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args)  throws java.io.IOException {

		WindowRaizer window = new WindowRaizer();
		
		window.setClose(true);
		try {
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
//		window.setClose(false);
//		try {
//			Thread.sleep(500);
//		} 
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		window.setOpen(true);
		try {
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
//		window.setOpen(false);
//		try {
//			Thread.sleep(500);
//		} 
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		window.setClose(true);
		try {
			Thread.sleep(4000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		//window.setClose(false);
		window.setOpen(true);
		try {
			Thread.sleep(800);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setOpen(false);
		
		try {
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setClose(true);

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
			
			while(!Thread.currentThread().isInterrupted()){
				
				try {
					Thread.sleep(20);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if((getOpen() == true) && (lastOpen == false)){	//Нажали на открытие
					lastOpen = true;
					OpenStartTime = c.getTimeInMillis();
					start_open();
				}
				else if((getClose() == true) && (lastClose == false)){	//Нажали на закрытие
					lastClose = true;
					CloseStartTime = c.getTimeInMillis();
					start_close();
				}
				else if((getOpen() == false) && (lastOpen == true)){	//Отпустили "открытие"
					lastOpen = false;
					if((c.getTimeInMillis() - OpenStartTime) > (_fixTime))
						stop_open();
				}
				else if((getClose() == false) && (lastClose == true)){	//Отпустили "закрытие"
					lastClose = false;
					if((c.getTimeInMillis() - CloseStartTime) > (_fixTime))
						stop_close();
				}
				
			}
		}
		
		//-----
		
		else if("close".compareTo(Thread.currentThread().getName()) == 0){
			
			System.out.println("Close thread start");
			
			boolean msg = false;
			
			while(!Thread.currentThread().isInterrupted()) {
				
//				wait
				synchronized (_closeLock) {
					try {
						_closeLock.wait();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				System.out.println("OnClose: Window opened on * " + (_fullMoveTime - getCurrentPosition()) + " *");
				
				while(getStartClose() == true){
					
//					synchronized(_mutex){
					
					if(getOpened()){
						isOpened(false);
						System.out.println("\nWindow close start (10)");
					}
					
					if(!getClosed()){
						
						msg = false;
						
						addCurrentPosition((float) 0.2);
						if(getCurrentPosition() >= _fullMoveTime){
							setCurrentPosition(_fullMoveTime);
							isClosed(true);
							System.out.println("OnClose: Window closed");
						}
						else
							System.out.println("OnClose: Window opened on * " + (_fullMoveTime - getCurrentPosition()) + " *");
						
						try {
							Thread.sleep(200);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					else{
						if(!msg){
							System.out.println("OnClose: Window already closed (0)");
							msg = true;
							setStartClose(false);
						}
					}
				
//					}
					
				}
				
//				notify

			}
			
		}
		
		//-----
		
		else if("open".compareTo(Thread.currentThread().getName()) == 0){
			
			System.out.println("Open thread start");
			
			boolean msg = false;
			
			while(!Thread.currentThread().isInterrupted()) {
				
				synchronized (_openLock) {
					try {
						_openLock.wait();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				System.out.println("OnOpen: Window opened on * " + (_fullMoveTime - getCurrentPosition()) + " *");
				
				while(getStartOpen() == true){
					
//					synchronized(_mutex){
					
					if(getClosed()){
						isClosed(false);
						System.out.println("\nWindow open start (0)");
					}
					
					if(!getOpened()){

						msg = false;
						
						subCurrentPosition((float) 0.2);
						if(getCurrentPosition() <= 0){
							setCurrentPosition((float) 0);
							isOpened(true);
							System.out.println("OnOpen: Window opened (10)");
						}
						else
							System.out.println("OnOpen: Window opened on * " + (_fullMoveTime - getCurrentPosition()) + " *");
						
						try {
							Thread.sleep(200);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					else{
						if(!msg){
							System.out.println("OnOpen: Window already opened");
							msg = true;
							setStartOpen(false);
						}
					}
					
//					}
				
				}

			}
			
		}
	        
	}

	public void start_close(){

		System.out.println();
		
		setStartClose(true);
	}
	
	public void stop_close(){
		setStartClose(false);
	}

	public void start_open(){

		System.out.println();
		
		setStartOpen(true);
	}
	
	public void stop_open(){
		setStartOpen(false);
	}

}
