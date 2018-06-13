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
	String speedclass = "";
	
	public int speedclassindex = -1;
	public int besclassindex = -1;
	int maxmb = -1;

	String dataset_name = "";
	
	public boolean displayDate = false;
	
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
	 * The initial step of the dataset compilation process:
	 * 1. Read all the input values from the GUI
	 * 2. Set up a log file to print relevant data in
	 * 3. Test critical systems to check for bugs
	 * 4. Scan the 
	 * @param i 
	 * @param string 
	 * 
	 */
	public void preProcessStep(String inputfolder, String outputfolder, String besclass, int besclassindex, String maxmb, String name, String interval, String spread, ParamFrame frame, String speedclass, int speedclassindex) {
		this.inputfolder = inputfolder;
		this.outputfolder = outputfolder;
		this.besclass = besclass;
		this.besclassindex = besclassindex;
		this.speedclass = speedclass;
		this.speedclassindex = speedclassindex;
		this.maxmb = Integer.parseInt((String) maxmb);
		this.dataset_name = name;
		this.days = Integer.parseInt(interval);
		this.spread = Double.parseDouble(spread);
		
		
		//Setup log
		log = new Log(new File(outputfolder), "mainlog.txt", true);
		
		
		//check program integrity
		log.reportln("-------------- Program integrity check --------------");
		fileCheck(inputfolder, log);
		Test.testCriticalSystems(log, inputfolder);
		log.reportln("-----------------------------------------------------");

		
		//scan present corridors
		File corridorfile = new File(inputfolder + "\\corridors.txt");
		FileLineIterator lines = new FileLineIterator(corridorfile, FileLineIterator.BYPASS_HEADER);
		while(lines.pop()){		
			String line = lines.getLine();
			
			corridors.add(line);
			log.reportln("Adding Corridor: " + line);
		}

	}
	


	




	/**
	 * Prepares the second panel and its checkboxes
	 * @param bispanel
	 */
	public void selectParameters(BISMainPanel bispanel) {		
		bisbase = new BISBase(inputfolder);
		bispanel.prepareTask(bisbase, inputfolder);		
		
	}
	
	
	




	public void setupDataset(MESPanel mespanel) {
		dataset = new Dataset(this, inputfolder, outputfolder, bisbase, mespanel, maxmb, dataset_name);
	}
	
	
	public void compileDataset(JProgressBar progressBar, ParamFrame frame) {
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
