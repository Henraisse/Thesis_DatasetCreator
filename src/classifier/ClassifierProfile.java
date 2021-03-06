package classifier;

import java.io.File;
import java.util.ArrayList;
import classification.InvalidDataPointException;
import structs.Date;
import util.FileLineIterator;
import util.Util;

/**
 * Class used to classify measurement file lines. That is, deciding if they are valid and labelling them according to a UH-limit.
 * 
 * @author Henrik Ronnholm
 *
 */
public class ClassifierProfile {

	String corridor_name;
	
	Corridor corridor;	
	Log log;
	
			
	ArrayList<MeasurementFile> measurementFiles = new ArrayList<MeasurementFile>();
	ArrayList<RepairFile> repairFiles = new ArrayList<RepairFile>();
	
	Date reparation_start_date;
	Date reparation_end_date;

	MFileAnalyzer alzr;
	
	int minMeasDays = 60;
	int maxMeasDays = 8;
	
	/**
	 * Constructor.
	 * @param corridor_name
	 * @param rep_start
	 * @param rep_end
	 * @param folder
	 * @param log
	 * @param specialLog 
	 */
	public ClassifierProfile(String corridor_name, Date rep_start, Date rep_end, File folder, Log log, int mDays, int mDaysOffset, MFileAnalyzer alzr) {		
		this.corridor_name = corridor_name;
		corridor = new Corridor(this);
		reparation_start_date = rep_start;
		reparation_end_date = rep_end;
		
		this.log = log;
		
		this.alzr = alzr;
		
		minMeasDays = mDays - mDaysOffset;
		maxMeasDays = mDays + mDaysOffset;

		
		File measurementFolder = new File(folder + "\\measurements\\");
		File repairFolder = new File(folder + "\\repairs\\");
		
		insertMeasurementFiles(measurementFolder);
		insertRepairFiles(repairFolder);
		processInsertedFiles();		
	}


	
	
	/**
	 * Given a folder containing measurement files, this filters out the correct and valid measurement files and remembers them for later use.
	 * It starts by iterating through the entire list of measurement files, ignoring files with wrong corridor, wrong dimensions, or wrong dates.
	 * The rest are stored in an arraylist within this object.
	 * NOTE! This method assumes that all files that are within the measurement file folder are nothing but measurement files!
	 * @param measurementFolder
	 */
	public void insertMeasurementFiles(File measurementFolder) {
		int c = 0;
		for (final File measurementFile : measurementFolder.listFiles()) { 									// iterate through every measurement file available in folder
			MeasurementFile file = new MeasurementFile(measurementFile);
			
			boolean is_correct_format = file.hasCorrectFormat(alzr);										//check if the file has the correct format
			boolean is_correct_corridor = file.matchesCorridor(corridor_name);									//check if the file is the same corridor
			boolean is_correct_date = file.isWithinDateRange(reparation_start_date, reparation_end_date);		//check if the file is within the given global repair interval
			
			if(is_correct_format && is_correct_corridor && is_correct_date) {									//if all these are true, add the measurement file to the list
				c++;
				measurementFiles.add(file);
			}
		}
		log.report("Adding measurement files for corridor " + corridor_name + "... ");
		log.reportln("Added " + c + " files.");
	}
	
	
	
	
	/**
	 * Given a folder containing repair files, this inserts all of them into this object for later use.
	 * @param repairFolder
	 */
	public void insertRepairFiles(File repairFolder) {
		int c = 0;

		for (final File repfile : repairFolder.listFiles()) {// iterate through every measurement file available in folder	
			repairFiles.add(new RepairFile(repfile));			
			c++;
		}
		log.report("Adding repair files for corridor " + corridor_name + "... ");
		log.reportln("Added " + c + " files.");

	}

	
	
	/**
	 * Processes the inserted files. Iterates through every measurement file, and each of its lines and create a measurement history from them.
	 * When all measurement files have been processed, it processes the repair files to create repair history.
	 * This allows the subsequent compilation step to have each measurement line segments history readily available at all times.
	 */
	public void processInsertedFiles() {		
		int cme = 0;
		int cre = 0;

		log.report("Reading measurement events for corridor " + corridor_name + "... ");
		for(MeasurementFile msrfile : measurementFiles) {			
			FileLineIterator lines = new FileLineIterator(msrfile.getFile(), FileLineIterator.BYPASS_HEADER);		
			String[] filename_fields = msrfile.getFile().getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");		//tokenize the file name to use its info
			
	    	int year = Integer.parseInt(filename_fields[1].substring(0, 2));
	    	int month = Integer.parseInt(filename_fields[1].substring(2, 4));
	    	int day = Integer.parseInt(filename_fields[1].substring(4, 6));
			
	    	Date date = new Date(year, month, day);
	    		    	
			while(lines.pop()){
				String line = lines.getLine();
				String[] line_fields = line.split(";");
						
				boolean valid = checkLineValidity(line_fields);
				if(!valid) {continue;}
				
				String track = Util.getMeasurementLineTrack(line);
				int km = Util.getMeasurementLineKm(line);
				double m = Util.getMeasurementLineM(line);
		
				
				//retrieve the given segment
				if(km < 0 || m > 1000.0) {continue;}
				Segment segment = corridor.getSegment(track, km, m);
				segment.storeMeasurementEvent(date, line_fields);	
				cme++;
			}	
		}
		
		
		log.reportln("Added " + cme + " events.");
		log.report("Adding repair events for corridor " + corridor_name + "... ");
		for(RepairFile repfile : repairFiles) {
			FileLineIterator lines = new FileLineIterator(repfile.getFile(), FileLineIterator.BYPASS_HEADER);
			
			while(lines.pop()){
				String line = lines.getLine();
				String[] fields = line.split(";");
				
				String corridor_name = fields[0];			
				
				if(!corridor_name.equals(this.corridor_name)) {
					continue;
				}
				
				int km0 = Integer.parseInt(fields[1].split(" \\+ ")[0]);
				double m0 = Double.parseDouble(fields[1].split(" \\+ ")[1].replaceAll(" m","").replaceAll(",[\\d]", ""));	
				int km1 = Integer.parseInt(fields[2].split(" \\+ ")[0]);
				double m1 = Double.parseDouble(fields[2].split(" \\+ ")[1].replaceAll(" m","").replaceAll(",[\\d]", ""));
					
				String track = fields[3];
				
				int day = 1;				
				int month = Util.getRepairMonth(fields[7]);
				int year = Util.getRepairYear(fields[7]);
				Date date = new Date(year, month, day);

				int kmx = km0;
				double mx = m0;
				
				while(!(kmx > km1 || (kmx == km1 && mx >= m1))) {
					
					Segment segment = corridor.getSegment(track, kmx, mx);
					segment.storeRepairEvent(date);
					cre++;
					mx += 0.25;
					if(mx >= 1000.0) {
						mx = 0;
						kmx += 1;
					}				
				}
			}
		}
		log.reportln("Added " + cre + " events.");
	}
	
	
	

	/**
	 * check whether a line is valid. That means checking if any of the mandatory fields are corrupted. A valid line needs all its field uncorrupted.
	 * @param line
	 * @return
	 */
	public boolean checkLineValidity(String[] line){
		if(line.length < 47) {return false;}
		//Define the mandatory field indices
		int[] checkIndices = new int[] {9, 10, 12, 13, 15, 19, 22, 23, 29, 30, 33, 45};

		//Iterate through every index of the list
		for(int i = 0; i < checkIndices.length; i++) {
			//open the corresponding line field and check the value
			int index = checkIndices[i];
			String field = line[index];
			double value = 0;
			try{
				value = Double.parseDouble(field);
			}catch(NumberFormatException e) {
				return false;
			}
			
			
			//If it is zero, return "FALSE" immediately
			if(value == 0.0) {
				return false;
			}
		}
		//If we iterate through all the mandatory fields without returning, it means we succeeded. return true.
		return true;
	}


	/**
	 * Retrieve the class label string for a given segment.
	 * @param date
	 * @param track
	 * @param km
	 * @param m
	 * @return
	 * @throws InvalidDataPointException
	 */
	public String[] getClassLabel(Date date, String track, int km, double m, int speedlevel) throws InvalidDataPointException {
		//Identify which segment it is, and ask it for its classification string
		Segment segment = corridor.getSegment(track, km, m);
		return segment.getClassificationString(date, maxMeasDays, minMeasDays, speedlevel);
	}
	
	
	
	/**
	 * Returns the list of VALID measurement files corresponding to this corridor.
	 * @return
	 */
	public ArrayList<MeasurementFile> getMeasurementFiles() {
		return measurementFiles;
	}
	
	
}
