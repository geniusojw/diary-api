package org.jerrioh.common.util;

import java.util.Date;

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
	private static String jwtSecret;
	private static long jwtExpirationInMs;
	
	public static void setUp(String jwtSecret, long jwtExpirationInMs) {
		AuthenticationUtil.jwtSecret = jwtSecret;
		AuthenticationUtil.jwtExpirationInMs = jwtExpirationInMs;
	}

	public static String generateJwt(String userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public static String extractUserIdFromJwt(String jwt) {
		Jws<Claims> parseClaimsJws;
		try {
			parseClaimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
		} catch (JwtException e) {
			OdLogger.error("Jwt exception occured jwt={}, e={}", jwt, e.toString());
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
