package structs;

/**
 * Generic interval class for segment checking.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Interval {

	String[] fields;
	
	String corridor;
	public int km0, m0, km1, m1;
	public String track;

	
	public Interval(String[] fields) {
		this.fields = fields;

		String km0 = fields[1].split(" \\+ ")[0];
		String m0 = fields[1].split(" \\+ ")[1].replaceAll(" m","").replaceAll(",[\\d]", "");	
		String km1 = fields[2].split(" \\+ ")[0];
		String m1 = fields[2].split(" \\+ ")[1].replaceAll(" m","").replaceAll(",[\\d]", "");
				
		this.km0 = Integer.parseInt(km0);
		this.m0 = Integer.parseInt(m0);
		this.km1 = Integer.parseInt(km1);
		this.m1 = Integer.parseInt(m1);
		
		corridor = fields[0];
		track = fields[8];
	}
	


	/**
	 * Checks whether a given segment is within this interval.
	 * @param m
	 * @param km
	 * @param track
	 * @return
	 */
	public boolean isWithin(int m, int km, String track) {
		if(((km0*1000) + m0) <= ((km*1000) + m) && ((km*1000) + m) <= ((km1*1000) + m1) && track.equals(this.track)) {
				return true;
		}
		return false;
	}
	
	
	
	/**
	 * Checks whether a given segment is within this interval.
	 * @param m
	 * @param km
	 * @param track
	 * @return
	 */
	public boolean isWithin(double m, int km, String track) {
		if(((km0*1000) + m0) <= ((km*1000) + m) && ((km*1000) + m) <= ((km1*1000) + m1) && track.equals(this.track)) {		
			return true;
		}
		return false;
	}

	
	/**
	 * returns the fields of this interval.
	 * @return
	 */
	public String[] getFields() {
		return fields;
	}



	/**
	 * Checks if a kilometer integer is within the track.
	 * @param km
	 * @param track
	 * @return
	 */
	public boolean isWithin(int km, String track) {
		if(km0 <= km && km <= km1) {
			if(track.equals(this.track)) {
				return true;
			}
		}
		return false;
	}
	
	
	

	
}
