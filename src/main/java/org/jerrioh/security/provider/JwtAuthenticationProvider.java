package org.jerrioh.security.provider;

import java.util.List;

import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.util.AuthenticationUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.security.authentication.CompleteToken;
import org.jerrioh.security.authentication.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String token = (String) ((JwtToken) authentication).getCredentials();
		if (StringUtils.isEmpty(token)) {
			OdLogger.info("Jwt is empty.");
			throw new OdAuthenticationException();
		}
		String userId = AuthenticationUtil.extractUserIdFromJwt(token);
		if (StringUtils.isEmpty(userId)) {
			OdLogger.info("Invalid Jwt");
			throw new OdAuthenticationException();
		}
		List<Account> accounts = accountRepository.findByUserId(userId);
		if (accounts.isEmpty()) {
			OdLogger.info("User not found. userId = {}", userId);
			throw new OdAuthenticationException();
		}
		return new CompleteToken(accounts.get(0), null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtToken.class.isAssignableFrom(authentication);
	}

}
