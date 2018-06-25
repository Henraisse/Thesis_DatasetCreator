package classifier;

import java.io.Serializable;
import java.util.ArrayList;
import classification.InvalidDataPointException;
import structs.Date;
import util.Util;

/**
 * A class that corresponds to a certain 25 centimeter segment of train railway somewhere in Sweden. In this class, repair and measurement events are stored,
 * and it can return a classification string corresponding to a certain date.
 * 
 * @author Henrik Ronnholm
 *
 */
public class Segment {

	double m;													//The meter offset from kilometer starting point
	
	ArrayList<RepEv> repairs = new ArrayList<RepEv>();			//The list of repair events
	ArrayList<MsEv> events = new ArrayList<MsEv>();				//The list of measurement events
	
	
	/**
	 * Main constructor. 
	 * @param m - double value corresponding to its meter offset from kilometer start (should be between 0.0 and 1000.0 m)
	 */
	public Segment(double m) {
		this.m = m;
	}

	
	
	
	
	
	
	/**
	 * Stores a measurement event in this segment. It takes date and measurement line as input
	 * @param date - Date object marking which day the measurement occurred
	 * @param line_fields - String array corresponding to each field in the line
	 */
	public void storeMeasurementEvent(Date date, String[] line_fields) {
		MsEv se = new MsEv(date, classifyMeasurementLine(line_fields));
		events.add(se);		
	}

	
	/**
	 * Stores a measurement event in this segment. It takes date and measurement line as input
	 * @param date - Date object marking which day the measurement occurred
	 * @param line_fields - String array corresponding to each field in the line
	 */
	public void storeMeasurementEvent(Date date, byte[] bytes) {
		MsEv se = new MsEv(date, bytes);
		events.add(se);		
	}
	
	/**
	 * Stores a repair event in this segment. It only takes a date object as input.
	 * @param date - Date object corresponding to the time of the repair
	 */
	public void storeRepairEvent(Date date) {
		RepEv re = new RepEv(date);
		repairs.add(re);	
	}
	
	
	
	/**
	 * calculates and returns the classification label string
	 * @param startdate - Date object corresponding to the current date
	 * @param maxdays - the upper limit of the date interval we want to calculate from
	 * @param mindays - the lower limit of the date interval we want to calculate from
	 * @return - classification string label for this segment and date
	 * @throws InvalidDataPointException - in case this method cannot produce a valid classification label string
	 */
	public String[] getClassificationString(Date startdate, int maxdays, int mindays, int speedLevel) throws InvalidDataPointException{
		//int optimaldays = (maxdays + mindays) / 2;
		//int currOptimaldays = 999999;
		boolean isEmpty = true;
		boolean isLastDate = true;
		
		//Create a stringbuilder to append class labels
		//StringBuilder sb = new StringBuilder();
		String[] classLabel = new String[]{"", ""};
		Date enddate = new Date(49, 12, 31);
		
		//Shrink the end date interval down to the nearest repair event
		for(RepEv r : repairs) {
			//If repair event is the same month as this measurement event, it is contaminated, and must be discarded
			if(r.getDate().isSameMonth(startdate)) {
				throw new InvalidDataPointException("(REPAIR ACTION CONTAMINATION)", new Exception());

			//If repair event is before the current nearest repair event, replace it
			}else if(r.getDate().isBefore(enddate) && startdate.daysUntil(r.getDate()) > 0) {
				enddate = r.getDate();
			}
		}
						
		for(int i = 0; i < events.size(); i++) {
			MsEv se = events.get(i);
			
			//first check that the event is inside the valid repair interval
			if(se.date.isBetween(startdate, enddate)) {

				int days = startdate.daysUntil(se.date);
				
				
				//check that the measurement is AFTER this date, and not THIS date
				if(days > 0) {				
					isLastDate = false;
					
					//check that we only get a given subset of the valid events (only the near future is useful)
					if(mindays <= days && days <= maxdays) {
						isEmpty = false;
						
						//If it passed all checks this far, it is a class label part. Append it to the string builder
						//String cstring = se.getClassificationString();
						//if(Math.abs(days-optimaldays) < currOptimaldays) {
						//	currOptimaldays = Math.abs(days-optimaldays);
						classLabel = se.getClassificationLabel(speedLevel, days);
						//}
						//sb.append(days + "-" + se.getClassificationString() + "#");
					}															
				}								
			}
		}	
		//if it is the last date, we couldn't find any subsequent measurements, and this is the last, and thus a useless, measurement.
		if(isLastDate) {throw new InvalidDataPointException("(LAST MEASUREMENT)", new Exception());}	
		
		//if no measurements could be found that is within the given interval, it is empty, and also useless.
		if(isEmpty) {throw new InvalidDataPointException("(NO MEASUREMENTS AVAILABLE)", new Exception());}	
		
		//If no exceptions has been thrown yet, we can return the class label string given by the stringbuilder
		//sb.append("..CLABEL=" + classLabel + "..");
		return classLabel;
		//return sb.toString();
	}
	
	
	
	
	
	

	

	/**
	 * Returns the combined max array by doing bytewise MAX.
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static byte[] maxBytes(byte[] b0, byte[] b1) {
		byte[] ret = new byte[6];
		for(int i = 0; i < 6; i++) {
			ret[i] = Util.max(b0[i], b1[i]);
		}
		return ret;
	}
	
	
	/**
	 * Returns the resulting classification label array when given a value, preceding classification label array, and a value matrix.
	 * It starts by taking a value and a matrix containing limits for every speed level. For each speed level (element in the array), it calculates the highest exceeded limit index.
	 * Finally, it takes the old array and the newly calculated array and does bytewise MAX, and returns the result.
	 * @param value - the given value
	 * @param matrix - the certain parameter matrix that decides the limit indices
	 * @param oldValues - the preceding classification label array.
	 * @return - the combined classification label array
	 */
	public static byte[] getParameterActionLevel(double value, double[][] matrix, byte[] oldValues) {
		//Start by creating a 6 length array corresponding to the action level for each speed level
		byte[] actionlevels = new byte[6];						

		for(int i = 0; i < 6; i++) {							//for every speed level
			double[] limits = matrix[i];							//extract the limits for that speed levels action levels
			for(int j = 0; j < limits.length; j++) {				//for every action level				
				if(value >= limits[j]) {							
					actionlevels[i] = (byte)j;							//store the biggest exceeded value index
				}
			}
		}
		return maxBytes(actionlevels, oldValues);
	}

	/**
	 * Returns the resulting classification label array when given a value, preceding classification label array, and a value matrix.
	 * It starts by taking a value and a matrix containing limits for every speed level. For each speed level (element in the array), it calculates the highest exceeded limit index.
	 * Finally, it takes the old array and the newly calculated array and does bytewise MAX, and returns the result.
	 * @param value - the given value
	 * @param matrix - the certain parameter matrix that decides the limit indices
	 * @param oldValues - the preceding classification label array.
	 * @return - the combined classification label array
	 */		
	public static byte[] getParameterIntervalActionLevel(double value, double[][][] matrix, byte[] oldValues) {
		byte[] actionlevels = new byte[6];						//Start by creating a 6 length array corresponding to the action level for each speed level

		for(int i = 0; i < 6; i++) {							//for every speed level
			double[][] limits = matrix[i];							//extract the limits for that speed levels action levels
			for(int j = 0; j < limits.length; j++) {				//for every action level				
				if(value >= limits[j][0] || value <= limits[j][1]) {							
					actionlevels[i] = (byte)j;							//store the biggest exceeded value index
				}
			}
		}
		return maxBytes(actionlevels, oldValues);
	}
	
	
	/**
	 * Returns the classification label byte array for a given measurement line
	 * @param fields - the line fields of a given measurement line
	 * @return - byte array, corresponding to the label byte array accumulated through every measurement line parameter
	 */
	public static byte[] classifyMeasurementLine(String[] fields) {

		byte[] maxLevel = new byte[] {MsEv.EVENT_MEASUREMENT_CLASS_PERFECT, MsEv.EVENT_MEASUREMENT_CLASS_PERFECT, MsEv.EVENT_MEASUREMENT_CLASS_PERFECT, 
				MsEv.EVENT_MEASUREMENT_CLASS_PERFECT, MsEv.EVENT_MEASUREMENT_CLASS_PERFECT, MsEv.EVENT_MEASUREMENT_CLASS_PERFECT};

		maxLevel = getParameterActionLevel(Double.parseDouble(fields[9]), höjd_kortvåg, maxLevel); 	 	/* 9 *///Höjd kortvåg vänster (null);		
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[10]), höjd_mellanvåg, maxLevel); 	/* 10 *///Höjd mellanvåg vänster (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[12]), höjd_kortvåg, maxLevel); 	/* 12 *///Höjd kortvåg höger (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[13]), höjd_mellanvåg, maxLevel);	/* 13 *///Höjd mellanvåg höger (null);			
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[15]), sido_kortvåg, maxLevel);		/* 15 *///Sido kortvåg vänster (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[19]), sido_kortvåg, maxLevel);		/* 19 *///Sido kortvåg höger (null);
		maxLevel = getParameterIntervalActionLevel(Double.parseDouble(fields[22]), spårvidd, maxLevel); /* 22 *///Spårvidd (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[23]), rälsförhöjning, maxLevel);	/* 23 *///Rälsförhöjning (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[29]), skevning_6m_bas, maxLevel);	/* 29 *///Skevning 6m bas (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[30]), std_höjd, maxLevel);			/* 30 *///Std höjd (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[33]), skevning_3m_bas, maxLevel);	/* 33 *///Skevning 3m bas (null);
		maxLevel = getParameterActionLevel(Double.parseDouble(fields[46]), std_sida, maxLevel);			/* 46 *///Std sida (null);
		return maxLevel;		
	}
		
	public static final double MAX = 1000.0;	//A value never meant to be exceeded. Could be a trillion for all I care	
		
	public static final double[][] höjd_kortvåg = new double[][]{//Höjdläge för respektive räl, punktfel i +/- mm, våglängd 1-25 meter
		/*H5*/	{0.0,				4.0,	6.0,	8.0,	16.0},
		/*H4*/	{0.0,				5.0,	7.0,	9.0,	20.0},
		/*H3*/	{0.0,				6.0,	8.0,	12.0,	23.0},
		/*H2*/	{0.0,				8.0,	10.0,	15.0,	26.0},
		/*H1*/	{0.0,				10.0,	12.0,	19.0,	28.0},
		/*H0*/	{0.0,				12.0,	15.0,	25.0,	28.0}
	};

	public static final double[][] höjd_mellanvåg = new double[][]{//Höjdläge för respektive räl, punktfel i +/- mm, våglängd 25-70 meter
		/*H5*/	{0.0,				10.0,	14.0,	20.0,	28.0},
		/*H4*/	{0.0,				12.0,	16.0,	23.0,	33.0},		
		/*H3*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H2*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H1*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H0*/	{0.0,				MAX,MAX,MAX,MAX}
	};

	public static final double[][] sido_kortvåg = new double[][]{//Sidoläge för respektive räl, punktfel i +/- mm, våglängd 1-25 meter
		/*H5*/	{0.0,				3.0,	4.0,	5.0,	10.0},
		/*H4*/	{0.0,				3.0,	4.0,	6.0,	12.0},
		/*H3*/	{0.0,				4.0,	5.0,	8.0,	14.0},
		/*H2*/	{0.0,				5.0,	7.0,	11.0,	17.0},
		/*H1*/	{0.0,				8.0,	11.0,	14.0,	22.0},
		/*H0*/	{0.0,				10.0,	14.0,	17.0,	22.0}
	};

	public static final double[][] sido_mellanvåg = new double[][]{//Sidoläge för respektive räl, punktfel i +/- mm, våglängd 25-70 meter
		/*H5*/	{0.0,				8.0,	10.0,	14.0,	20.0},
		/*H4*/	{0.0,				9.0,	12.0,	17.0,	24.0},		
		/*H3*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H2*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H1*/	{0.0,				MAX,MAX,MAX,MAX},
		/*H0*/	{0.0,				MAX,MAX,MAX,MAX}
	};

	public static final double[][][] spårvidd = new double[][][]{//Spårvidd, punktfel, avvikelse från nominellt värde 1435 mm, (+), - mm
		/*H5*/	{{0,0},				{8.0,-3.0},		{10.0,-3.0},	{15.0,-4.0},	{28.0,-5.0}},
		/*H4*/	{{0,0},				{10.0,-3.0},	{12.0,-4.0},	{20.0,-5.0},	{28.0,-7.0}},
		/*H3*/	{{0,0},				{12.0,-3.0},	{15.0,-4.0},	{22.0,-5.0},	{33.0,-8.0}},
		/*H2*/	{{0,0},				{12.0,-4.0},	{17.0,-4.0},	{25.0,-5.0},	{33.0,-9.0}},
		/*H1*/	{{0,0},				{15.0,-4.0},	{22.0,-4.0},	{30.0,-5.0},	{35.0,-10.0}},
		/*H0*/	{{0,0},				{15.0,-4.0},	{22.0,-4.0},	{30.0,-5.0},	{35.0,-10.0}}
	};

	public static final double[][] rälsförhöjning = new double[][]{//Rälsförhöjningens ojämnhet, punktfel i +/- mm
		/*H5*/	{0.0,				4.0,	5.0,	8.0,	20.0},
		/*H4*/	{0.0,				4.0,	6.0,	9.0,	20.0},
		/*H3*/	{0.0,				5.0,	7.0,	10.0,	20.0},
		/*H2*/	{0.0,				6.0,	8.0,	12.0,	20.0},
		/*H1*/	{0.0,				7.0,	10.0,	14.0,	20.0},
		/*H0*/	{0.0,				8.0,	12.0,	16.0,	20.0}
	};

	public static final double[][] skevning_3m_bas = new double[][]{//Skevningens punktfel vid 3 m mätbas, i +/- mm
		/*H5*/	{0.0,				4.0,	6.0,	9.0,	12.0},
		/*H4*/	{0.0,				5.0,	7.0,	10.0,	15.0},
		/*H3*/	{0.0,				6.0,	8.0,	11.0,	15.0},
		/*H2*/	{0.0,				7.0,	9.0,	12.0,	18.0},
		/*H1*/	{0.0,				8.0,	10.0,	13.0,	18.0},
		/*H0*/	{0.0,				9.0,	12.0,	14.0,	18.0}
	};

	public static final double[][] skevning_6m_bas = new double[][]{//Skevningens punktfel med 6 m mätbas, i +/- mm
		/*H5*/	{0.0,				6.0,	9.0,	13.0,	20.0},
		/*H4*/	{0.0,				6.0,	9.0,	13.0,	25.0},
		/*H3*/	{0.0,				8.0,	11.0,	17.0,	25.0},
		/*H2*/	{0.0,				9.0,	13.0,	19.0,	29.0},
		/*H1*/	{0.0,				12.0,	16.0,	21.0,	29.0},
		/*H0*/	{0.0,				15.0,	19.0,	23.0,	29.0}
	};

	public static final double[][] std_höjd = new double[][]{//Standardavvikelse för höjdläge, i mm, våglängd 3-25 meter
		/*H5*/	{0.0,				0.85,	1.15,	MAX,MAX},
		/*H4*/	{0.0,				1.15,	1.6,	MAX,MAX},
		/*H3*/	{0.0,				1.40,	1.85,	MAX,MAX},
		/*H2*/	{0.0,				1.80,	2.50,	MAX,MAX},
		/*H1*/	{0.0,				2.75,	3.75,	MAX,MAX},
		/*H0*/	{0.0,				2.75,	3.75,	MAX,MAX}
	};		

	public static final double[][] std_sida = new double[][]{//Standardavvikelse för sidoläge, i mm, våglängd 3-25 meter
		/*H5*/	{0.0,				0.50,	0.65,	MAX,MAX},
		/*H4*/	{0.0,				0.70,	0.90,	MAX,MAX},
		/*H3*/	{0.0,				0.75,	1.00,	MAX,MAX},
		/*H2*/	{0.0,				1.05,	1.45,	MAX,MAX},
		/*H1*/	{0.0,				1.95,	2.70,	MAX,MAX},
		/*H0*/	{0.0,				1.95,	2.70,	MAX,MAX}
	};




	
	/**
	 * Measurement Event class. Corresponds to a measurement for a given segment line and segment.
	 * @author Henra
	 *
	 */
	public class MsEv implements Serializable{	private static final long serialVersionUID = 1L;		
		static final byte EVENT_REPAIR = 100;
		public static final byte EVENT_MEASUREMENT_CLASS_PERFECT = 0;
		public static final byte EVENT_MEASUREMENT_CLASS_PLAN = 1;
		public static final byte EVENT_MEASUREMENT_CLASS_UH1 = 2;
		public static final byte EVENT_MEASUREMENT_CLASS_UH2 = 3;
		public static final byte EVENT_MEASUREMENT_CLASS_KRIT = 4;


		Date date;								//The date of the measurement
		byte[] eventTypeArray;					//The classification byte array from that line

		
		/**
		 * Constructor.
		 * @param year
		 * @param month
		 * @param day
		 * @param type
		 */
		public MsEv(int year, int month, int day, byte[] type) {
			date = new Date(year, month, day);
			this.eventTypeArray = type;
		}
		
		
		public String[] getClassificationLabel(int speedLevel, int days) {
			String[] ret = new String[] {Byte.toString(eventTypeArray[speedLevel]), (Integer.toString(days) + ";")};
			return ret;
		}


		/**
		 * Second constructor.
		 * @param date
		 * @param classifyMeasurementLineSegment
		 */
		public MsEv(Date date, byte[] classifyMeasurementLineSegment) {
			this.date = date;
			eventTypeArray = classifyMeasurementLineSegment;
		}
		
		
		/**
		 * Returns the classification sub string for this event. It consists of the classification label bytes.
		 * @return
		 */
		public String getClassificationString() {
			String ret = "";
			
			for(byte b : eventTypeArray) {
				ret = ret + "." + b;
			}
			return ret;
		}	
	}


	/**
	 * Repair event class. Corresponds to a repair event in the segment.
	 * @author Henra
	 *
	 */
	public class RepEv implements Serializable {	private static final long serialVersionUID = 1L;
		Date date;									//The date of the repair event
		
		public RepEv(Date date) {
			this.date = date;
		}
		
		public RepEv(byte y, byte m, byte d) {
			date = new Date(y,m,d);
		}


		public Date getDate() {
			return date;
		}						
	}
	
}
