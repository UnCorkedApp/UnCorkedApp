package com.getuncorkedapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateTime {

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE MMM dd, HH:mm");
	
	public static String displayTime(Date date) {
		if ( date == null)
			return "";
		else
			return FORMAT.format(date);
	}
}
