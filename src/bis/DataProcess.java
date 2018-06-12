package bis;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import classifier.Log;
import gui.BISMainPanel;
import gui.MESPanel;
import gui.ParamFrame;
import structs.Dataset;
import test.Test;
import util.FileLineIterator;
import util.Util;

public class DataProcess{

	
	
	//------------- Panel 1 data ---------------
	String inputfolder = "";
	String outputfolder = "";	
	String besclass = "";
	
	int besclassindex = -1;
	int maxmb = -1;

	String dataset_name = "";
	
	public int days = 100;
	public double spread = 0.30;
	
	//------------- Panel 2 data ---------------
	BISBase bisbase;
	
	
	//------------- Panel 3 data ---------------
	Dataset dataset;
	
	public Log log;
	public ArrayList<String> corridors = new ArrayList<String>(); 
	
	public DataProcess() {
		
	}
	
	
	
	
	/**
	 * Retrieves all the selected values from the first panel.
	 * @param name 
	 */
	public void processStep0(String inputfolder, String outputfolder, String besclass, int besclassindex, String maxmb, String name, String interval, String spread, ParamFrame frame) {
		this.inputfolder = inputfolder;
		this.outputfolder = outputfolder;
		this.besclass = besclass;
		this.besclassindex = besclassindex;
		this.maxmb = Integer.parseInt((String) maxmb);
		this.dataset_name = name;
		this.days = Integer.parseInt(interval);
		this.spread = Double.parseDouble(spread);
		
		
		
		

		log = new Log(new File(outputfolder), "mainlog.txt", true);

		log.reportln("");
		log.reportln("######################################################################################################################################################");
		log.reportln("######################################################################################################################################################");
		log.reportln("#                   _____ _____  _            _____        _                 _          _____                      _ _           		     #");
		log.reportln("#		   / ____|  __ \\| |          |  __ \\      | |               | |        / ____|                    (_) |          		     #");
		log.reportln("#		  | (___ | |__) | |          | |  | | __ _| |_ __ _ ___  ___| |_      | |     ___  _ __ ___  _ __  _| | ___ _ __ 		     #");
		log.reportln("#		   \\___ \\|  ___/| |          | |  | |/ _` | __/ _` / __|/ _ \\ __|     | |    / _ \\| '_ ` _ \\| '_ \\| | |/ _ \\ '__|		     #");
		log.reportln("#		   ____) | |    | |____      | |__| | (_| | || (_| \\__ \\  __/ |_      | |___| (_) | | | | | | |_) | | |  __/ |   		     #");
		log.reportln("#		  |_____/|_|    |______|     |_____/ \\__,_|\\__\\__,_|___/\\___|\\__|      \\_____\\___/|_| |_| |_| .__/|_|_|\\___|_|   		     #");
		log.reportln("#		                                                                                            | |                  		     #");
		log.reportln("#		                                                                                            |_|               			     #");   
		log.reportln("######################################################################################################################################################");
		log.reportln("######################################################################################################################################################");
		log.reportln("");
		log.reportln("Welcome to SPL Dataset Compiler! This program is aimed at compiling a usable machine learning dataset from rail data.");
		log.reportln("This program is a work-in-progress, and is constructed for my thesis, so no guarantees or warranty at this point I'm afraid.");
		log.reportln("");
		log.reportln("");
		log.reportln("-------------- Program integrity check --------------");
		fileCheck(inputfolder, log);
		Test.testCriticalSystems(log, inputfolder);
		log.reportln("-----------------------------------------------------");
		log.reportln("");
		log.reportln("");
		log.reportln("######################################################################################################################################################");
		scanPresentCorridors();

		
	

	}
	






	



	
	
	
	
	
	/**
	 * Prepares the second panel and its checkboxes
	 * @param bispanel
	 */
	public void processStep1(BISMainPanel bispanel) {		
		bisbase = new BISBase(inputfolder);
		bispanel.prepareTask(bisbase, inputfolder);		
		
	}
	
	
	


	/**
	 * Read the correct values from the checkboxes
	 */
	public void processStep2() {
		
	}
	
	
	
	
	
	private void scanPresentCorridors() {
		File corridorfile = new File(inputfolder + "\\corridors.txt");
		FileLineIterator lines = new FileLineIterator(corridorfile, FileLineIterator.BYPASS_HEADER);
		while(lines.pop()){		
			//Pop and tokenize each line
			String line = lines.getLine();
			
			corridors.add(line);
			log.reportln("Adding Corridor: " + line);
		}
	}




	public void processStep3(MESPanel mespanel) {
		dataset = new Dataset(this, inputfolder, outputfolder, bisbase, mespanel, maxmb, dataset_name);
	}
	
	
	public void processStep4(JProgressBar progressBar, ParamFrame frame) {
		dataset.build(progressBar, frame);
	}	
	
	
	
	
	
	private void fileCheck(String inputFolder, Log log) {	
		String errorMsg = "";
		boolean allWell = true;
			
		try(BufferedReader br = new BufferedReader(new FileReader(inputFolder + "\\checklist.txt"))) {
		    String line = br.readLine();

		    while (line != null) {
		    	
		    	//If line is empty, a comment, or starts with a whitespace
		    	if(line.equals("") || line.startsWith("#") || line.startsWith(" ")) {
		    		line = br.readLine();
		    		continue;
		    	}
		    		    		    	
		    	//if line starts with fo| it means it is supposed to be a folder
		    	if(line.startsWith("fo|")) {
		    		line = line.replace("fo|", "");
		    		
			    	File f = new File(inputFolder + "\\" + line);	
			    	if(!f.isDirectory()) {
			    		allWell = false;
			    		errorMsg = line + " isn't a directory!";
			    		break;
			    	}
			    	
			    	if(!f.exists()) {
			    		allWell = false;
			    		errorMsg = line + " is missing!";
			    		break;
			    	}
		    	}else {
		    		
		    		File f = new File(inputFolder + "\\" + line);		 
			    	if(f.isDirectory()) {
			    		allWell = false;
			    		errorMsg = line + " isn't a file!";
			    		break;
			    	}
			    	if(!f.exists()) {
			    		allWell = false;
			    		errorMsg = line + " is missing!";
			    		break;
			    	}
		    	}
		    	

		    	
    	
		        line = br.readLine();
		    }

		} catch (IOException e) {e.printStackTrace();}
		
		
		if(!allWell) {
			//"Not all required data files are available to the program. Cannot continue.", "Critical files missing"
			//custom title, error icon
			JOptionPane.showMessageDialog(null, errorMsg, "Critical files missing", JOptionPane.ERROR_MESSAGE);
			log.reportln(errorMsg);
			log.reportln("Encountered an error, terminating process.");
			log.close();
			System.exit(0);
		}else {
			log.reportln("Data files and folders present --- (OK)");
		}
	}




	public void setLogDestination(String outputfilefolder) {
		log.setDestination(outputfilefolder);
	}
	

}
