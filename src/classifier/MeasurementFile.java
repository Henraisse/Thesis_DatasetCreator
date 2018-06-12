package classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import structs.Date;

public class MeasurementFile {

	public static final int FIELD_LENGTH = 48;
	
	File file;
	
	//###########################¤¤¤## CONSTRUCTOR ####################################################
	
	
	public MeasurementFile(File measurementFile) {
		this.file = measurementFile;
		if(file == null || !file.exists()) {
			System.err.println("Measurement file doesn't exist! : " + file.getAbsolutePath() );
		}
	}

	
	//############################# PUBLIC METHODS ####################################################
	
	
	/**
	 * Checks that the measurement file has the correct format. This is currently only a check to see that there are 48 fields in every line, and checking that it is a .csv-file.
	 * @return
	 */
	public boolean hasCorrectFormat() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			br.close();
			String[] fields = line.split(";");
			if(!(fields.length == FIELD_LENGTH)) {
				return false;
			}
		} catch (IOException e) {	
			e.printStackTrace();
			return false;
		}		
		
		String suffix = file.getName().split("\\.")[1];
		if(!suffix.equalsIgnoreCase("csv")) {
			return false;
		}
		
		//if we made it this far, everything is correct - return true;
		return true;		
	}


	
	public boolean matchesCorridor(String corridor_name) {
		String[] fields = file.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");
		String corridor = fields[2];			
		if(!corridor_name.equals(corridor)) {
			return false;
		}
		return true;
	}
	
	

	public boolean isWithinDateRange(Date reparation_start_date, Date reparation_end_date) {
		String[] fields = file.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");	
																
		int year = Integer.parseInt(fields[1].substring(0, 2));
		int month = Integer.parseInt(fields[1].substring(2, 4));
		int day = Integer.parseInt(fields[1].substring(4, 6));
		
		Date date = new Date(year, month, day);		
		return date.isBetween(reparation_start_date, reparation_end_date);
	}


	public File getFile() {
		return file;
	}

	
	//############################# PRIVATE METHODS ########¤¤¤¤#######################################
	
	
}
