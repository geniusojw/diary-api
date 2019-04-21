package org.jerrioh.diary.controller.diary;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.util.AuthenticationUtil;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.Diary;
import org.jerrioh.diary.payload.ApiResponse;
import org.jerrioh.diary.payload.diary.DiaryReadResponse;
import org.jerrioh.diary.payload.diary.DiaryWriteBulkRequest;
import org.jerrioh.diary.payload.diary.DiaryWriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/diary")
public class DiaryController extends AbstractController {
	@GetMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<DiaryReadResponse>> read(@PathVariable(name = "writeDay") String writeDay) throws OdException {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		Diary diary = diaryRepository.findByWriteUserIdAndWriteDay(account.getUserId(), writeDay);
		if (diary == null) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		
		DiaryReadResponse diaryResponse = new DiaryReadResponse();
		diaryResponse.setWriteDay(diary.getWriteDay());
		diaryResponse.setWriteUserId(diary.getWriteUserId());
		diaryResponse.setTitle(diary.getTitle());
		diaryResponse.setContent(diary.getContent());

		return ApiResponse.make(OdResponseType.OK, diaryResponse);
	}

	@PostMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<Object>> write(@PathVariable(name = "writeDay") String writeDay,
			@RequestBody @Valid DiaryWriteRequest diaryWriteRequest) throws OdException {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		Diary diary = diaryRepository.findByWriteUserIdAndWriteDay(account.getUserId(), writeDay);
		if (diary != null) {
			throw new OdException(OdResponseType.DIARY_CONFLICT);
		}
		diary = new Diary();
		diary.setWriteDay(writeDay);
		diary.setWriteUserId(account.getUserId());
		diary.setTitle(diaryWriteRequest.getTitle());
		diary.setContent(diaryWriteRequest.getContent());
		diaryRepository.save(diary);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	@DeleteMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(name = "writeDay") String writeDay) throws OdException {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		Diary diary = diaryRepository.findByWriteUserIdAndWriteDay(account.getUserId(), writeDay);
		if (diary == null) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		diaryRepository.delete(diary);
		return ApiResponse.make(OdResponseType.OK);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<DiaryReadResponse>>> readAll() {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		List<Diary> diaries = diaryRepository.findByWriteUserId(account.getUserId());
		List<DiaryReadResponse> diaryResponses = new ArrayList<>();
		for (Diary diary : diaries) {
			DiaryReadResponse diaryResponse = new DiaryReadResponse();
			diaryResponse.setWriteDay(diary.getWriteDay());
			diaryResponse.setWriteUserId(diary.getWriteUserId());
			diaryResponse.setTitle(diary.getTitle());
			diaryResponse.setContent(diary.getContent());
		}
		return ApiResponse.make(OdResponseType.OK, diaryResponses);
	}

//	@Transactional(rollbackFor = Exception.class)
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> writeAll(@RequestBody @Valid List<DiaryWriteBulkRequest> diaryRequests) {
		Account account = AuthenticationUtil.getAuthenticatedAccount();
		for (DiaryWriteBulkRequest diaryRequest : diaryRequests) {
//			Diary diary = diaryRepository.findByWriteUserIdAndWriteDay(account.getUserId(), diaryRequest.getWriteDay());
//			if (diary != null) {
//				throw new OdException(OdResponseType.DIARY_CONFLICT);
//			}
			Diary diary = new Diary();
			diary.setWriteDay(diaryRequest.getWriteDay());
			diary.setWriteUserId(account.getUserId());
			diary.setTitle(diaryRequest.getTitle());
			diary.setContent(diaryRequest.getContent());
			diaryRepository.save(diary);
		}
		return ApiResponse.make(OdResponseType.OK);
	}

}
