package org.jerrioh.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodingUtil extends Util {
	private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static String passwordEncode(String rawPassword) {
		return bCryptPasswordEncoder.encode(rawPassword);
	}
	
	public static boolean passwordMatches(String rawPassword, String encodedPassword) {
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}
}
