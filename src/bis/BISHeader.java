package bis;

import java.io.File;
import util.Util;

/**
 * Represents every bis type.
 * @author Henrik Ronnholm
 *
 */
public class BISHeader {

	public String name; 
	public String[] headers;
	
	public BISHeader(File file) {
		name = file.getName().replaceAll(".csv", "");
		headers = Util.get_headers(file);
	}
			
}
