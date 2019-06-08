package org.jerrioh.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.security.authentication.before.AccountJwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	public JwtAuthenticationFilter(RequestMatcher matcher) {
		super(matcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(OdHeaders.TOKEN);
		return this.getAuthenticationManager().authenticate(new AccountJwtToken(token));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		OdLogger.debug("Jwt authentication succeded");
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		OdLogger.debug("Jwt authentication failed");
		SecurityContextHolder.clearContext();
		response.sendError(HttpStatus.UNAUTHORIZED.value());
	}

}