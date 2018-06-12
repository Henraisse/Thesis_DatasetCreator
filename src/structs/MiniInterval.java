package structs;

import java.io.Serializable;

public class MiniInterval implements Serializable{
	double m0;
	double m1;
	
	public MiniInterval(double mx0, double mx1) {
		m0 = mx0;
		m1 = mx1;
	}

	public boolean isWithinInterval(double m) {
		if(m0 <= m && m <= m1) {
			return true;
		}
		return false;		
	}
	
	
	
}
