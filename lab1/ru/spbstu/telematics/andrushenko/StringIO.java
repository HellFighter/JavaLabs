package ru.spbstu.telematics.andrushenko;

import java.io.*;

public class StringIO{

    StringBuilder result = new StringBuilder();

    public static void main(String args []) throws java.io.IOException {
	
        System.out.println("Main Start");
	
	StringIO strIO = new StringIO();
	strIO.read();
	strIO.show();
	
	System.out.println("Main End");	

    }
    
    void read() {
    
	char ch = '0';
	
	while(ch != '}'){
	
	    try{
		ch = (char)System.in.read();
	    }
	    catch(IOException e){
		System.out.println("\nIO Exception!!!\n");
	    }
	
	    if(((ch <= '0') || (ch >= '9')) && (ch != '}') && (ch != 10)){
		result.append(ch);
	    }
	    
	}	
	
    }
    
    void show(){
    
	System.out.println("\n\tResult:");
	System.out.println("\t\"" + result + "\"\n");
	
    }

}