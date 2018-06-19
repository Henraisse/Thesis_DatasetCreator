package classifier;

import util.Util;

/**
 * Corresponds to a certain corridor track kilometer. Consists of all the 25cm segments of that kilometer.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Kilometer {

	int i;
	Segment[] segments = new Segment[4040];
	
	public Kilometer(int i) {
		this.i = i;
	}

	
	/**
	 * Returns a given segment.
	 * @param m
	 * @return
	 */
	public Segment getSegment(double m) {
		m = (Util.roundOffSegment(m))*4.0;
		int i = (int)m;
		if(segments[i] == null) {
			segments[i] = new Segment(m);
		}
		return segments[i];
	}
	
	
}
