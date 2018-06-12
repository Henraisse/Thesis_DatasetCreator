package bis;
import java.io.File;
import java.util.ArrayList;

import structs.Interval;
import util.Util;

/**
	 * This class represents ONE bis type for one corridor.
	 * @author Henra
	 *
	 */
	public class BIS_Type{
		
		//-----------------------------------------------------------
		/*VARIABLES*/
		String type_name = "";
		String corridor_name = "";
		String[] headers;
		//BISBase bisbase;
		
		boolean test = false;
		
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		int[] selectedVariables = {0};
		
		//-----------------------------------------------------------
		
		
		public BIS_Type(String corridor, File file, BISBase bisbase) {
			int i = 0;
			//this.bisbase = bisbase;
			corridor_name = corridor;
			storeElement(file);
			setSelectedVariables(bisbase);
		}
		
				
		public BIS_Type(String corridor, File file, int[] var) {
			int i = 0;
			corridor_name = corridor;
			storeElement(file);
			selectedVariables = var;
			test = true;
		}
		





		public void storeElement(File file) {
			type_name = file.getName().replaceAll(".csv", "");
			headers = Util.get_headers(file);
			intervals = Util.get_intervals(file, corridor_name);
		}
		

		
		public void setSelectedVariables(BISBase bisbase) {
			selectedVariables = bisbase.getSelectedBISParameters(type_name);				
			//TODO: GÖR SÅ ATT INTERVALS TOKENIZERAS OCH INTERVALLET BYGGS UPP, OCH ATT DE FÅR EN COMPARE-FUNKTION			
		}


		String error_field = "---";
		
		public String getSegmentDataString(String track, int km, double m) {
			//System.out.println("Finding an interval that fits [TRACK=" + track + " KM=" + km + " M=" + m + "]...");
			Interval interval = null;
			StringBuilder sb = new StringBuilder();
			//find the correct interval, if there is any (otherwise just return a default string with zeroes)
			//print/filter the interval's tokenized strings based on the bit-integer array provided by bisbase
			
			//System.out.println("Interval count: " + intervals.size());
			for(Interval i: intervals) {
				//System.out.println("-Checking interval [TRACK=" + i.track + " KM0=" + i.km0 + " M0=" + i.m1 + " KM1=" + i.km1 + " M1=" + i.m1 + "]...");
				if(i.isWithin(m, km, track)) {
					interval = i;
					//System.out.println("-WITHIN INTERVAL: [TRACK=" + i.track + " KM0=" + i.km0 + " M0=" + i.m0 + " KM1=" + i.km1 + " M1=" + i.m1 + "]...");
					break;
				}
			}
			
			int[] bits = selectedVariables;
			//int[] bits = bisbase.getSelectedBISParameters(type_name);
						
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
		
		
		public boolean noBits(int[] bits) {
			
			for(int i = 0; i < bits.length; i++) {
				if(bits[i] == 1) {
					return false;
				}
			}
			return true;						
		}
		
		
	}
	
	
	
	