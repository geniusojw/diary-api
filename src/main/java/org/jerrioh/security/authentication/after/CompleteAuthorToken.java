package org.jerrioh.security.authentication.after;

import java.util.Collection;

import org.jerrioh.diary.domain.Author;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CompleteAuthorToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private Author author;

	public CompleteAuthorToken(Author author, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);

		this.author = author;
	}

	@Override
	public Object getPrincipal() {
		return author;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}
}
