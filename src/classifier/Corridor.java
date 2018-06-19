package classifier;

import java.util.ArrayList;

/**
 * Corridor class.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Corridor {

	
	ArrayList<Track> tracks = new ArrayList<Track>();
	
	
	public Corridor(ClassifierProfile classifierProfile) {
		
	}

	
	/**
	 * retrieves a given track object. If there is no track by that name, create and return a new track object.
	 * @param track
	 * @return
	 */
	public Track getTrack(String track) {
		for(Track t : tracks) {
			if(t.name.equals(track)) {
				return t;
			}
		}
		Track ret = new Track(track);
		tracks.add(ret);
		return ret;
	}


	/**
	 * Returns a given segment.
	 * @param track
	 * @param km
	 * @param m
	 * @return
	 */
	public Segment getSegment(String track, int km, double m) {
		return getTrack(track).getKilometer(km).getSegment(m);
	}
	
	

	
	/**
	 * Track class. Corresponds to a certain track in a corridor.
	 * @author Henrik Ronnholm
	 *
	 */
	public class Track{

		private String name;		
		Kilometer[] kilometers = new Kilometer[2000];
		
		public Track(String track) {
			name = track;
		}

		public Kilometer getKilometer(int i) {
			if(kilometers[i] == null) {
				kilometers[i] = new Kilometer(i);
			}
			return kilometers[i];
		}
		
		
		public String getName() {
			return name;
		}
	}


	
	
}


















