package org.jerrioh.diary.controller.author;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.author.payload.AuthorDiaryRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorDiary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/diaries")
public class AuthorDiaryController extends AbstractAuthorController {
	
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> write(@RequestBody @Valid AuthorDiaryRequest request) throws OdException {
		Author author = super.getAuthor();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		int minimumPermit = Integer.parseInt(ZonedDateTime.now(ZoneOffset.MIN).format(dateTimeFormatter));
		int maximumPermit = Integer.parseInt(ZonedDateTime.now(ZoneOffset.MAX).format(dateTimeFormatter));
		int diaryDateInteger = Integer.parseInt(request.getDiaryDate());
		if (diaryDateInteger < minimumPermit || maximumPermit < diaryDateInteger) {
			OdLogger.info("invalid diaryDate. diaryDate = {} ", request.getDiaryDate());
			throw new OdAuthenticationException();
		}
		
		AuthorDiary diary = authorDiaryRepository.findByAuthorIdAndDiaryDate(author.getAuthorId(), request.getDiaryDate());
		if (diary != null) {
			throw new OdException(OdResponseType.DIARY_CONFLICT);
		}
		
		diary = new AuthorDiary();
		diary.setAuthorId(author.getAuthorId());
		diary.setDiaryDate(request.getDiaryDate());
		diary.setTitle(request.getTitle());
		diary.setContent(request.getContent());
		diary.setLanguage(request.getLanguage());
		diary.setCountry(request.getCountry());
		authorDiaryRepository.save(diary);
		
		return ApiResponse.make(OdResponseType.OK);
	}
}
