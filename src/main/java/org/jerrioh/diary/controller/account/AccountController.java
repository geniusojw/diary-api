package org.jerrioh.diary.controller.account;

import java.util.List;

import javax.validation.Valid;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.util.AuthenticationUtil;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.payload.ApiResponse;
import org.jerrioh.diary.payload.account.DeleteNonMemberRequest;
import org.jerrioh.diary.payload.account.SigninRequest;
import org.jerrioh.diary.payload.account.SigninResponse;
import org.jerrioh.security.authentication.SigninToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController extends AbstractController {
	@PostMapping(value = "/signup")
	public ResponseEntity<ApiResponse<SigninResponse>> signup(@RequestBody @Valid SigninRequest signupRequest) throws OdException {
		List<Account> accounts = accountRepository.findByUserId(signupRequest.getUserId());
		if (!accounts.isEmpty()) {
			throw new OdException(OdResponseType.USER_CONFLICT);
		}
		
		Account account = new Account();
		account.setUserId(signupRequest.getUserId());
		account.setMemberUserId(signupRequest.getUserId());
		account.setPasswordEnc(EncodingUtil.passwordEncode(signupRequest.getPassword()));
		
		accountRepository.save(account);
		
		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(AuthenticationUtil.generateJwt(signupRequest.getUserId()));
		return ApiResponse.make(OdResponseType.OK, signinResponse);
	}

	@PostMapping(value = "/signin")
	public ResponseEntity<ApiResponse<SigninResponse>> signin(@RequestBody @Valid SigninRequest signinRequest) {
		Authentication preAuthentication = new SigninToken(signinRequest.getUserId(), signinRequest.getPassword());
		Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);

		SecurityContextHolder.getContext().setAuthentication(postAuthentication);
		
		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(AuthenticationUtil.generateJwt(signinRequest.getUserId()));
		return ApiResponse.make(OdResponseType.OK, signinResponse);
	}

	@PostMapping(value = "/refresh-token")
	public ResponseEntity<ApiResponse<SigninResponse>> signin() {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		
		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(AuthenticationUtil.generateJwt(account.getUserId()));
		return ApiResponse.make(OdResponseType.OK, signinResponse);
	}

	@PostMapping(value = "/deleteNonMember")
	public ResponseEntity<ApiResponse<Object>> deleteNonMember(@RequestBody @Valid DeleteNonMemberRequest deleteNonMemberRequest) {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		
		String memberUserId = deleteNonMemberRequest.getMemberUserId();
		account.setMemberUserId(memberUserId);
		
		accountRepository.save(account);

		return ApiResponse.make(OdResponseType.OK);
	}
	
	@GetMapping(value = "/about-me")
	public ResponseEntity<ApiResponse<Object>> aboutMe() throws OdException {

		
		return ApiResponse.make(OdResponseType.OK);
	}
	
	// TODO 회원삭제 api : 계정 삭제, 일기 삭제, 편지 삭제, 별명 삭제

}
