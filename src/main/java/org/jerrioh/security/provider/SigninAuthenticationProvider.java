package org.jerrioh.security.provider;

import java.util.List;

import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.security.authentication.CompleteToken;
import org.jerrioh.security.authentication.SigninToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SigninAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		SigninToken signinToken = (SigninToken) authentication;
		
		String userId = (String) signinToken.getPrincipal();
		String password = (String) signinToken.getCredentials();
		
		List<Account> accounts = accountRepository.findByUserId(userId);
		if (accounts.isEmpty()) {
			OdLogger.info("User not found. userId = {}", userId);
			throw new OdAuthenticationException();
		}
		
		if (!EncodingUtil.passwordMatches(password, accounts.get(0).getPasswordEnc())) {
			OdLogger.info("UserId or Password not valid");
			throw new OdAuthenticationException();
		}
		
		return new CompleteToken(accounts.get(0), null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SigninToken.class.isAssignableFrom(authentication);
	}

}
