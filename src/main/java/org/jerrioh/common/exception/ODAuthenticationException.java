package org.jerrioh.common.exception;

import org.springframework.security.core.AuthenticationException;

public class ODAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public ODAuthenticationException(String msg) {
		super(msg);
	}
}
