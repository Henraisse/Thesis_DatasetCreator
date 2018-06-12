package bis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class BISBase {

	public ArrayList<BISHeader> headers = new ArrayList<BISHeader>();
	
	public Hashtable<String, int[]> signatures = new Hashtable<String, int[]>();
	
	
	
	public BISBase(String inputfolder) {
		readBISFiles(inputfolder);
	}
	
	
	
	
	
	
	
	
	
	
	
	public int[] getSelectedBISParameters(String typename) {
		if(signatures.containsKey(typename)) {
			return signatures.get(typename);
		}		
		return new int[]{0};
	}
	
	
	
	public void readBISFiles(String in_path) {
	    File directory = new File(in_path + "\\bis");
	    for (File file : directory.listFiles()) {
	    	if (file.isFile()) {	    		
	    		BISHeader bh = new BISHeader(file);
	    		headers.add(bh);
	    	}
	    }	
	}
	
	
	
	
	public String[] getFilteredBISHeaderStrings() {
		ArrayList<String> bisheaders = new ArrayList<String>();
		String[] ret = new String[0];
		//For every bis type
		for(BISHeader bh : headers) {
			
			String name = bh.name;
			int[] signature = signatures.get(name);
			String[] headers = bh.headers;
			

			for(int i = 0; i < signature.length; i++) {
				if (signature[i] == 1) {
					bisheaders.add(headers[i]);
				}
			}
			
			ret = new String[bisheaders.size()];
			for(int i = 0; i < bisheaders.size(); i++) {
				ret[i] = bisheaders.get(i);
			}

			
		}
		return ret;
	}
	
	
	
	public String getBISHeaderString() {
		StringBuilder sb = new StringBuilder();
		for(BISHeader bh : headers) {
			String name = bh.name;
			int[] signature = signatures.get(name);
			String[] headers = bh.headers;
			
//			try {
				
				for(int i = 0; i < signature.length; i++) {
					if (signature[i] == 1) {
						sb.append(bh.name + ";");
						break;
					}
				}
				
				for(int i = 0; i < signature.length; i++) {
					if (signature[i] == 1) {
						sb.append(headers[i] + ";");
					}
				}
				
//			}catch(NullPointerException e) {
//				System.out.println("Signature not available for name " + name);
//			}

			
		}
		return sb.toString();
	}
	
	
	
	
	public static String[] get_headers(File file) {

		String[] ret = new String[10];
		try {		
			BufferedReader br = new BufferedReader(new FileReader(file));
			ret = br.readLine().split(";");	 //Throw this line away.				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	

	

	
	

	
}
