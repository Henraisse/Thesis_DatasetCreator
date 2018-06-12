import bis.BISBase;
import bis.BISProfile;

/**
 * This class is the DatasetCompiler. It is used to extract a complete dataset from the available .csv data files. 
 * @author Henra
 *
 */
public class DatasetCompiler {
	BISBase bisbase;
	
	
	public DatasetCompiler(String in_path, String out_path) {
		//bisbase = new BISBase();
		//skapa en BIS-profil
		
		//make_dataset_corridor("C:\\Users\\Henra\\Desktop\\DATA COLLECTION\\dataset_creator_target_folder\\bis\\", "813");
		BISProfile bis = new BISProfile("C:\\Users\\Henra\\Desktop\\DATA COLLECTION\\dataset_creator_target_folder\\bis\\", "813", bisbase);
		//för varje mätning och bandel, extrahera alla datapunkter
		//för varje datapunkt, skicka query till BIS-profilen och fråga om BIS-kryddningar till datasetet
		
		
		testBisBase(bis);
		
		
		
	}



	
	public void testBisBase(BISProfile bis) {
		String ret = bis.getSegmentBISProfileString("813", "3", 430, 550);
	}
	
	
	
	
	
}
