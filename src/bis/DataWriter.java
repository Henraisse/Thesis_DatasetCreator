package bis;

import java.beans.PropertyChangeEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.swing.SwingWorker;

import util.Util;

public class DataWriter {

	String outputfolder;
	String output_file_header;
	
	int printfilecounter = 0;
	public int totalmbwritten = 0;
	int writtenmegabytes = 0;
	int writtenbytes = 0;
	
	int maxMB = 1;
	
	BufferedWriter writer;
	
	
	
	public DataWriter(String outputfolder, int maxMB, String output_file_header) {
		this.maxMB = maxMB;
		this.output_file_header = output_file_header;		
		
		this.outputfolder = outputfolder;
	}
	
	
	
	public void startNewWriteFile() {
		try {						
			String name = createNewOutputFile(printfilecounter, outputfolder);
			writer = new BufferedWriter(new FileWriter(name, true));
			appendLine(output_file_header);

		} catch (IOException e) {
			System.err.println("Couldn't write to file!");
			e.printStackTrace();
		}
	}
	
	
	public void closeWriteFile() {
		try {
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
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
	
	
	



	private void resetFile() {
		closeWriteFile();
		printfilecounter++;
		startNewWriteFile();		
	}



	private void count(int bytes) {
		writtenbytes += bytes;
		
		if(writtenbytes > 1000000 * 1.024) {
			writtenbytes -= 1000000 * 1.024;
			writtenmegabytes++;
			totalmbwritten++;
		}		
	}



	public String createNewOutputFile(int c, String folder) {
		try {			
			String path = folder + "\\dataset" + c + ".csv";
			File f = new File(path);
			//System.out.println(path);
			f.getParentFile().mkdirs(); 
			
			f.createNewFile();
			return f.getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	
	
	
	

	

	
	
}
