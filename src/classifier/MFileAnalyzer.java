package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class MFileAnalyzer {

	Log specialLog;
	//specialLog.reportln(line.substring(40, line.length()-1));
	Hashtable<String, Header> counter = new Hashtable<String, Header>();
	ArrayList<String> list = new ArrayList<String>();
	
	public MFileAnalyzer(String outputfolder) {
		specialLog = new Log(new File(outputfolder), "speciallog.txt", true, false);
	}

	
	public void countHeader(String line) {
		Header h;
		if(counter.containsKey(line)) {
			h = counter.get(line);
			h.increment();
		}else {
			h = new Header(line);
			counter.put(line, h);
			list.add(line);
		}
	
	}
	
	
	public void display() {
		specialLog.reportln("displaying..");
		for(String s : list) {
			int c = counter.get(s).counter;
			int d = s.split(";").length;
			specialLog.reportln(c + " = " + d + " ======== " + s);
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	private class Header{

		@SuppressWarnings("unused")
		String line;
		int counter = 1;
		
		public Header(String line) {
			this.line = line;
		}
		
		
		public void increment() {
			counter = counter + 1;
		}

	}
	
	
	
	
	
	
	
}
