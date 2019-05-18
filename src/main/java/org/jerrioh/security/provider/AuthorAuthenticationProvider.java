package org.jerrioh.security.provider;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.security.authentication.after.CompleteAuthorToken;
import org.jerrioh.security.authentication.before.AuthorToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthorAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AuthorRepository authorRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		AuthorToken authorToken = (AuthorToken) authentication;
		String authorId = (String) authorToken.getPrincipal();
		String authorCode = (String) authorToken.getCredentials();
		
		if (StringUtils.isAnyEmpty(authorId, authorCode)) {
			OdLogger.info("Author authentication header is empty. authorId = {}, authorCode = {}", authorId, authorCode);
			throw new OdAuthenticationException();
		}
		
		Author author = authorRepository.findByAuthorIdAndAuthorCode(authorId, authorCode);
		if (author == null || author.isDeleted()) {
			OdLogger.info("Author not found. authorId = {}, authorCode = {}", authorId, authorCode);
			throw new OdAuthenticationException();
		}
		
		return new CompleteAuthorToken(author, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AuthorToken.class.isAssignableFrom(authentication);
	}

}
