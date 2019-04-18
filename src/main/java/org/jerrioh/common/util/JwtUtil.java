package org.jerrioh.common.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil extends AbstractStatic {
	private static final String jwtSecret = "secret";
	private static final int jwtExpirationInMs = 1000;

	public static String generate(String userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public static Jws<Claims> getJwsClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		} catch (JwtException e) {
			Log.error("jwt exception", e);
			return null;
		}
	}

	public static String getUserId(Jws<Claims> jws) {
		return jws.getBody().getSubject();
	}

}
