package org.jerrioh.security.authentication.before;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AuthorToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String authorId;
	private String authorCode;

	public AuthorToken(String authorId, String authorCode) {
		super(null);
		this.authorId = authorId;
		this.authorCode = authorCode;
	}

	@Override
	public Object getPrincipal() {
		return authorId;
	}

	@Override
	public Object getCredentials() {
		return authorCode;
	}
}
