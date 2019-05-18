package org.jerrioh.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class TimeUtil extends Util {
	public static String serverTime() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	dateFormat.setTimeZone(TimeZone.getTimeZone("Greenwich"));
    	return dateFormat.format(new Date(System.currentTimeMillis()));
	}
	
	public static String formatISO8601(Timestamp timestamp) {
		DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
		DateTime dateTime = new DateTime(timestamp);
    	return formatter.print(dateTime); // ex) 2019-05-18T06:07:23Z
	}
}
