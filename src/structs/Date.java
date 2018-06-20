package structs;

import java.io.Serializable;

public class Date implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_YEAR = 50;
	
	public static final int DAYS_JANUARY = 0; //31
	public static final int DAYS_FEBRUARY = 31; //28
	public static final int DAYS_MARCH = 59; //31
	public static final int DAYS_APRIL = 90; //30
	public static final int DAYS_MAY = 120; //31
	public static final int DAYS_JUNE = 151; //30
	public static final int DAYS_JULY = 181; //31
	public static final int DAYS_AUGUST = 212; //31
	public static final int DAYS_SEPTEMBER = 243; //30
	public static final int DAYS_OCTOBER = 273; //31
	public static final int DAYS_NOVEMBER = 304; //30
	public static final int DAYS_DECEMBER = 334; //31
	
	
	byte year;
	byte month;
	byte day;
	public Date(byte year, byte month, byte day) {
		this.year = year;
		this.month = month;
		this.day = day;
		checkValidity(year, month, day);
	}

	public Date(int year, int month, int day) {
		this.year = (byte) year;
		this.month = (byte) month;
		this.day = (byte) day;
		checkValidity(year, month, day);
	}
	
	
	
	private void checkValidity(int year2, int month2, int day2) {
		if(year > MAX_YEAR) {
			System.err.println("Cannot use the century-digits!");
			System.exit(0);
		}
	}



	private int daysSinceY2K() {
		int yearDays = 365*(year);
		int leapDays = getLeapDays();
		int monthDays = getMonthOffset();
		int dayDays = day;
		
		return yearDays + leapDays + monthDays + dayDays;
	}
	
	
	



	/**
	 * Gives the number of days until the given date.
	 * @param date
	 * @return
	 */
	public int daysUntil(Date date) {
		int days0 = daysSinceY2K();
		int days1 = date.daysSinceY2K();
		return days1 - days0;
	}
	
	
	
	
	
	@Override
	public int compareTo(Object arg0) {
		return daysSinceY2K() - ((Date) arg0).daysSinceY2K();
	}

	
	
	
	
	
	//################################################## DONE ###########################################################
	
	
	private int getLeapDays() {
		int d = 1 + (year/4);
		
		if(month < 3 && ((year/4)*4) == year) {
			d -= 1;
		}

		return d;
	}
	
	
	private int getMonthOffset() {
		switch(month) {
			case 1 : return DAYS_JANUARY;
			case 2 : return DAYS_FEBRUARY;
			case 3 : return DAYS_MARCH;
			case 4 : return DAYS_APRIL;
			case 5 : return DAYS_MAY;
			case 6 : return DAYS_JUNE;
			case 7 : return DAYS_JULY;
			case 8 : return DAYS_AUGUST;
			case 9 : return DAYS_SEPTEMBER;
			case 10 : return DAYS_OCTOBER;
			case 11 : return DAYS_NOVEMBER;
			case 12 : return DAYS_DECEMBER;
			default : return 0;
		}
	}

	public boolean isBefore(Date enddate) {
		if(enddate.daysSinceY2K() > daysSinceY2K()) {return true;}
		return false;
	}

	public boolean isSameMonth(Date startdate) {
		if(year == startdate.year && month == startdate.month) {
			return true;
		}
		return false;
	}

	public boolean isBetween(Date startdate, Date enddate) {
		if(isBefore(enddate) && startdate.isBefore(this)) {
			return true;
		}
				
		return false;
	}
	
	public String toString() {
		return "(" + year + " " + month + " " + day + ")";
	}

	public String toStringSPLOLD() {
		String ret = year + "." + month + "." + day + ";";
		return ret;
	}
	
	public String toStringSPL2() {
		String ret = month + ";" + day + ";";
		return ret;
	}

	public static String printSPLHeaderField() {
		return "MONTH;DAY;";
	}
}
