package org.jerrioh.security;

import java.util.Arrays;
import java.util.List;

import org.jerrioh.security.provider.JwtAuthenticationProvider;
import org.jerrioh.security.provider.SigninAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String JWT_AUTHENTICATION_URL_PATTERN = "/**/*";
	private static final String[] IGNORE_AUTHENTICATION_URI_PATTERNS = {
			"/error", "/account/signup", "/account/signin",
			"/", "/favicon.ico", "/**/*.png", "/**/*.gif",
			"/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js" };
    		
    
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    
    @Autowired
    private SigninAuthenticationProvider signinAuthenticationProvider;
    
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(jwtAuthenticationProvider)
			.authenticationProvider(signinAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf().disable()
			.exceptionHandling()
			.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers(IGNORE_AUTHENTICATION_URI_PATTERNS).permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		List<String> ignores = Arrays.asList(IGNORE_AUTHENTICATION_URI_PATTERNS);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(JWT_AUTHENTICATION_URL_PATTERN, ignores);
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(matcher);
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		return jwtAuthenticationFilter;
	}
}