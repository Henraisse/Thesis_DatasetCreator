package bis;
import java.io.File;
import java.util.ArrayList;
import structs.Interval;
import util.Util;

/**
	 * This class represents ONE bis type for one corridor, for example sleepers or signal posts.
	 * @author Henrik Rönnholm
	 *
	 */
	public class BIS_Type{
		
		//-----------------------------------------------------------
		/*VARIABLES*/
		String type_name = "";
		String corridor_name = "";
		String[] headers;
		String error_field = "---";
		
		boolean test = false;
		
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		int[] selectedVariables = {0};
		
		//-----------------------------------------------------------
		
	
		
		/**
		 * Constructor.
		 * @param corridor
		 * @param file
		 * @param bisbase
		 */
		public BIS_Type(String corridor, File file, BISBase bisbase) {
			corridor_name = corridor;
			storeElement(file);
			setSelectedVariables(bisbase);
		}
	
		
		
		/**
		 * Constructor for the test cases.		
		 * @param corridor
		 * @param file
		 * @param var
		 */
		public BIS_Type(String corridor, File file, int[] var) {
			corridor_name = corridor;
			storeElement(file);
			selectedVariables = var;
			test = true;
		}



		/**
		 * Stores a bis type element.
		 * @param file
		 */
		public void storeElement(File file) {
			type_name = file.getName().replaceAll(".csv", "");
			headers = Util.get_headers(file);
			intervals = Util.get_intervals(file, corridor_name);
		}
		

		
		/**
		 * Sets which variables have been selected. Done at setup, so the same values
		 * doesn't have to be selected every time the program starts.
		 * @param bisbase
		 */
		public void setSelectedVariables(BISBase bisbase) {
			selectedVariables = bisbase.getSelectedBISParameters(type_name);					
		}

		
		
		/**
		 * Checks if a bit array (actually an int array) contains any ones.
		 * It is used to check if a BIS-type is completely unused, in which case it shouldn't be present
		 * in the final dataset in any way.
		 * @param bits
		 * @return
		 */
		public boolean noBits(int[] bits) {
			for(int i = 0; i < bits.length; i++) {
				if(bits[i] == 1) {
					return false;
				}
			}
			return true;						
		}
		
		
		
		/**
		 * For this specific BIS type, return the segment data string (if present and selected fields)
		 * For example, returns the data string for ballast, beginning with "YES" or "NO" depending if the segment is within a ballast interval, 
		 * followed by each selected parameter (for example corridor, kilometer, type of ballast, and such)
		 * @param track
		 * @param km
		 * @param m
		 * @return
		 */
		public String getSegmentDataString(String track, int km, double m) {
			Interval interval = null;
			StringBuilder sb = new StringBuilder();
			
			for(Interval i: intervals) {
				if(i.isWithin(m, km, track)) {
					interval = i;
					break;
				}
			}
			
			int[] bits = selectedVariables;
						
			if(interval == null) {		
				if(!noBits(bits)) {
					sb.append("NO;");
				}
				for(int i : bits) {
					if(i == 1) {
						sb.append("0;");
					}
				}
			}else {
				if(!noBits(bits)) {
					sb.append("YES;");
				}
				
				String[] fields = interval.getFields();
				for(int i = 0; i < bits.length; i++) {
					if(bits[i] == 1) {
						sb.append(fields[i] + ";");
					}
				}
			}
			return sb.toString();
		}
		
	
	}