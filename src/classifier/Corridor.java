package classifier;

import java.util.ArrayList;

public class Corridor {

	
	ArrayList<Track> tracks = new ArrayList<Track>();
	
	
	public Corridor(ClassifierProfile classifierProfile) {
		// TODO Auto-generated constructor stub
		int i = 0;
	}

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


	public Segment getSegment(String track, int km, double m) {
		// TODO Auto-generated method stub
		return getTrack(track).getKilometer(km).getSegment(m);
	}
	
	

	
	
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


















