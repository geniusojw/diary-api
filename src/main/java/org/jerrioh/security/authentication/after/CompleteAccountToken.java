package org.jerrioh.security.authentication.after;

import java.util.Collection;

import org.jerrioh.diary.domain.Account;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CompleteAccountToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private Account account;

	public CompleteAccountToken(Account account, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);

		this.account = account;
	}

	@Override
	public Object getPrincipal() {
		return account;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}
}
