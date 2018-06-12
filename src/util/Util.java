package util;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import classifier.MeasurementFile;
import gui.ParamFrame;
import structs.Date;
import structs.Interval;

public class Util {







	public static ArrayList<Interval> get_intervals(File file, String corridor){
		ArrayList<Interval> intervals = new ArrayList<Interval>();		
		try {		
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();	 //Throw this line away.				

			line = br.readLine();			
			while (line != null) {

				String[] fields = line.split(";");  
				String bandel_name = fields[0];

				//System.out.println("comparing '" + corridor + "'=?'" + bandel_name + "'");

				if(bandel_name.equals(corridor)) {
					Interval interval = null;
					
					if(fields.length >= 9) {
						interval = new Interval(fields);					
						intervals.add(interval);
					}
					
				}
				line = br.readLine();				
			}	
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Done");
		return intervals;
	}






	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}






	public static String[] get_headers(File file) {

		String[] ret = new String[1];
		try {		
			BufferedReader br = new BufferedReader(new FileReader(file));
			ret = br.readLine().split(";");	 //Throw this line away.				

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}



	public static void setLookAndFeel(ParamFrame pf) {
		try {

			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			SwingUtilities.updateComponentTreeUI(pf);
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
	}






	public static String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		return sdf.format(new Timestamp(System.currentTimeMillis()));
	}










	public static boolean isAfterDate(int year, int month, int day, int curr_year, int curr_month, int curr_day) {
		if(year > curr_year) {
			return true;
		}else if(year == curr_year) {
			if(month > curr_month) {
				return true;
			}else if(month == curr_month) {
				if(day > curr_day) {
					return true;
				}
			}
		}
		return false;
	}











	public static void deleteFolderAndContents(File folder) {
		File[] files = folder.listFiles();
		if(files!=null) { //some JVMs return null for empty dirs
			for(File f: files) {
				if(f.isDirectory()) {
					deleteFolderAndContents(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	public static void createFolder(String path) {
		new File(path).mkdirs();
	}

	public static void makeFileInDirs(File file) throws IOException {
		if(!file.exists()) {
			new File(file.getParentFile().getPath()).mkdirs();
			file.createNewFile();
		}
	}
	
	public static void writeSingleLine(File file, String line) throws IOException {
		FileWriter writer = new FileWriter(file, true);
		writer.write(line + "\n");
		writer.close();
	}


	public static int getMeasurementLineKm(String line) {
		int ret = Integer.parseInt(line.split(";")[4]);
		return ret;
	}

	public static double getMeasurementLineM(String line) {			
		double ret = Double.parseDouble(line.split(";")[5]);
		return ret;
	}

	public static String getMeasurementLineTrack(String line) {	
		String ret = line.split(";")[6];
		return ret;
	}

	//########################################################################################################################################################################################
	//######################################################################## LABEL CLASSIFICATION ##########################################################################################
	//########################################################################################################################################################################################





	/**
	 * Returns the biggest of the two bytes, favouring the first one in a tie.
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static byte max(Byte b0, Byte b1) {
		if(b0 >= b1) {return b0;}
		return b1;
	}






	private static String printArray(byte[] output) {
		String ret = "{ " + output[0] + " " + output[1] + " " + output[2] + " " + output[3] + " " + output[4] + " " + output[5] + " }";
		return ret;
	}













	









	public static int getRepairMonth(String string) {
		String month = string.split("-")[1];
		return Integer.parseInt(month);
	}
	
	public static int getRepairYear(String string) {
		String year = string.split("-")[0];
		year = year.substring(2, 4);
		if(year.length() > 2) {
			System.err.println("Getting repair year with more than two digits! Fail!");
			System.exit(0);
		}
		return Integer.parseInt(year);
	}
	
	
//	public static int getRepairMonthOLD(String string) {
//		String month = string.split("-")[0];
//
//		if(month.equals("Jan")) {return 1;}
//		if(month.equals("Feb")) {return 2;}
//		if(month.equals("Mar")) {return 3;}
//		if(month.equals("Apr")) {return 4;}
//		if(month.equals("May")) {return 5;}
//		if(month.equals("Jun")) {return 6;}
//		if(month.equals("Jul")) {return 7;}
//		if(month.equals("Aug")) {return 8;}
//		if(month.equals("Sep")) {return 9;}
//		if(month.equals("Oct")) {return 10;}
//		if(month.equals("Nov")) {return 11;}
//		if(month.equals("Dec")) {return 12;}
//
//		return -1;
//	}

//	public static int getRepairYearOLD(String string) {
//		String year = string.split("-")[1];
//		if(year.length() > 2) {
//			System.err.println("Getting repair year with more than two digits! Fail!");
//			System.exit(0);
//		}
//		return Integer.parseInt(year);
//
//	}






	public static double roundOffSegment(double meters) {
		int wholeMeters = (int)meters;
		double cm = meters%1;

		if(cm < 0.125) {
			cm = 0;
		}else if(cm >= 0.125 && cm < 0.375) {
			cm = 0.25;
		}else if(cm >= 0.375 && cm < 0.625) {
			cm = 0.5;
		}else if(cm >= 0.625 && cm < 0.875) {
			cm = 0.75;
		}else {
			cm = 0.0;
			wholeMeters++;
		}

		return (double)wholeMeters + cm;
	}



	public static Date[] getGlobalRepairDateInterval(File repairFolder) {
		ArrayList<Date> earliestDates = new ArrayList<Date>();
		ArrayList<Date> latestDates = new ArrayList<Date>();
		
	
		for(File repfile : repairFolder.listFiles()) {		
			Date[] ret = getLocalRepairDateInterval(repfile);
			earliestDates.add(ret[0]);
			latestDates.add(ret[1]);
		}
					
		Collections.sort(earliestDates);
		Collections.sort(latestDates);
		return new Date[] {earliestDates.get(earliestDates.size() -1), latestDates.get(0)};
	}






	public static Date[] getLocalRepairDateInterval(File repairFile) {
		Date[] ret = new Date[2];
		ret[0] = new Date(50, 1, 1);
		ret[1] = new Date(1, 1, 1);

		FileLineIterator lines = new FileLineIterator(repairFile, FileLineIterator.BYPASS_HEADER);
		while(lines.pop()){
			String line = lines.getLine();
			String[] fields = line.split(";");

			int day = 1;				
			int month = Util.getRepairMonth(fields[7]);
			int year = Util.getRepairYear(fields[7]);
			Date date = new Date(year, month, day);

			if(date.isBefore(ret[0])) {
				ret[0] = date;
			}if(ret[1].isBefore(date)) {
				ret[1] = date;
			}
		}

		return ret;
	}






	public static void moveFile(File fileToMove, String destination) {

		boolean isMoved = fileToMove.renameTo(new File(destination + "\\" + fileToMove.getName()));
		if (!isMoved) {
			System.err.println("COULDN'T MOVE FILE: " + fileToMove.getName());
			System.exit(0);
		}

	}



//	/**
//	 * This function takes a line and speed class as parameters, and determines which action level this lines corresponding segment belongs to.
//	 * It achieves this by looking at each and every parameter defined in TDOK2013:0347 as action level critical, and determining which action level limit has been surpassed.
//	 * The highest surpassed limit is the class for this segment.
//	 * @param line - measurement file line, a sequence of measurement values separated by a semi-colon
//	 * @param speedClass - the string corresponding to the speed class: "H5" to "H0"
//	 * @return returns the corresponding SegEv action level byte constant
//	 */
//	public static byte[] classifyMeasurementLineSegment(String line) {
//		String[] fields = line.split(";");	
//		return classifyMeasurementLine(fields);
//	}

}
