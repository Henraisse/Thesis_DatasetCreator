package bis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import classifier.Log;
import gui.BISMainPanel;
import gui.MESPanel;
import gui.ParamFrame;
import structs.Dataset;
import test.Test;
import util.FileLineIterator;

/**
 * 
 * The outward wrapper of the dataset compiler class. This is what the program GUI interacts with in order to
 * perpetuate the compilation process.
 * 
 * @author Henrik Rönnholm
 *
 */
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
		//Read fields an values from the GUI
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
		
		
		//Setup the log (system.out.print along with a written text file in the output folder)
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
	 * Prepares the second panel and its checkboxes along with the bisbase
	 * @param bispanel
	 */
	public void selectParameters(BISMainPanel bispanel) {		
		bisbase = new BISBase(inputfolder);
		bispanel.prepareTask(bisbase, inputfolder);		
		
	}
	
	

	/**
	 * Create the DataSet(the inner wrapper) and prepare for compilation 
	 * @param mespanel
	 */
	public void setupDataset(MESPanel mespanel) {
		dataset = new Dataset(this, inputfolder, outputfolder, bisbase, mespanel, maxmb, dataset_name, days, (int)(days*spread));
	}
	
	
	
	/**
	 * Compiles the dataset.
	 * @param progressBar
	 * @param frame
	 */
	public void compileDataset(JProgressBar progressBar, ParamFrame frame) {
		dataset.build(progressBar, frame);
	}	
	
	
	
	/**
	 * Sets the logfile output folder
	 * @param outputfilefolder
	 */
	public void setLogDestination(String outputfilefolder) {
		log.setDestination(outputfilefolder);
	}
	
	
	
	/**
	 * Performs a file check to make sure everything is present for the compilation.
	 * @param inputFolder
	 * @param log
	 */
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
			//if something is amiss, display an error message and terminate the program.
			JOptionPane.showMessageDialog(null, errorMsg, "Critical files missing", JOptionPane.ERROR_MESSAGE);
			log.reportln(errorMsg);
			log.reportln("Encountered an error, terminating process.");
			log.close();
			System.exit(0);
		}else {
			log.reportln("Data files and folders present --- (OK)");
		}
	}





	

}
