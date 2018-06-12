package bis;
import java.io.File;
import java.util.ArrayList;

import structs.Interval;
import util.Util;

public class BISProfile {

	ArrayList<BIS_Type> elements = new ArrayList<BIS_Type>();
	String corridor;
		
	public BISProfile(String in_path, String corridor, BISBase bisbase) {
		this.corridor = corridor;		
	    File directory = new File(in_path + "\\bis");
	    for (File file : directory.listFiles()) {
	    	if (file.isFile()) {	    		
	    		
	    		if(!isblacklisted(file)) {
	    			BIS_Type btype = new BIS_Type(corridor, file, bisbase);
	    			elements.add(btype);	
	    		}
	    		    		
	    	}
	    }							
	}
	
	public BISProfile(String corridor) {
		this.corridor = corridor;
	}
	

	
	private boolean isblacklisted(File file) {
		if(file.getName().equals("Plats.csv") || file.getName().equals("Plattformsövergång.csv") || file.getName().equals("Spårkors.csv")) {
			return true;
		}
		return false;
	}


	public String getSegmentBISProfileString(String corridor, String track, int km, double m) {
		StringBuilder sb = new StringBuilder();
	
		for(BIS_Type b : elements) {
			sb.append(b.getSegmentDataString(track, km, m));
		}
		
		return sb.toString();
	}
	
}
