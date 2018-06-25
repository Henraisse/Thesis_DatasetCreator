package classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import util.Util;



/**
 * Log file writer. An easy-to-use log that writes to a text file.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Log {

	File folder;
	File textfile;
	PrintWriter writer;
	
	boolean sysprint;
	boolean ok;
	boolean destinationSet;
	
	String destination = "";
	
	
	/**
	 * Constructor.
	 * @param folder
	 * @param name
	 * @param alsoPrint
	 */
	public Log(File folder, String name, boolean alsoPrint, boolean printHeader) {
		sysprint = alsoPrint;		
		this.folder = folder;
		folder.mkdirs();
		
		
		try {
			writer = new PrintWriter(folder.getAbsolutePath() + "\\" + name, "UTF-8");
			textfile = new File(folder.getAbsolutePath() + "\\" + name);
			ok = true;
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
			ok = false;
		} 	
		
		if(printHeader) {
			displayWelcomeMsg();
		}
		
	}
	
	
	
	/**
	 * Appends a string to the log file.
	 * @param text
	 */
	public void report(String text) {
		writer.print(text);
		if(sysprint) { 
			System.out.print(text);
		}		
	}
	
	
	
	/**
	 * Appends a string to the log file, includes new line.
	 * @param text
	 */
	public void reportln(String text) {
		writer.println(text);
		if(sysprint) { 
			System.out.println(text);
		}		
	}
	
	
	
	/**
	 * Closes the log and finalizes the log file.
	 */
	public void close() {
		writer.close();
		if(destinationSet) {
			Util.moveFile(textfile, destination);
		}
	}

	
	
	/**
	 * Returns true if the log is OK and ready to use, otherwise false.
	 * @return
	 */
	public boolean isOK() {
		return ok;
	}

	
	
	/**
	 * Set the destination of the log file.
	 * @param dest
	 */
	public void setDestination(String dest) {
		destinationSet = true;
		destination = dest;		
	}
	
	

	/**
	 * Display a welcome message for the SPL Dataset Compiler.
	 */
	public void displayWelcomeMsg() {
		reportln("");
		reportln("######################################################################################################################################################");
		reportln("######################################################################################################################################################");
		reportln("#                   _____ _____  _            _____        _                 _          _____                      _ _           		     #");
		reportln("#		   / ____|  __ \\| |          |  __ \\      | |               | |        / ____|                    (_) |          		     #");
		reportln("#		  | (___ | |__) | |          | |  | | __ _| |_ __ _ ___  ___| |_      | |     ___  _ __ ___  _ __  _| | ___ _ __ 		     #");
		reportln("#		   \\___ \\|  ___/| |          | |  | |/ _` | __/ _` / __|/ _ \\ __|     | |    / _ \\| '_ ` _ \\| '_ \\| | |/ _ \\ '__|		     #");
		reportln("#		   ____) | |    | |____      | |__| | (_| | || (_| \\__ \\  __/ |_      | |___| (_) | | | | | | |_) | | |  __/ |   		     #");
		reportln("#		  |_____/|_|    |______|     |_____/ \\__,_|\\__\\__,_|___/\\___|\\__|      \\_____\\___/|_| |_| |_| .__/|_|_|\\___|_|   		     #");
		reportln("#		                                                                                            | |                  		     #");
		reportln("#		                                                                                            |_|               			     #");   
		reportln("######################################################################################################################################################");
		reportln("######################################################################################################################################################");
		reportln("");
		reportln("Welcome to SPL Dataset Compiler! This program is aimed at compiling a usable machine learning dataset from rail data.");
		reportln("This program is a work-in-progress, and is constructed for my thesis, so no guarantees or warranty at this point I'm afraid.");
		reportln("");
	}
}
