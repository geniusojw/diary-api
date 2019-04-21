package org.jerrioh.common.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.diary.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationUtil extends Util {
	private static final String JWT_SECRET = "NuP7j3DwRfbImWt59Xp6g3J7r4ahOOVd";
	private static final long JWT_EXPIRATION_IN_MS = TimeUnit.MINUTES.toMillis(10);

	public static String generateJwt(String userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_IN_MS);

		return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
	}

	public static String extractUserIdFromJwt(String jwt) {
		Jws<Claims> parseClaimsJws;
		try {
			parseClaimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
		} catch (JwtException e) {
			OdLogger.error("Jwt exception occured", e.toString());
			throw new OdAuthenticationException();
		}
		return parseClaimsJws.getBody().getSubject();
	}
	
	public static Account getAuthenticatedAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new OdAuthenticationException();
		}
		return (Account) authentication.getPrincipal();
	}
}
