package org.jerrioh.diary.controller.author;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.AuthorDiaryRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorDiary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/diaries")
public class AuthorDiaryController extends AbstractAuthorController {
	
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> write(@RequestBody @Valid AuthorDiaryRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestHeader(value = OdHeaders.COUNTRY) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID) String timeZoneId) throws OdException {
		Author author = super.getAuthor();
		
		super.checkInvalidDate(request.getDiaryDate(), 0L);
		super.getDateTimeZone(timeZoneId);
		
		AuthorDiary diary = authorDiaryRepository.findByAuthorIdAndDiaryDate(author.getAuthorId(), request.getDiaryDate());
		if (diary == null) {
			OdLogger.info("new author diary");
			diary = new AuthorDiary();
			diary.setAuthorId(author.getAuthorId());
			diary.setDiaryDate(request.getDiaryDate());
		}
		
		diary.setTitle(request.getTitle());
		diary.setContent(request.getContent());
		diary.setLanguage(language);
		diary.setCountry(country);
		diary.setTimeZoneId(timeZoneId);
		
		authorDiaryRepository.save(diary);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	@DeleteMapping(value = "/{diaryDate}")
	public ResponseEntity<ApiResponse<Object>> deleteDiary(@PathVariable(name = "diaryDate") String diaryDate) throws OdException {
		Author author = super.getAuthor();
		
		AuthorDiary diary = authorDiaryRepository.findByAuthorIdAndDiaryDate(author.getAuthorId(), diaryDate);
		if (diary == null || diary.isDeleted()) {
			throw new OdException(OdResponseType.DIARY_NOT_FOUND);
		}
		diary.setDeleted(true);
		authorDiaryRepository.save(diary);
		

		author.setChocolates(author.getChocolates() + 1);
		authorRepository.save(author);
		authorRepository.insertChocolateHistory(author.getAuthorId(), 1, "TODAY DIARY");
		
		return ApiResponse.make(OdResponseType.OK);
	}
}
