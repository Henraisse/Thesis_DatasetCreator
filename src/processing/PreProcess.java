package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;

import util.Util;

/**
 * The purpose of this class is to provide a clean set of methods which can pre-process the used data files into a more manageable set.
 * This is meant to be run once for every set of data files; Since it will, given the same files, produce the exact same results every time, a feature should be added
 * to allow the program to skip this step if wanted and use the pre-calculated pre-processed files.
 * @author Henra
 *
 */
public class PreProcess {


	/**
	 * This is the main method of this class, and the only one called from the outside. It prepares a more consise file structure before the main processing, and
	 * splits all the data in small folders for each corridor, to speed up and simplify the calculations.
	 * @param inputfolder
	 */
	public static void restructureFiles(String inputfolder) {	
		System.out.println("Preprocessing...");

		Util.deleteFolderAndContents(new File(inputfolder + "\\temp"));			//Remove the old temp-folder
		Util.createFolder(inputfolder + "\\temp");								//Create a new temp-folder

		//copyMeasurementFiles(inputfolder);									//move every measurement file to their respective temp folders
		fragmentBISFiles(inputfolder);											//split up the bis files into smaller versions for every corridor
		fragmentRepairFiles(inputfolder);										//split up the repair files into smaller versions for every corridor
		fragmentInspectionLevelFile(inputfolder);									//split up the inspection level file into a smaller version for every corridor

		System.out.println("Preprocessing complete.");
	}


	//########################################################################################################################################################



	/**
	 * Copies the measurement files into their respective corridor folders within the temp folder.
	 * @param inputfolder
	 */
	private static void copyMeasurementFiles(String inputfolder) {
		System.out.println("Copying measurement files...");		
		File measurement_folder = new File(inputfolder + "\\measurements");											//Identify the measurement file folder		
		for (final File fileEntry : measurement_folder.listFiles()) {												//For every file in the measurement file folder
			String[] fields = fileEntry.getName().replaceAll(".csv", "").replaceAll(".CSV","").split("_");			
			String corridor = fields[2];																				//Identify the corridor 
			File source = fileEntry;
			File dest = new File(inputfolder + "\\temp\\" + corridor + "\\measurements\\" + fileEntry.getName());		//Create the new target file
			try {	
				new File(inputfolder + "\\temp\\" + corridor + "\\measurements\\").mkdirs();							
				Files.copy(source.toPath(), dest.toPath());																//Copy the measurement to the target file
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	

	/**
	 * Fragments all the BIS files into their respective corridors' bis folders. Each BIS file is split up into several smaller bis files, each smaller file containing the lines
	 * in the original file which corridor matched the target folders corridor.
	 * @param inputfolder
	 */
	private static void fragmentBISFiles(String inputfolder) {
		System.out.println("Processing bis files...");
		File bis_folder = new File(inputfolder + "\\bis\\");																//identifies the bis folder
		for (final File fileEntry : bis_folder.listFiles()) {	 															//for every bis file
			FileFragmenter bis = new FileFragmenter(fileEntry.getAbsolutePath(), (inputfolder + "\\temp\\"), "bis\\");			//fragment the bis file
		}			
	}



	/**
	 * Fragments the inspection level file into smaller files corresponding to each corridor.
	 * @param inputfolder
	 */
	private static void fragmentInspectionLevelFile(String inputfolder) {
		System.out.println("Processing Inspection level file...");
		File inspectionclassfile = new File("C:\\Users\\Henra\\Desktop\\DATA COLLECTION\\input_folder\\events\\Besiktningsklass.csv");	
		FileFragmenter bis = new FileFragmenter(inspectionclassfile.getAbsolutePath(), (inputfolder + "\\temp\\"), "events\\");
	}



	/**
	 * Fragments the repair files into smaller files corresponding to each corridor. It reads a text file list of all files regarded as repair files (files that contain repair event entries).
	 * @param inputfolder
	 */
	private static void fragmentRepairFiles(String inputfolder){

		try {
			System.out.println("Processing repair files...");
			String repairfilelistfile = inputfolder + "\\repairfiles.txt";
			BufferedReader rbr = new BufferedReader(new FileReader(new File(repairfilelistfile)));
			String repairfilename = rbr.readLine();
			while (repairfilename != null) { //for every repair file
				File repairfile = new File(inputfolder + repairfilename);
				FileFragmenter bis = new FileFragmenter(repairfile.getAbsolutePath(), (inputfolder + "\\temp\\"), "repair\\");
				repairfilename = rbr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * This class fragments a file into several smaller different files, and does it in optimal time.
	 * @author Henra
	 *
	 */
	private static class FileFragmenter{

		Hashtable<String, ArrayList<String>> filecontent = new Hashtable<String, ArrayList<String>>();
		ArrayList<String> corridors = new ArrayList<String>();

		public FileFragmenter(String filepath, String outpath, String subpath) {

			try {
				File file = new File(filepath);
				fragmentFile(file);
				writeAllFiles(file.getName(), outpath, subpath);			

			} catch (IOException e) {
				e.printStackTrace();
			}
		}




		/**
		 * Fragments a file. It reads the file and processes each line.
		 * @param file
		 * @throws IOException
		 */
		public void fragmentFile(File file) throws IOException {			
			BufferedReader br = new BufferedReader(new FileReader(file));		//create a filereader			
			String line = br.readLine();	 									//throw away the header line				

			line = br.readLine();			
			while (line != null) {												//loop through every line

				processLine(line);													//process each line
				line = br.readLine();
			}

			br.close();															//close the filereader

		}




		/**
		 * Writes a line to a given new file. If the file doesn't exist, a new one is created.
		 * @param line			- the line to be written in the new file
		 * @param masterpath	- the main path
		 * @param subpath		- the corridor subpath, (for example \bis\ or \repair\)
		 * @param subpath2 
		 * @throws IOException 
		 */
		public void processLine(String line) throws IOException {
			String[] fields = line.split(";");																// read which corridor it is
			String corridor = fields[0];			
			if(corridor.contains("\"")) {
				return;
			}
			ArrayList<String> content = null;																			// define a new filewriter

			if(!filecontent.containsKey(corridor)) {															// create a new one if necessary, otherwise get the existing one from the hashtable
				content = new ArrayList<String>();
				filecontent.put(corridor, content);
				corridors.add(corridor);
			}else {
				content = filecontent.get(corridor);
			}

			content.add(line);
		}




		/**
		 * Writes all files to disk.
		 * @param filename
		 * @param masterpath
		 * @param subpath
		 * @throws IOException
		 */
		private void writeAllFiles(String filename, String masterpath, String subpath) throws IOException {
			for(String corridor : corridors) {																		//For every corridor
				File file = new File(masterpath + "\\" + corridor + "\\" + subpath + "\\" + filename);					//create a file corresponding to this corridor
				Util.makeFileInDirs(file);																				//write the file to disk

				FileWriter fw = new FileWriter(file);																	//Open the file
				for(String line : filecontent.get(corridor)) {																//for every line saved, write it to the file
					fw.write(line + "\n");	
				}
				fw.close();
			}
		}



	}

}
