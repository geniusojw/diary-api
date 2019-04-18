package org.jerrioh.security.provider;

import org.jerrioh.common.exception.ODAuthenticationException;
import org.jerrioh.common.util.JwtUtil;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.security.authentication.CompleteToken;
import org.jerrioh.security.authentication.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountRepository usersRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String) ((JwtToken) authentication).getCredentials();
		if (StringUtils.isEmpty(token)) {
			throw new ODAuthenticationException("Jwt is empty.");
		}
		
		Jws<Claims> jws = JwtUtil.getJwsClaims(token);
		if (jws == null) {
			throw new ODAuthenticationException("Invalid Jwt");
		}
		String userId = JwtUtil.getUserId(jws);
		Account user = usersRepository.findById(userId)
				.orElseThrow(() -> new ODAuthenticationException("User not found. userId = " + userId));

		return new CompleteToken(user, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtToken.class.isAssignableFrom(authentication);
	}

}
