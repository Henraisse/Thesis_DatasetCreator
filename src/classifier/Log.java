package classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.Util;

public class Log {

	//Add a stringbuilder to make able to print on the same line
	
	File folder;
	File textfile;
	PrintWriter writer;
	
	boolean sysprint;
	boolean ok;
	boolean destinationSet;
	
	String destination = "";
	
	public Log(File folder, String name, boolean alsoPrint) {
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
		
		displayWelcomeMsg();
	}
	
	public void report(String text) {
		writer.print(text);
		if(sysprint) { 
			System.out.print(text);
		}		
	}
	
	public void reportln(String text) {
		writer.println(text);
		if(sysprint) { 
			System.out.println(text);
		}		
	}
	
	public void close() {
		writer.close();
		if(destinationSet) {
			Util.moveFile(textfile, destination);
		}
	}

	
	public boolean isOK() {
		return ok;
	}

	public void setDestination(String dest) {
		destinationSet = true;
		destination = dest;		
	}
	
	
	//*************
	
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
