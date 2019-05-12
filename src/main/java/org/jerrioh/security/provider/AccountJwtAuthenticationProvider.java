package org.jerrioh.security.provider;

import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.util.JwtUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.repo.AccountRepository;
import org.jerrioh.security.authentication.after.CompleteAccountToken;
import org.jerrioh.security.authentication.before.AccountJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AccountJwtAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String jwt = (String) ((AccountJwtToken) authentication).getCredentials();
		if (StringUtils.isEmpty(jwt)) {
			OdLogger.info("Jwt is empty.");
			throw new OdAuthenticationException();
		}
		
		String email = JwtUtil.extractEmailFromJwt(jwt);
		if (StringUtils.isEmpty(email)) {
			OdLogger.info("Invalid Jwt");
			throw new OdAuthenticationException();
		}
		
		Account account = accountRepository.findByAccountEmail(email);
		if (account == null) {
			OdLogger.info("Account not found. email = {}", email);
			throw new OdAuthenticationException();
		}
		
		return new CompleteAccountToken(account, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccountJwtToken.class.isAssignableFrom(authentication);
	}

}
