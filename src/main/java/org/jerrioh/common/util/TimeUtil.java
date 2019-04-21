package org.jerrioh.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil extends Util {
	public static String serverTime() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	dateFormat.setTimeZone(TimeZone.getTimeZone("Greenwich"));
    	return dateFormat.format(new Date(System.currentTimeMillis()));
	}
}
