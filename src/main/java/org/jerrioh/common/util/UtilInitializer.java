package org.jerrioh.common.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UtilInitializer {
	@Value("${jwt.jwtscret}")
	private String jwtSecret;

	@Value("${jwt.jwt-expiration-days}")
	private String jwtExpirationDays;

	@PostConstruct
	public void init() {
		Long jwtExpiration = Long.parseLong(jwtExpirationDays);
		JwtUtil.setUp(jwtSecret, jwtExpiration);
	}
}
