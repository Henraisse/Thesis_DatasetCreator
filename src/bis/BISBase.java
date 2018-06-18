package bis;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Keeps track of bis parameter selections.
 * @author Henrik Ronnholm
 *
 */
public class BISBase {

	public ArrayList<BISHeader> headers = new ArrayList<BISHeader>();	
	public Hashtable<String, int[]> signatures = new Hashtable<String, int[]>(); 
	
		
	/**
	 * Constructor.
	 * @param inputfolder
	 */
	public BISBase(String inputfolder) {
	    File directory = new File(inputfolder + "\\bis");
	    for (File file : directory.listFiles()) {
	    	if (file.isFile()) {	    		
	    		BISHeader bh = new BISHeader(file);
	    		headers.add(bh);
	    	}
	    }
	}
	
	
	
	/**
	 * Returns the selection bit array used to identify which parameters have been selected, given a bis type.
	 * @param typename - the name of the bis type
	 * @return
	 */
	public int[] getSelectedBISParameters(String typename) {
		if(signatures.containsKey(typename)) {
			return signatures.get(typename);
		}		
		return new int[]{0};
	}
	
	
	/**
	 * Given the different selections of parameters, this returns the complete and sorted BIS type parameter header string, to be appended
	 * at the top of every dataset output file.
	 * @return
	 */
	public String getBISHeaderString() {
		StringBuilder sb = new StringBuilder();
		for(BISHeader bh : headers) {
			String name = bh.name;
			int[] signature = signatures.get(name);
			String[] headers = bh.headers;

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
		}
		return sb.toString();
	}
	

}
