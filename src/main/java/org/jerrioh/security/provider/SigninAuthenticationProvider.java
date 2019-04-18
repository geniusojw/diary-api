package org.jerrioh.security.provider;

import org.jerrioh.common.exception.ODAuthenticationException;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.security.authentication.CompleteToken;
import org.jerrioh.security.authentication.SigninToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SigninAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository usersRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SigninToken signinToken = (SigninToken) authentication;
		
		String userId = (String) signinToken.getPrincipal();
		String password = (String) signinToken.getCredentials();
		
		Account account = usersRepository.findById(userId)
				.orElseThrow(() -> new ODAuthenticationException("User not found. userId = " + userId));
		
		if (!EncodingUtil.passwordMatches(password, account.getPasswordEnc())) {
			throw new ODAuthenticationException("UserId or Password not valid.");
		}
		
		return new CompleteToken(account, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SigninToken.class.isAssignableFrom(authentication);
	}

}
