package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for wrapping the line iteration. Pops each line in turn.
 * 
 * @author Henrik Ronnholm
 *
 */
public class FileLineIterator {

	public static final int INCLUDE_HEADER = 0;
	public static final int BYPASS_HEADER = 1;
	int setting = -1;
	
	File file;
	
	BufferedReader br;
	
	private String header = "";
	String currentString = "";
	
	
	
	/**
	 * Constructor.
	 * @param file
	 * @param headerChoice
	 */
	public FileLineIterator(File file, int headerChoice) {
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
	
	
	
	/**
	 * Pops a line from the file iterator. returns a boolean denoting if the popping succeeded or not.
	 * @return
	 */
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
	
	
	
	/**
	 * Returns the last popped line.
	 * @return
	 */
	public String getLine() {
		return currentString;
	}
	
	
	
	/**
	 * Returns the header line of the current file.
	 * @return
	 */
	public String getHeaderLine() {
		if(header == null)return "";
		return header;
	}
	
	
	
	/**
	 * Close the FileLineIterator object.
	 */
	public void close() {
		try {
			br.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
}
