package structs;

import java.io.Serializable;

public class SelectionSetting implements Serializable{
	
	public int[] bits;
	
	public SelectionSetting(int[] bits) {
		this.bits = bits;
	}
	
	
}
