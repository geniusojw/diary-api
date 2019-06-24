package org.jerrioh.common.util;

import java.util.Random;

public class StringUtil {
	private static Random random = new Random();
	
	public static String randomString(String... strings) {
		return strings[random.nextInt(strings.length)];
	}
}
