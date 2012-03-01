package ru.spbstu.telematics.lab2;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedVector<T extends Comparable<T>> implements ISortedVector<T>, Iterable<T>, Iterator<T>{
	
	private T[] _arr;
	private int _size = 0;
	
	private int _count = 0;
	
	@SuppressWarnings("unchecked")
	SortedVector(int size){
		_arr = (T[]) new Comparable[size];
	}

	public void show(){
		
		if(_size > 0)
			System.out.println("\n\nIn vector: ");
		else
			System.out.println("\n\tNothing in vector...");
		
		for(int i = 0; i < _size; i++){
			System.out.print("\t" + (i+1) + ") " + _arr[i].toString() + "\n");
		}
		
		System.out.println("\n");
	}
	
	@Override
	public void add(T o){//Comparable o) {

		System.out.print("\nAdd in vector: " + o + "\n");
		
		int j;	
		for(j = 0; (j < _size) && (o.compareTo(_arr[j]) > 0); )
			j++;
		
		for(int i = j; i < _size; i++)
			_arr[i+1] = _arr[i];
		
		_arr[j] = o;
		
		_size++;
		
		
		System.out.print("\n\t\tVector size: " + _size + " (" + _arr.length + ")\n");
		
		if(_arr.length == _size){	// If no empty space
			
			System.out.print("\n\t\tResizing vector to " + (_arr.length*2) + " (from " + _arr.length + ")\n");
			
			@SuppressWarnings("unchecked")
			T[] arr = (T[]) new Comparable[(_arr.length*2)];
			
			for(j = 0; j < _size; j++)
				arr[j] = _arr[j];
			
			_arr = arr;
			
		}
		
	}

	@Override
	public void remove(int index) {
		
		System.out.print("\nRemove from vector element number: " + index + "\n");
		
		if((index > _size) || (index <= 0)){
			
			System.out.print("\n\tNo element with such index in vector: " + index + "\n");
			return;
		
		}
		
		for(int i = (index - 1); i < (_arr.length - 1); i++){
			
			_arr[i] = _arr[i+1];
			
		}
		
		_size--;
		
		
		System.out.print("\n\t\tVector size: " + _size + " (" + _arr.length + ")\n");
		
		if((_arr.length)/4 <= _size){	// If 1/4 full only
			
			System.out.print("\n\t\tResizing vector to " + (_arr.length/2) + " (from " + _arr.length + ")\n");
			
			@SuppressWarnings("unchecked")
			T arr[] = (T[]) new Comparable[(_arr.length/2)];
			
			for(int i = 0; i < _size; i++)
				arr[i] = _arr[i];
			
			_arr = arr;
			
		}
		
	}

	@Override
	public T get(int index) {
		
		System.out.print("\nGetting vector element with index: " + index + "\n");
		
		if((index > _size) || (index <= 0)){
			
			throw new IndexOutOfBoundsException("Element with given index does not exist");
		
		}
		
		return _arr[(index-1)];
		
	}

	@Override
	public int indexOf(T o) {
		
		System.out.print("\nSearching in vector for element: \"" + o.toString() + "\" ...\n");
		
		for(int i = 0; i < _size; i++){
			
			if(o.compareTo(_arr[i]) == 0)
				return (i + 1);
			
		}
		
		System.out.print("\n\tNo such element in vector...\n");
		
		return -1;
		
	}
	

	@Override
	public boolean hasNext() {
		if(_count < _size) return true; 
		return false;
	}

	@Override
	public T next() {
		if(_count == _size) 
			throw new NoSuchElementException(); 

			_count++; 
			return _arr[(_count-1)];
	}

	@Override
	public void remove() {
		//throw new UnsupportedOperationException();
		remove(_count);
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws java.io.IOException {
		
		System.out.println("Main Start\n");
		
		SortedVector<String> vec = new SortedVector<String>(10);
		
		String o1 = "abc";
		
		vec.show();
		
		vec.add(o1);
		
		vec.show();
		
		String o2 = "ghi";
		
		vec.add(o2);
		
		vec.show();
		
		String o3 = "def";
		
		vec.add(o3);
		
		vec.show();
		
		vec.remove(0);
		
		vec.show();
		
		vec.remove(1);
		
		vec.show();
		
		String o4 = "qwe";
		
		vec.add(o4);
		
		vec.show();
		
		String o5 = "rty";
		
		vec.add(o5);
		
		vec.show();
		
		String o6 = "uio";
		
		vec.add(o6);
		
		vec.show();
		
		try{
			System.out.print("\n\tGot: \"" + vec.get(2) + "\"\n");
		}
		catch(IndexOutOfBoundsException exception){
			System.out.print("\n\tGot exception: \"" + exception.getMessage() + "\"\n");
		}
		
		try{
			System.out.print("\n\tGot: \"" + vec.get(0) + "\"\n");
		}		
		catch(IndexOutOfBoundsException exception){
			System.out.print("\n\tGot exception: \"" + exception.getMessage() + "\"\n");
		}
		
		String o1_ind = "qwe";
		int index1 = vec.indexOf(o1_ind);
		
		if(index1 > 0)
			System.out.print("\n\tIndex of element \"" + o1_ind.toString() + "\" is: " + index1 + "\n");
		
		String o2_ind = "abc";
		int index2 = vec.indexOf(o2_ind);
		
		if(index2 > 0)
			System.out.print("\n\tIndex of element \"" + o2_ind.toString() + "\" is: " + index2 + "\n");
		
		System.out.println("\nfor-each:");
		for(String str : vec)
			System.out.print("\t\"" + str + "\"\n");
		
		System.out.println("\nMain End");
		
	}

}
