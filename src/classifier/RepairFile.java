package classifier;

import java.io.File;

/**
 * Corresponds to a repair file.
 * @author Henrik Ronnholm
 *
 */
public class RepairFile {

	File file;
	

	public RepairFile(File repfile) {
		file = repfile;
	}

	
	public File getFile() {
		return file;
	}
	
	
}
