package org.jerrioh.security.provider;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.repo.AccountRepository;
import org.jerrioh.security.authentication.after.CompleteAccountToken;
import org.jerrioh.security.authentication.before.AccountSigninToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AccountSigninAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		AccountSigninToken signinToken = (AccountSigninToken) authentication;
		
		String email = (String) signinToken.getPrincipal();
		String password = (String) signinToken.getCredentials();
		if (StringUtils.isAnyEmpty(email, password)) {
			OdLogger.info("Account authentication parameter is empty. email = {}, password = {}", email, password);
			throw new OdAuthenticationException();
		}
		
		Account account = accountRepository.findByAccountEmail(email);
		if (account == null) {
			OdLogger.info("Account not found. email = {}", email);
			throw new OdAuthenticationException();
		}
		
		if (!EncodingUtil.passwordMatches(password, account.getPasswordEnc())) {
			OdLogger.info("UserId or Password not valid");
			throw new OdAuthenticationException();
		}
		
		return new CompleteAccountToken(account, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccountSigninToken.class.isAssignableFrom(authentication);
	}

}
