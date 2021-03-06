package org.jerrioh.common.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jerrioh.common.Util;
import org.jerrioh.common.exception.OdAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil extends Util {
	private static String jwtSecret;
	private static long jwtExpirationInMs;
	
	public static void setUp(String jwtSecret, long jwtExpirationDays) {
		JwtUtil.jwtSecret = jwtSecret;
		JwtUtil.jwtExpirationInMs = TimeUnit.DAYS.toMillis(jwtExpirationDays);
	}

	public static String generateJwt(String email) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public static String extractEmailFromJwt(String jwt) {
		Jws<Claims> parseClaimsJws;
		try {
			parseClaimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
		} catch (JwtException e) {
			OdLogger.error("Jwt exception occured jwt={}, e={}", jwt, e.toString());
			throw new OdAuthenticationException();
		}
		return parseClaimsJws.getBody().getSubject();
	}
}
