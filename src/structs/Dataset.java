package structs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import bis.BISBase;
import bis.BISProfile;
import bis.DataProcess;
import bis.DataWriter;
import classification.InvalidDataPointException;
import classifier.ClassifierProfile;
import classifier.MFileAnalyzer;
import classifier.MeasurementFile;
import classifier.Segment;
import gui.MESPanel;
import gui.ParamFrame;
import util.FileLineIterator;
import util.Util;


/**
 * The inner wrapper Class for creating the dataset.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Dataset implements PropertyChangeListener{
	String outputfolder;
	String inputfolder;
	String output_file_header = "";

	BISBase bisbase;	
	DataProcess dataprocess;
	JProgressBar progressBar;
	Task progress_task;
	ParamFrame frame;
	DataWriter writer;
	
	public MFileAnalyzer alzr;
	
	int[] measurementBits = new int[0];
	
	int days;
	int daysOffset;
	
	/**
	 * Constructs a Dataset object and assigns a name to the dataset, if no previous name has been given.
	 * @param dp
	 * @param inputfolder
	 * @param outputfolder
	 * @param bisbase
	 * @param mespanel
	 * @param maxmb
	 * @param name
	 */
	public Dataset(DataProcess dp, String inputfolder, String outputfolder, BISBase bisbase, MESPanel mespanel, int maxmb, String name, int days, int daysOffset)  {	
		this.dataprocess = dp;
		this.bisbase = bisbase;
		this.outputfolder = outputfolder;	
		this.inputfolder = inputfolder;

		this.days = days;
		this.daysOffset = daysOffset;
		
		measurementBits = mespanel.getMESHeaderBits();
		
		alzr = new MFileAnalyzer(outputfolder);
		
		//Define the output file header, which will be the first line in each file;
		output_file_header = mespanel.getMESHeaderString() + bisbase.getBISHeaderString() + Date.printSPLHeaderField() + "OFFSET_DAYS;PRE_CLASS;POST_CLASS";
	
		
		if(name.equals("")) {
			name = Util.getTimeStamp();
		} 
		
		String outputfilefolder =outputfolder + "\\" + name;
		
		writer = new DataWriter(outputfilefolder, maxmb, output_file_header);
		dp.setLogDestination(outputfilefolder);
		//-------------------------------------------
	}




	
	
	
	





	/**
	 * Processes all the corridor's measurement files and turns them into datapoints.
	 * @param corridor
	 */
	public void processCorridor(String corridor) {
		Date[] repinterval = Util.getGlobalRepairDateInterval(new File(inputfolder + "\\repairs\\"));
		dataprocess.log.reportln("-------------------------------------------------------------------------");
		dataprocess.log.reportln("|		PROCESSING CORRIDOR " + corridor + "				|");
		dataprocess.log.reportln("-------------------------------------------------------------------------");
	
		BISProfile bisprofile = new BISProfile(inputfolder, corridor, bisbase);	   	//create a bisprofile for this corridor			
		ClassifierProfile cp = new ClassifierProfile(corridor, repinterval[0], repinterval[1], new File(inputfolder), dataprocess.log, days, daysOffset, alzr);

		int success = 0;
		int empty = 0;
		int contamination = 0;
		int dirty = 0;
		int fail = 0;
		int invalid = 0;
		int islast = 0;

		
		dataprocess.log.reportln("Extracting dataset points for corridor " + corridor + " ...");
		for (final MeasurementFile measurement_file : cp.getMeasurementFiles()) {							//for file in the measurement folder    
			
			int[] ret = extractLines(measurement_file.getFile(), corridor, bisprofile, writer, cp);		
			
			success += ret[0];
			empty += ret[1];
			contamination += ret[2];
			dirty += ret[3];
			fail += ret[4];
			invalid += ret[5];
			islast += ret[6];
		}			
		dataprocess.log.reportln("-----------------------");
		dataprocess.log.reportln(success + " successful datapoints");
		dataprocess.log.reportln(islast + " lacking any subsequent measurement");
		dataprocess.log.reportln(empty + " lacking subsequent measurement within given interval");
		dataprocess.log.reportln(contamination + " contaminated by contemporary repair event");
		dataprocess.log.reportln(dirty + " with foreign position");
		dataprocess.log.reportln(invalid + " with invalid data line");
		dataprocess.log.reportln(fail + " other failed measurements");
		dataprocess.log.reportln("");
		int mb = writer.totalmbwritten;
		dataprocess.log.reportln(mb + " megabytes extracted from corridor.");
		dataprocess.log.reportln("-----------------------");
		dataprocess.log.reportln("");
		dataprocess.log.reportln("");
	}
	
	

	

	//#############################################################################################################################################################
	

	
	
	

	
	
	
	
	/**
	 * This method processes a file and turns it into input in the output files. It processes each line by itself, and writes them via a custom filewriter to the
	 * current output file (which is filled to an approximate MB-size).
	 * @param inputfile
	 * @param bisprofile
	 * @param writer
	 * @param classifier
	 */
	private int[] extractLines(File inputfile, String corridor, BISProfile bisprofile, DataWriter writer, ClassifierProfile classifier) {	
		dataprocess.log.reportln("Processing: " + inputfile.getName());
		//Count every successful and failed potential datapoint extracted from this measurement file
		int success = 0;
		int empty = 0;
		int contamination = 0;
		int dirty = 0;
		int fail = 0;
		int invalid = 0;
		int islast = 0;
		
		//Extract the date from the current file
		String[] fields = inputfile.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");		//Extract useful info from the measurement file name
		int year = Integer.parseInt(fields[1].substring(0, 2));
		int month = Integer.parseInt(fields[1].substring(2, 4));
		int day = Integer.parseInt(fields[1].substring(4, 6));			
		Date date = new Date(year, month, day);

		//Begin iterating through the file
		FileLineIterator lines = new FileLineIterator(inputfile, FileLineIterator.BYPASS_HEADER);				
		while(lines.pop()){		
			//Pop and tokenize each line
			String line = lines.getLine();
			String[] linefields = line.split(";");	
			
			//Try processing the line. If successful, append it to the datapoint writer and count it
			try {
				String datapoint = processMeasurementLine(linefields, bisprofile, classifier, date);
				writer.appendLine("\n" + datapoint);			
				success++;
				
			//If it fails somewhere along the process, see what went wrong and note the failure
			} catch (InvalidDataPointException e) {
				if(e.getMessage().equals("(NO MEASUREMENTS AVAILABLE)")) {empty++;}
				else if(e.getMessage().equals("(LAST MEASUREMENT)")) {islast++;}
				else if(e.getMessage().equals("(REPAIR ACTION CONTAMINATION)")) {contamination++;}
				else if(e.getMessage().equals("(OUTSIDE VALID CORRIDOR)")) {dirty++;}
				else if(e.getMessage().equals("(INVALID LINE)")) {invalid++;}
				else {
					fail++;
				}
			}								
		}
		
		
		//When the file has been completely processed, return the file statistics
		return new int[] {success, empty, contamination, dirty, fail, invalid, islast};
	}





	/**
	 * This method processes a specific measurement line.
	 * @param line
	 * @param bisprofile
	 * @param classifier
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @throws InvalidDataPointException 
	 */
	public String processMeasurementLine(String[] line, BISProfile bisprofile, ClassifierProfile classifier, Date date) throws InvalidDataPointException {	
		//start by making sure that the line is valid.
		boolean valid = classifier.checkLineValidity(line);
		if(!valid) {
			throw new InvalidDataPointException("(INVALID LINE)", new Exception());
		}
		//Define a stringbuilder for appending each part of the finished datapoint
		StringBuilder sb = new StringBuilder();	


		
		
		//Extract some useful data from the line
		String corridor = line[1];															
		String track = line[6];
		int km = Integer.parseInt(line[4]);
		double m = Double.parseDouble(line[5]);
		
		//If there is a negative kilometer or a meter offset more than 1000 meters, discard this datapoint. We cannot guarantee that it belongs.
		if(km < 0 || m > 1000.0 ) {
			throw new InvalidDataPointException("(OUTSIDE VALID CORRIDOR)", new Exception());
		}
		
		//Append the bis-data to the line
		String bislines = bisprofile.getSegmentBISProfileString(corridor, track, km, m);	

		String[] labels = classifier.getClassLabel(date, track, km, m, dataprocess.speedclassindex);
		
		String preClassLabel = Segment.classifyMeasurementLine(line)[dataprocess.speedclassindex] + ";";		
		String postClassLabel = labels[0];
		String daysToNext = labels[1];
		
		//---------- APPEND THE LINE -------------
		
		//For each marked measurement field bit, check for each measurement line field if it is included (bit=1), if it is, then append it to the stringbuilder.
		for(int i = 0; i < line.length; i++) {					
			String field = line[i];
			if(measurementBits[i] == 1) {													
				sb.append(field + ";");														
			}
		}
		sb.append(bislines);		
		sb.append(date.toStringSPL2());
		sb.append(daysToNext);		
		sb.append(preClassLabel);	
		sb.append(postClassLabel);	
				
		//Return the complete datapoint string
		return sb.toString();
	}







	
	//-----------------------------------------------------------------------------------------------------------------
	
	
	public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }
		
	
	
	/**
	 * Initiates the compilation process which creates the dataset.
	 * @param progressBar
	 * @param frame
	 */
	public void build(JProgressBar progressBar, ParamFrame frame) {
		this.frame = frame;
		this.progressBar = progressBar;
		
	    progress_task = new Task();
	    progress_task.addPropertyChangeListener(this);
	    progress_task.execute();	    		
	}


	
	/**
	 * This object is created and run when the compilation task of the dataset is set in motion.
	 * @author Henrik Ronnholm
	 *
	 */
	class Task extends SwingWorker<Void, Void> {
		
		double secsSinceStart = 0;
		int total = 0;
		int curr = 0;
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {     
        	try {
        		doWork();
        	}catch(Exception c) {
        		dataprocess.log.reportln("Exception caught! " + c.getMessage());
        		c.printStackTrace();
        	}
        	
        	return null;
        }
        
    	public void doWork() {
    		long starttime = System.currentTimeMillis();   		
    		total = dataprocess.corridors.size();
    		curr = 0;
    		  		
    		for(String c : dataprocess.corridors) {
    			processCorridor(c);
    			curr++;
    			double progress = 100.0 * ((double)curr / (double)total);   			
    			secsSinceStart = (double)(System.currentTimeMillis() - starttime)/1000.0;
    			setProgress((int)progress);
    			
    			SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					int time =  (int) (((secsSinceStart / progress) * 100.0) - secsSinceStart);
    					String et = "Elapsed time: " + (int)secsSinceStart + " seconds";
    					String t = "Estimated remaining time: " + time + " seconds";
    					frame.centerPanel3.elapsedtime.setText(et);
    					frame.centerPanel3.expectedtime.setText(t);
    				}
    			});
    		}
    		//alzr.display();
    		frame.finished();
    						
    		//=============== END =================
    		writer.closeWriteFile();	
    		dataprocess.log.report("");
    		dataprocess.log.report("Process complete.");
    		dataprocess.log.close();
    	}
        
    }
		


}
