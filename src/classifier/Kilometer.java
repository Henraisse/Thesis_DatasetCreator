package classifier;

import util.Util;

public class Kilometer {

	int i;
	Segment[] segments = new Segment[4040];
	
	public Kilometer(int i) {
		this.i = i;
	}

	
	
	public Segment getSegment(double m) {
		m = (Util.roundOffSegment(m))*4.0;
		int i = (int)m;
		Segment ret = segments[i];
		if(segments[i] == null) {
			segments[i] = new Segment(m);
		}
		return segments[i];
	}
	
	
}
