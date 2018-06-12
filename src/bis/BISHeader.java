package bis;

import java.io.File;

import util.Util;

public class BISHeader {

	public String name; 
	public String[] headers;
	
	public BISHeader(File file) {
		int i = 0;
		name = file.getName().replaceAll(".csv", "");
		headers = Util.get_headers(file);
		
	}
	
	
	
	
	
	
	
}
