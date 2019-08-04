package org.jerrioh.diary.controller.account;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.EncodingUtil;
import org.jerrioh.common.util.JwtUtil;
import org.jerrioh.common.util.MailUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.account.payload.AccountRequest;
import org.jerrioh.diary.controller.account.payload.AccountResponse;
import org.jerrioh.diary.controller.account.payload.ChangePasswordRequest;
import org.jerrioh.diary.controller.account.payload.DeleteAccountRequest;
import org.jerrioh.diary.controller.account.payload.FindLockPasswordRequest;
import org.jerrioh.diary.controller.account.payload.FindPasswordRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.security.authentication.before.AccountSigninToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController extends AbstractAccountController {

	@Transactional(rollbackFor = Exception.class)
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

	@Transactional(rollbackFor = Exception.class)
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

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/find-password")
	public ResponseEntity<ApiResponse<Object>> findPassword(@RequestBody @Valid FindPasswordRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		Account account = accountRepository.findByAccountEmail(request.getAccountEmail());
		if (account == null) {
			throw new OdException(OdResponseType.USER_NOT_FOUND);
		}

		// 이메일 발송, 발송실패 시 에러
		String generatedPassword = generateRandomPassword();
		try {
			String subject = messageSource.getMessage("account.findpassword.subject", language);
			String text = messageSource.getMessage("account.findpassword.text", language, generatedPassword);
			
			MailUtil.sendmail(account.getAccountEmail(), subject, text);
		} catch (MessagingException | UnsupportedEncodingException e) {
			OdLogger.error("sendMail fail", e);
			throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
		}
		
		account.setPasswordEnc(EncodingUtil.passwordEncode(generatedPassword));
		accountRepository.save(account);

		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/find-lock-password")
	public ResponseEntity<ApiResponse<Object>> findPassword(@RequestBody @Valid FindLockPasswordRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		Account account = super.getAccount();

		// 이메일 발송, 발송실패 시 에러
		try {
			String subject = messageSource.getMessage("account.findlock.subject", language);
			String text = messageSource.getMessage("account.findlock.text", language, request.getLock());

			MailUtil.sendmail(account.getAccountEmail(), subject, text);
		} catch (MessagingException | UnsupportedEncodingException e) {
			OdLogger.error("sendMail fail", e);
			throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
		}

		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-password")
	public ResponseEntity<ApiResponse<Object>> changePassword(@RequestBody @Valid ChangePasswordRequest request) throws OdException {
		Account account = super.getAccount();
		authenticationManager.authenticate(new AccountSigninToken(account.getAccountEmail(), request.getOldPassword()));
		
		if (!isValidPassword(request.getNewPassword())) {
			OdLogger.error("invalid password = {}", request.getNewPassword());
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}

		// 이메일 발송, 발송실패 시 에러
		account.setPasswordEnc(EncodingUtil.passwordEncode(request.getNewPassword()));
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

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/delete")
	public ResponseEntity<ApiResponse<Object>> deleteAccount(@RequestBody @Valid DeleteAccountRequest request) throws OdException {
		Account account = super.getAccount();
		
		if (!StringUtils.equals(account.getAccountEmail(), request.getAccountEmail())) {
			throw new OdException(OdResponseType.UNAUTHORIZED);
		}
		
		authenticationManager.authenticate(new AccountSigninToken(request.getAccountEmail(), request.getPassword()));

		accountRepository.delete(account);
		accountDiaryRepository.deleteByAccountEmail(account.getAccountEmail());
		
		return ApiResponse.make(OdResponseType.OK);
	}

	private String generateRandomPassword() {
		return RandomStringUtils.randomAlphanumeric(8).toUpperCase();
	}
	
	private boolean isValidPassword(String password) {
		return password.length() < 6;
	}
}
