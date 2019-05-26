package org.jerrioh.diary.controller.account;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.common.util.JwtUtil;
import org.jerrioh.diary.controller.account.payload.AccountRequest;
import org.jerrioh.diary.controller.account.payload.AccountResponse;
import org.jerrioh.diary.controller.account.payload.FindPasswordRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.security.authentication.before.AccountSigninToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController extends AbstractAccountController {
	
	@PostMapping(value = "/sign-up")
	public ResponseEntity<ApiResponse<AccountResponse>> signUp(@RequestBody @Valid AccountRequest request) throws OdException {
		Account account = accountRepository.findByAccountEmail(request.getAccountEmail());
		if (account != null) {
			throw new OdException(OdResponseType.USER_CONFLICT);
		}
		
		account = new Account();
		account.setAccountEmail(request.getAccountEmail());
		account.setPasswordEnc(EncodingUtil.passwordEncode(request.getPassword()));
		account.setFirstAuthorId(request.getAuthorId());
		account.setLastAuthorId(request.getAuthorId());
		
		accountRepository.save(account);
		
		AccountResponse response = new AccountResponse();
		response.setToken(JwtUtil.generateJwt(request.getAccountEmail()));
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/sign-in")
	public ResponseEntity<ApiResponse<AccountResponse>> signIn(@RequestBody @Valid AccountRequest request) {
		Authentication preAuthentication = new AccountSigninToken(request.getAccountEmail(), request.getPassword());
		Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);

		Account account = (Account) postAuthentication.getPrincipal();
		account.setLastAuthorId(request.getAuthorId());
		accountRepository.save(account);
		
		AccountResponse response = new AccountResponse();
		response.setToken(JwtUtil.generateJwt(request.getAccountEmail()));
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/find-password")
	public ResponseEntity<ApiResponse<Object>> findPassword(@RequestBody @Valid FindPasswordRequest request) throws OdException {
		Account account = accountRepository.findByAccountEmail(request.getAccountEmail());
		if (account == null) {
			throw new OdException(OdResponseType.USER_NOT_FOUND);
		}

		// 이메일 발송, 발송실패 시 에러
		account.setPasswordEnc(EncodingUtil.passwordEncode(generateRandomPassword()));
		accountRepository.save(account);

		return ApiResponse.make(OdResponseType.OK);
	}

	@PostMapping(value = "/refresh-token")
	public ResponseEntity<ApiResponse<AccountResponse>> refreshToken() {
		Account account = super.getAccount();
		
		AccountResponse response = new AccountResponse();
		response.setToken(JwtUtil.generateJwt(account.getAccountEmail()));
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/delete")
	public ResponseEntity<ApiResponse<Object>> deleteAccount(@RequestBody @Valid AccountRequest request) throws OdException {
		Account account = super.getAccount();
		
		if (!StringUtils.equals(account.getAccountEmail(), request.getAccountEmail())) {
			throw new OdException(OdResponseType.UNAUTHORIZED);
		}
		
		Authentication preAuthentication = new AccountSigninToken(request.getAccountEmail(), request.getPassword());
		authenticationManager.authenticate(preAuthentication);

		accountRepository.delete(account);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	private String generateRandomPassword() {
		return RandomStringUtils.randomAlphanumeric(8);
	}
}
