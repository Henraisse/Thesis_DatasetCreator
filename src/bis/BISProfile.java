package bis;
import java.io.File;
import java.util.ArrayList;

/**
 * A memory bank for each corridor to store and retrieve bis element intervals.
 * @author Henrik Ronnholm
 *
 */
public class BISProfile {

	ArrayList<BIS_Type> elements = new ArrayList<BIS_Type>();
	String corridor;
		
	/**
	 * Constructor.
	 * @param in_path
	 * @param corridor
	 * @param bisbase
	 */
	public BISProfile(String in_path, String corridor, BISBase bisbase) {
		this.corridor = corridor;		
	    File directory = new File(in_path + "\\bis");
	    for (File file : directory.listFiles()) {
	    	if (file.isFile()) {	    		
	    		
	    		if(!(file.getName().equals("Plats.csv") || file.getName().equals("Plattformsövergång.csv") || file.getName().equals("Spårkors.csv"))) {
	    			BIS_Type btype = new BIS_Type(corridor, file, bisbase);
	    			elements.add(btype);	
	    		}
	    		    		
	    	}
	    }							
	}
	

	/**
	 * Given the bis type selection done in the GUI and BISBase, retrieves the bis data string for a given segment.
	 * @param corridor
	 * @param track
	 * @param km
	 * @param m
	 * @return
	 */
	public String getSegmentBISProfileString(String corridor, String track, int km, double m) {
		StringBuilder sb = new StringBuilder();
	
		for(BIS_Type b : elements) {
			sb.append(b.getSegmentDataString(track, km, m));
		}
		
		return sb.toString();
	}
	
}
