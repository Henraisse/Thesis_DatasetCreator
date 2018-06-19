package structs;

import java.io.Serializable;

/**
 * Wrapper class for the selection setting bit array. Not sure why I needed a class for this but I don't have any time to fix that at the moment.
 * 
 * @author Henrik Ronnholm
 *
 */
public class SelectionSetting implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int[] bits;
	
	public SelectionSetting(int[] bits) {
		this.bits = bits;
	}
	
	
}
