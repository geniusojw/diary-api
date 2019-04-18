package org.jerrioh.security.authentication;

import java.util.Collection;

import org.jerrioh.diary.domain.Account;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CompleteToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private Account account;

	public CompleteToken(Account account, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.account = account;
	}

	@Override
	public Object getCredentials() {
		return account;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
