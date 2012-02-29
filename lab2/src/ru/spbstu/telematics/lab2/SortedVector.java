package ru.spbstu.telematics.lab2;

import java.io.*;

public class SortedVector implements ISortedVector{
	
	private Comparable _arr[];
	private int _size = 0;
	
	SortedVector(int size){
		_arr = new Comparable[size];
	}

	public void show(){
		
		if(_size > 0)
			System.out.println("\n\nIn vector: ");
		else
			System.out.println("\nNothing in vector...");
		
		for(int i = 0; i < _size; i++){
			System.out.print((i+1) + ") " + _arr[i].toString() + "\n");
		}
		
		System.out.println("\n");
	}
	
	@Override
	public void add(Comparable o) {

		System.out.print("\nAdd in vector: " + o + "\n");
		
		int j;	
		for(j = 0; (j < _size) && (o.compareTo(_arr[j]) > 0); )
			j++;
		
		for(int i = j; i < _size; i++)
			_arr[i+1] = _arr[i];
		
		_arr[j] = o;
		
		_size++;
		
		
		System.out.print("\nVector size: " + _size + " (" + _arr.length + ")\n");
		
		if(_arr.length == _size){	// If no empty space
			
			System.out.print("\nResizing vector to " + (_arr.length*2) + " (from " + _arr.length + ")\n");
			
			Comparable arr[] = new Comparable[(_arr.length*2)];
			
			for(j = 0; j < _size; j++)
				arr[j] = _arr[j];
			
			_arr = arr;
			
		}
		
	}

	@Override
	public void remove(int index) {
		
		System.out.print("\nRemove from vector element number: " + index + "\n");
		
		if((index > _size) || (index <= 0)){
			
			System.out.print("\nNo element with such index in vector: " + index + "\n");
			return;
		
		}
		
		for(int i = (index - 1); i < (_arr.length - 1); i++){
			
			_arr[i] = _arr[i+1];
			
		}
		
		_size--;
		
		
		System.out.print("\nVector size: " + _size + " (" + _arr.length + ")\n");
		
		if((_arr.length)/4 <= _size){	// If 1/4 full only
			
			System.out.print("\nResizing vector to " + (_arr.length/2) + " (from " + _arr.length + ")\n");
			
			Comparable arr[] = new Comparable[(_arr.length/2)];
			
			for(int i = 0; i < _size; i++)
				arr[i] = _arr[i];
			
			_arr = arr;
			
		}
		
	}

	@Override
	public Comparable get(int index) {
		
		System.out.print("\nGetting vector element with index: " + index + "\n");
		
		if((index > _size) || (index <= 0)){
			
			System.out.print("\nNo element with such index in vector...\n");
			return null;
		
		}
		
		return _arr[(index-1)];
		
	}

	@Override
	public int indexOf(Comparable o) {
		
		System.out.print("\nSearching in vector for element: \"" + o.toString() + "\" ...\n");
		
		for(int i = 0; i < _size; i++){
			
			if(o.compareTo(_arr[i]) == 0)
				return (i + 1);
			
		}
		
		System.out.print("\nNo such element in vector...\n");
		
		return -1;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws java.io.IOException {
		
		System.out.println("Main Start\n");
		
		SortedVector vec = new SortedVector(10);
		
		Comparable o1 = "abc";
		
		vec.show();
		
		vec.add(o1);
		
		vec.show();
		
		Comparable o2 = "ghi";
		
		vec.add(o2);
		
		vec.show();
		
		Comparable o3 = "def";
		
		vec.add(o3);
		
		vec.show();
		
		vec.remove(0);
		
		vec.show();
		
		vec.remove(1);
		
		vec.show();
		
		Comparable o4 = "qwe";
		
		vec.add(o4);
		
		vec.show();
		
		Comparable o5 = "rty";
		
		vec.add(o5);
		
		vec.show();
		
		Comparable o6 = "uio";
		
		vec.add(o6);
		
		vec.show();
		
		Comparable o1_ret = vec.get(2);
		
		if(o1_ret != null)
			System.out.print("\nGot [" + 2 + "] element: \"" + o1_ret.toString() + "\"\n");
		
		Comparable o2_ret = vec.get(0);
		
		if(o2_ret != null)
			System.out.print("\nGot [" + 2 + "] element: \"" + o2_ret.toString() + "\"\n");
		
		Comparable o1_ind = "qwe";
		int index1 = vec.indexOf(o1_ind);
		
		if(index1 > 0)
			System.out.print("\nIndex of element \"" + o1_ind.toString() + "\" is: " + index1 + "\n");
		
		Comparable o2_ind = "abc";
		int index2 = vec.indexOf(o2_ind);
		
		if(index2 > 0)
			System.out.print("\nIndex of element \"" + o2_ind.toString() + "\" is: " + index2 + "\n");
		
		
		System.out.println("\nMain End");
		
	}

}
