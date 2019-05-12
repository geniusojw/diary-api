package org.jerrioh.security.authentication.before;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AccountSigninToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;

	public AccountSigninToken(String email, String password) {
		super(null); // NO_AUTHORITIES
		this.email = email;
		this.password = password;
	}

	@Override
	public Object getPrincipal() {
		return email;
	}

	@Override
	public Object getCredentials() {
		return password;
	}
}
