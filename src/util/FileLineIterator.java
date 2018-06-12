package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLineIterator {

	public static final int INCLUDE_HEADER = 0;
	public static final int BYPASS_HEADER = 1;
	int setting = -1;
	
	File file;
	
	BufferedReader br;
	
	private String header = "";
	String currentString = "";
	
	public FileLineIterator(File file, int headerChoice) {
		int i = 0;
		setting = headerChoice;
		try {									
			br = new BufferedReader(new FileReader(file));	
			if(headerChoice == BYPASS_HEADER) {
				header = br.readLine();	 
			}
																			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean pop() {
		try {
			currentString = br.readLine();
			if(currentString != null) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;		
	}
	
	
	public String getLine() {
		return currentString;
	}
	
	public String getHeaderLine() {
		if(header == null)return "";
		return header;
	}
	
	
	public void close() {
		try {
			br.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
}
