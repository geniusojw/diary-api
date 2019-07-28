package org.jerrioh.diary.controller.account;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.MailUtil;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.account.payload.AccountDiaryRequest;
import org.jerrioh.diary.controller.account.payload.AccountDiaryResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.AccountDiary;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account/diaries")
public class AccountDiaryController extends AbstractAccountController {
	
	@GetMapping(value = "/{diaryDate}")
	public ResponseEntity<ApiResponse<AccountDiaryResponse>> read(@PathVariable(name = "diaryDate") String diaryDate) throws OdException {
		Account account = super.getAccount();
		AccountDiary diary = accountDiaryRepository.findByAccountEmailAndDiaryDate(account.getAccountEmail(), diaryDate);
		if (diary == null) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		
		AccountDiaryResponse response = new AccountDiaryResponse();
		response.setDiaryDate(diaryDate);
		response.setTitle(diary.getTitle());
		response.setContent(diary.getContent());

		return ApiResponse.make(OdResponseType.OK, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AccountDiaryResponse>>> readAll() {
		Account account = super.getAccount();
		List<AccountDiary> diaries = accountDiaryRepository.findByAccountEmail(account.getAccountEmail());
		List<AccountDiaryResponse> responses = new ArrayList<>();
		for (AccountDiary diary : diaries) {
			if (diary.isDeleted()) {
				continue;
			}
			
			AccountDiaryResponse response = new AccountDiaryResponse();
			response.setDiaryDate(diary.getDiaryDate());
			response.setTitle(diary.getTitle());
			response.setContent(diary.getContent());
			responses.add(response);
		}
		return ApiResponse.make(OdResponseType.OK, responses);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> write(@RequestBody @Valid AccountDiaryRequest request) throws OdException {
		Account account = super.getAccount();
		AccountDiary diary = accountDiaryRepository.findByAccountEmailAndDiaryDate(account.getAccountEmail(), request.getDiaryDate());
		if (diary != null) {
			throw new OdException(OdResponseType.DIARY_CONFLICT);
		}
		diary = new AccountDiary();
		diary.setAccountEmail(account.getAccountEmail());
		diary.setDiaryDate(request.getDiaryDate());
		diary.setTitle(request.getTitle());
		diary.setContent(request.getContent());
		accountDiaryRepository.save(diary);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Object>> overWrite(@RequestBody @Valid AccountDiaryRequest request) throws OdException {
		Account account = super.getAccount();
		AccountDiary diary = accountDiaryRepository.findByAccountEmailAndDiaryDate(account.getAccountEmail(), request.getDiaryDate());
		if (diary == null) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		diary = new AccountDiary();
		diary.setAccountEmail(account.getAccountEmail());
		diary.setDiaryDate(request.getDiaryDate());
		diary.setTitle(request.getTitle());
		diary.setContent(request.getContent());
		accountDiaryRepository.save(diary);
		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/synchronize")
	public ResponseEntity<ApiResponse<List<AccountDiaryResponse>>> synchronize(@RequestBody @Valid List<AccountDiaryRequest> requests) {
		Account account = super.getAccount();
		List<AccountDiary> diaries = accountDiaryRepository.findByAccountEmail(account.getAccountEmail());
		Map<String, AccountDiary> diaryMap = diaries.stream().collect(Collectors.toMap(AccountDiary::getDiaryDate, diary -> diary));
		
		for (AccountDiaryRequest request : requests) {
			if (diaryMap.containsKey(request.getDiaryDate())) {
				continue;
			}
			AccountDiary diary = new AccountDiary();
			diary.setAccountEmail(account.getAccountEmail());
			diary.setDiaryDate(request.getDiaryDate());
			diary.setTitle(request.getTitle());
			diary.setContent(request.getContent());
			diaryMap.put(request.getDiaryDate(), diary);
			accountDiaryRepository.save(diary);
		}
		
		List<AccountDiaryResponse> responses = new ArrayList<>();
		for (AccountDiary diary : diaryMap.values()) {
			if (diary.isDeleted()) {
				continue;
			}
			AccountDiaryResponse response = new AccountDiaryResponse();
			response.setDiaryDate(diary.getDiaryDate());
			response.setTitle(diary.getTitle());
			response.setContent(diary.getContent());
			responses.add(response);
		}
		return ApiResponse.make(OdResponseType.OK, responses);
	}

	@PostMapping(value = "/export")
	public ResponseEntity<ApiResponse<Object>> exportDiaries(
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		Account account = super.getAccount();
		
		List<AccountDiary> diaries = accountDiaryRepository.findByAccountEmail(account.getAccountEmail());
		if (diaries.isEmpty()) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		
		String date = messageSource.getMessage("account.emailexport.date", language) + ": ";
		String title = messageSource.getMessage("account.emailexport.title", language) + ": ";
		String line = "--------------------------------------------------------------------------------";
		
		StringBuilder diaryBuilder = new StringBuilder();
		for (AccountDiary diary : diaries) {
			String dateStringDotAdded = diary.getDiaryDate().substring(0, 4)
					+ "." + diary.getDiaryDate().substring(4, 6)
					+ "." + diary.getDiaryDate().substring(6, 8);
			
			diaryBuilder.append(date + dateStringDotAdded + "\n");
			diaryBuilder.append(title + diary.getTitle() + "\n");
			diaryBuilder.append(diary.getContent() + "\n\n" + line + "\n\n");
		}

		String subject = messageSource.getMessage("account.emailexport.subject", language);
		String text = diaryBuilder.toString();
		
		try {
			MailUtil.sendmail(account.getAccountEmail(), subject, text);
		} catch (UnsupportedEncodingException | MessagingException e) {
			OdLogger.error("sendMail fail", e);
			throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
		}
		
		
		return ApiResponse.make(OdResponseType.OK);
	}

	@DeleteMapping(value = "/{diaryDate}")
	public ResponseEntity<ApiResponse<Object>> deleteDiary(@PathVariable(name = "diaryDate") String diaryDate) throws OdException {
		Account account = super.getAccount();
		
		AccountDiary diary = accountDiaryRepository.findByAccountEmailAndDiaryDate(account.getAccountEmail(), diaryDate);
		if (diary == null || diary.isDeleted()) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		diary.setDeleted(true);
		accountDiaryRepository.save(diary);
		return ApiResponse.make(OdResponseType.OK);
	}

}
