package org.jerrioh.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String token;

	public JwtToken(String token) {
		super(null);
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
