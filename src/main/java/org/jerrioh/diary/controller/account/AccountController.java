package org.jerrioh.diary.controller.account;

import org.jerrioh.common.exception.ODException;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.common.util.JwtUtil;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.diary.payload.account.SigninRequest;
import org.jerrioh.diary.payload.account.SigninResponse;
import org.jerrioh.diary.payload.account.SignupRequest;
import org.jerrioh.security.authentication.SigninToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping(value = "/signup")
	public ResponseEntity<SigninResponse> signup(@RequestBody SignupRequest signupRequest) throws ODException {
		if (accountRepository.findById(signupRequest.getUserId()).isPresent()) {
			throw new ODException("User already exists.");
		}
		
		Account account = new Account();
		account.setUserId(signupRequest.getUserId());
		account.setPasswordEnc(EncodingUtil.passwordEncode(signupRequest.getPassword()));
		
		accountRepository.save(account);

		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(JwtUtil.generate(account.getUserId()));
		return ResponseEntity.ok(signinResponse);
	}

	@PostMapping(value = "/signin")
	public ResponseEntity<SigninResponse> signin(@RequestBody SigninRequest signinRequest) {
		Authentication preAuthentication = new SigninToken(signinRequest.getUserId(), signinRequest.getPassword());
		Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);

		SecurityContextHolder.getContext().setAuthentication(postAuthentication);

		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(JwtUtil.generate(signinRequest.getUserId()));
		return ResponseEntity.ok(signinResponse);
	}

}
