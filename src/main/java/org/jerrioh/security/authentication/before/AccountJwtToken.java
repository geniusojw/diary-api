package org.jerrioh.security.authentication.before;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AccountJwtToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String jwt;

	public AccountJwtToken(String jwt) {
		super(null);
		this.jwt = jwt;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return jwt;
	}
}
