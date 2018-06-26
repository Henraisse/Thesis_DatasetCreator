package classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import structs.Date;

/**
 * Corresponds to a measurement file.
 * 
 * @author Henrik Ronnholm
 *
 */
public class MeasurementFile {

	public static final int FIELD_LENGTH = 48;
	
	File file;
	

	/**
	 * Constructor.
	 * @param measurementFile
	 */
	public MeasurementFile(File measurementFile) {
		this.file = measurementFile;
		if(file == null || !file.exists()) {
			System.err.println("Measurement file doesn't exist! : " + file.getAbsolutePath() );
		}
	}
	

	
	/**
	 * Checks that the measurement file has the correct format. This is currently only a check to see that there are 48 fields in every line, and checking that it is a .csv-file.
	 * @return
	 */
	public boolean hasCorrectFormat(MFileAnalyzer alzr) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			br.close();
			
			alzr.countHeader(line);
			
			
			String[] fields = line.split(";");
			if(!(fields.length == FIELD_LENGTH)) {
				//alzr.specialLog.reportln("FILE DENIED DUE TO NOT BEING CORRECT LENGTH! LENGTH = " + fields.length);
				return false;
			}
						
			@SuppressWarnings("unused")
			String fieldname = "";
			@SuppressWarnings("unused")
			int index = -1;
			boolean ret = true;
			if(!fields[9].equals("Höjd kortvåg vänster (null)") && ret){
				fieldname = "Höjd kortvåg vänster (null)";
				ret = false;
				index = 9;
			}
			if(!fields[10].equals("Höjd mellanvåg vänster (null)") && ret){
				fieldname = "Höjd mellanvåg vänster (null)";
				ret = false;
				index = 10;
			}
			if(!fields[12].equals("Höjd kortvåg höger (null)") && ret){
				fieldname = "Höjd kortvåg höger (null)";
				ret = false;
				index = 12;
			}
			if(!fields[13].equals("Höjd mellanvåg höger (null)") && ret){
				fieldname = "Höjd mellanvåg höger (null)";
				ret = false;
				index = 13;
			}			
			if(!fields[15].equals("Sido kortvåg vänster (null)") && ret){
				fieldname = "Sido kortvåg vänster (null)";
				ret = false;
				index = 15;
			}
			if(!fields[19].equals("Sido kortvåg höger (null)") && ret){
				fieldname = "Sido kortvåg höger (null)";
				ret = false;
				index = 19;
			}
			if(!fields[22].equals("Spårvidd (null)") && ret){
				fieldname = "Spårvidd (null)";
				ret = false;
				index = 22;
			}
			if(!fields[23].equals("Rälsförhöjning (null)") && ret){
				fieldname = "Rälsförhöjning (null)";
				ret = false;
				index = 23;
			}
			if(!fields[29].equals("Skevning 6m bas (null)") && ret){
				fieldname = "Skevning 6m bas (null)";
				ret = false;
				index = 29;
			}
			if(!fields[30].equals("Std höjd (null)") && ret){
				fieldname = "Std höjd (null)";
				ret = false;
				index = 30;
			}
			if(!fields[33].equals("Skevning 3m bas (null)") && ret){
				fieldname = "Skevning 3m bas (null)";
				ret = false;
				index = 33;
			}
			if(!fields[46].equals("Std sida (null)") && ret){
				fieldname = "Std sida (null)";
				ret = false;
				index = 45;
			}
			
			
			if(ret == false) {
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


	
	/**
	 * Checks whether a corridor name matches this file.
	 * @param corridor_name
	 * @return
	 */
	public boolean matchesCorridor(String corridor_name) {
		String[] fields = file.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");
		String corridor = fields[2];			
		if(!corridor_name.equals(corridor)) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Checks if this file is within a given date interval.
	 * @param reparation_start_date
	 * @param reparation_end_date
	 * @return
	 */
	public boolean isWithinDateRange(Date reparation_start_date, Date reparation_end_date) {
		String[] fields = file.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");	
																
		int year = Integer.parseInt(fields[1].substring(0, 2));
		int month = Integer.parseInt(fields[1].substring(2, 4));
		int day = Integer.parseInt(fields[1].substring(4, 6));
		
		Date date = new Date(year, month, day);		
		return date.isBetween(reparation_start_date, reparation_end_date);
	}


	
	/**
	 * Returns a File object of this file.
	 * @return
	 */
	public File getFile() {
		return file;
	}

}
