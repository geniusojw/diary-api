package org.jerrioh.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SigninToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String userId;
	private String password;

	public SigninToken(String userId, String password) {
		super(null); // NO_AUTHORITIES
		this.userId = userId;
		this.password = password;
	}

	@Override
	public Object getPrincipal() {
		return userId;
	}

	@Override
	public Object getCredentials() {
		return password;
	}
}
