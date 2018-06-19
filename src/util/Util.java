package util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.ParamFrame;
import structs.Date;
import structs.Interval;

public class Util {






	/**
	 * Return intervals from a file. Reads line by line and turns it into a given interval. Requires each file line to be of correct format (obviously).
	 * @param file
	 * @param corridor
	 * @return
	 */
	public static ArrayList<Interval> get_intervals(File file, String corridor){
		ArrayList<Interval> intervals = new ArrayList<Interval>();		
		try {		
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();	 //Throw this line away.				

			line = br.readLine();			
			while (line != null) {

				String[] fields = line.split(";");  
				String bandel_name = fields[0];

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
		return intervals;
	}



	/**
	 * Checks if string corresponds to an integer.
	 * @param s
	 * @return
	 */
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





	/**
	 * Returns the header strings of a given csv file.
	 * @param file
	 * @return
	 */
	public static String[] get_headers(File file) {

		String[] ret = new String[1];
		try {		
			BufferedReader br = new BufferedReader(new FileReader(file));
			ret = br.readLine().split(";");	 			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}



	/**
	 * Sets the look and feel of the ParamFrame.
	 * @param pf
	 */
	public static void setLookAndFeel(ParamFrame pf) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(pf);
		} 
		catch (UnsupportedLookAndFeelException e) {}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
	}



	/**
	 * Returns a timestamp string of the current date and time.
	 */
	public static String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		return sdf.format(new Timestamp(System.currentTimeMillis()));
	}



	/**
	 * Checks whether a given date is after another date.
	 * @param year
	 * @param month
	 * @param day
	 * @param curr_year
	 * @param curr_month
	 * @param curr_day
	 * @return
	 */
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


	
	
	/**
	 * Deletes a folder and its contents.
	 * @param folder
	 */
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
	
	
	
	/**
	 * Creates a folder.
	 * @param path
	 */
	public static void createFolder(String path) {
		new File(path).mkdirs();
	}

	
	
	/**
	 * Creates a file, if containing folder doesn't exist, create it.
	 * @param file
	 * @throws IOException
	 */
	public static void makeFileInDirs(File file) throws IOException {
		if(!file.exists()) {
			new File(file.getParentFile().getPath()).mkdirs();
			file.createNewFile();
		}
	}
	
	
	
	/**
	 * Writes a single line to a file.
	 * @param file
	 * @param line
	 * @throws IOException
	 */
	public static void writeSingleLine(File file, String line) throws IOException {
		FileWriter writer = new FileWriter(file, true);
		writer.write(line + "\n");
		writer.close();
	}


	
	/**
	 * Returns the kilometer field of a given measurement line.
	 * @param line
	 * @return
	 */
	public static int getMeasurementLineKm(String line) {
		int ret = Integer.parseInt(line.split(";")[4]);
		return ret;
	}

	
	
	/**
	 * Returns the meter field of a given measurement line.
	 * @param line
	 * @return
	 */
	public static double getMeasurementLineM(String line) {			
		double ret = Double.parseDouble(line.split(";")[5]);
		return ret;
	}

	
	
	/**
	 * Returns the track field of a given measurement line.
	 * @param line
	 * @return
	 */
	public static String getMeasurementLineTrack(String line) {	
		String ret = line.split(";")[6];
		return ret;
	}



	/**
	 * Returns the month in which repairs was made in a repair file.
	 * @param string
	 * @return
	 */
	public static int getRepairMonth(String string) {
		String month = string.split("-")[1];
		return Integer.parseInt(month);
	}
	
	
	
	/**
	 * Returns the year in which repairs was made in a repair file.
	 * @param string
	 * @return
	 */
	public static int getRepairYear(String string) {
		String year = string.split("-")[0];
		year = year.substring(2, 4);
		if(year.length() > 2) {
			System.err.println("Getting repair year with more than two digits! Fail!");
			System.exit(0);
		}
		return Integer.parseInt(year);
	}
	
	

	/**
	 * Rounds a given meter double to the closest 25cm segment.
	 * @param meters
	 * @return
	 */
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



	/**
	 * Returns the global repair interval in which the repair file provides coverage (absence of evidence is not evidence of absence)
	 * @param repairFolder
	 * @return
	 */
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





	/**
	 * Returns the local repair date interval.
	 * @param repairFile
	 * @return
	 */
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





	/**
	 * Moves a file to a given destination.
	 * @param fileToMove
	 * @param destination
	 */
	public static void moveFile(File fileToMove, String destination) {
		boolean isMoved = fileToMove.renameTo(new File(destination + "\\" + fileToMove.getName()));
		if (!isMoved) {
			System.err.println("COULDN'T MOVE FILE: " + fileToMove.getName());
			System.exit(0);
		}
	}




}
