package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import bis.BISProfile;
import bis.BIS_Type;
import classification.InvalidDataPointException;
import classifier.Log;
import classifier.Segment;
import classifier.Segment.MsEv;
import structs.Date;
import util.Util;

public class Test {
	
	public static void testCriticalSystems(Log log, String inputfolder) {
		int i = 0;
		testSystem(log, testDates(), "Date day distance checking");
		testSystem(log, test_valueClassifier_OK(), "Measurement line classification");
		testSystem(log, testBISTypes(log, inputfolder), "BIS parameter interval checking");
		testSystem(log, testClassifierIntervals(log), "Segment date interval checking");
	}
	



	public static void testSystem(Log log, boolean bool, String name) {
		if(!bool) {
			log.reportln("Program integrity compromised, a bug has been detected. Terminating process...");
			log.close();
			System.exit(0);
		
		}else {
			log.reportln(name + " --- (OK)");
		}
	}
	
	
	
	/**
	 * If this returns true, it means that the method that returns a class array for a given parameter and value (along with an earlier class array), is working. Thus, for a given set of values,
	 * we can be sure that for a set of values, given that their respective parameters aren't mixed, always yields the correct result.
	 * @return
	 */
	public static boolean test_valueClassifier_OK() {
		
		double value = 6.0;
		double[][] usedParameter = Segment.skevning_6m_bas;
		byte[] input = {0, 0, 0, 0, 0, 0};
		byte[] expectedRes = {1, 1, 0, 0, 0, 0};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}

		value = 6.0;
		usedParameter = Segment.skevning_6m_bas;
		input = new byte[]{0, 0, 3, 0, 0, 0};
		expectedRes = new byte[]{1, 1, 3, 0, 0, 0};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}
		
		value = 1.16;
		usedParameter = Segment.std_höjd;
		input = new byte[]{0, 0, 0, 0, 0, 0};
		expectedRes = new byte[]{2, 1, 0, 0, 0, 0};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}

		value = 1.16;
		usedParameter = Segment.std_höjd;
		input = new byte[]{3, 0, 0, 3, 0, 0};
		expectedRes = new byte[]{3, 1, 0, 3, 0, 0};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}
		
		value = 17.5;
		usedParameter = Segment.höjd_mellanvåg;
		input = new byte[]{1, 0, 1, 0, 0, 2};
		expectedRes = new byte[]{2, 2, 1, 0, 0, 2};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}
		
		value = 7.8;
		usedParameter = Segment.rälsförhöjning;
		input = new byte[]{1, 4, 1, 2, 0, 2};
		expectedRes = new byte[]{2, 4, 2, 2, 1, 2};
		if(!testVC(value, usedParameter, input, expectedRes)) {return false;}
		
		return true;
	}
	
	

	
	public static boolean testVC(double value, double[][] parameter, byte[] input, byte[] expectedRes) {
		byte[] res = Segment.getParameterActionLevel(value, parameter, input);
		if(Arrays.equals(res, expectedRes)) {return true;}
		return false;
	}
	
	
	
	public static boolean testBISTypes(Log log, String input) {		
		File testfile = new File(input + "\\testbisfile.csv");
		BIS_Type testtype = new BIS_Type("1337", testfile, new int[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0});
		
			
		String t = testtype.getSegmentDataString("A", 12, 221.5);
		if(!t.equals("NO;0;")) {return false;}
			
		t = testtype.getSegmentDataString("A", 45, 160.5);
		if(!t.equals("NO;0;")) {return false;}
				
		t = testtype.getSegmentDataString("NOTRACKNAME", 160, 570.5);
		if(!t.equals("NO;0;")) {return false;}
			
		t = testtype.getSegmentDataString("D", 200, 130.5);
		if(!t.equals("NO;0;")) {return false;}
			
		t = testtype.getSegmentDataString("C", 5, 30.5);
		if(!t.equals("NO;0;")) {return false;}
			
		t = testtype.getSegmentDataString("A", 1, 30.5);
		if(!t.equals("YES;1;")) {return false;}
		
		t = testtype.getSegmentDataString("C", 80, 100.0);
		if(!t.equals("YES;4;")) {return false;}
		
		t = testtype.getSegmentDataString("R", 152, 995.5);		
		if(!t.equals("YES;7;")) {return false;}
		
		t = testtype.getSegmentDataString("A", 30, 0.0);
		if(!t.equals("YES;3;")) {return false;}
		
		t = testtype.getSegmentDataString("E", 180, 500.0);
		if(!t.equals("YES;9;")) {return false;}
		
		t = testtype.getSegmentDataString("A", 500, 227.5);
		if(!t.equals("YES;11;")) {return false;}
		
		return true;
	}

	
	
	private static boolean testDates() {
		Date start = new Date(15, 10, 10);
		Date stop = new Date(16, 2, 14);
		if(start.daysUntil(stop) != 127)return false;
		
		start = new Date(12, 2, 10);
		stop = new Date(12, 2, 24);
		if(start.daysUntil(stop) != 14)return false;
		
		return true;
	}
	
	
	
	
	
	
	
	public static boolean testClassifierIntervals(Log log) {
		Segment seg = new Segment(100.0);
		
		seg.storeRepairEvent(		new Date(10, 2, 15));
		seg.storeRepairEvent(		new Date(10, 8, 13));
		
		seg.storeMeasurementEvent(	new Date(10, 5, 10), 	new byte[]{0, 0, 0, 0, 0, 0});
		seg.storeMeasurementEvent(	new Date(10, 7, 10), 	new byte[]{1, 1, 1, 1, 1, 1});
		
		String r = testSegment(seg, new Date(10, 3, 20));
		log.reportln(r);
		String res = "51-.0.0.0.0.0.0#112-.1.1.1.1.1.1#";
		if(!r.equals(res)) return false;
		
		
		return true;
	}
	
	
	public static String testSegment(Segment seg, Date date) {
		try {
			String ret = seg.getClassificationString(date, 99999, 1);
			return ret;
		} catch (InvalidDataPointException e) {
			return e.getMessage();
		}	
	}
	
	
	
	
	
}

