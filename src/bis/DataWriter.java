package bis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DataWriter {

	String outputfolder;
	String output_file_header;

	public int totalmbwritten = 0;	
	int printfilecounter = 0;	
	int writtenmegabytes = 0;
	int writtenbytes = 0;
	int maxMB = 100;
	
	BufferedWriter writer;
	
	
	/**
	 * Constructor.
	 * @param outputfolder
	 * @param maxMB
	 * @param output_file_header
	 */
	public DataWriter(String outputfolder, int maxMB, String output_file_header) {
		this.maxMB = maxMB;
		this.output_file_header = output_file_header;		
		
		this.outputfolder = outputfolder;
	}
	
	
	
	/**
	 * Begin writing in a fresh, new file.
	 */
	public void startNewWriteFile() {
		try {						
			String name = createNewOutputFile(printfilecounter, outputfolder);
			File file = new File(name);
			file.delete();
			
			FileOutputStream fos = new FileOutputStream(name);
	        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
	        
			writer = new BufferedWriter(osw);
			appendLine(output_file_header);

		} catch (IOException e) {
			System.err.println("Couldn't write to file!");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Close the current writing file.
	 */
	public void closeWriteFile() {
		try {
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * Appends a new string to the file.
	 * @param inputline
	 */
	public void appendLine(String inputline){
		if(writer == null) {
			startNewWriteFile();
			if(writer == null) {
				System.exit(0);
			}
		}
		int newBytes = inputline.length();
		count(newBytes);
		
		if(writtenmegabytes >= maxMB){
			writtenmegabytes = 0;
			writtenbytes = newBytes;	
			resetFile();							
		}		
		try {		
			writer.append(inputline);
			
		} catch (IOException e) {e.printStackTrace();}

	}

	
	
	/**
	 * Closes the old writing file and opens a new one.
	 */
	private void resetFile() {
		closeWriteFile();
		printfilecounter++;
		startNewWriteFile();		
	}

	

	/**
	 * Counts the bytes passed to the DataWriter.
	 * @param bytes
	 */
	private void count(int bytes) {
		writtenbytes += bytes;
		
		if(writtenbytes > 1000000 * 1.024) {
			writtenbytes -= 1000000 * 1.024;
			writtenmegabytes++;
			totalmbwritten++;
		}		
	}


	
	/*
	 * Creates a new file to write to.
	 */
	public String createNewOutputFile(int c, String folder) {
		try {			
			String path = folder + "\\dataset" + c + ".csv";
			File f = new File(path);
			f.getParentFile().mkdirs(); 
			
			f.createNewFile();
			return f.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	
	
}
