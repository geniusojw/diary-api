package org.jerrioh.common.exception;

import org.springframework.security.core.AuthenticationException;

public class OdAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = 1L;
	
	public OdAuthenticationException() {
		super("ODAuthentication exception");
	}
}
