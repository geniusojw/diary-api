package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.AuthorLetterRequest;
import org.jerrioh.diary.controller.author.payload.AuthorLetterResponse;
import org.jerrioh.diary.controller.author.requestparam.GetLettersParameter;
import org.jerrioh.diary.controller.author.requestparam.GetLettersParameter.Range;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorLetter;
import org.jerrioh.diary.domain.AuthorLetter.LetterType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/letters")
public class AuthorLetterController extends AbstractAuthorController {

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> send(@RequestBody @Valid AuthorLetterRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestHeader(value = OdHeaders.COUNTRY) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID) String timeZoneId) throws OdException {
		Author author = super.getAuthor();
		AuthorLetter letter = authorLetterRepository.findByLetterId(request.getLetterId());
		if (letter != null) {
			throw new OdException(OdResponseType.LETTER_CONFLICT);
		}
		
		Author toAuthor = authorRepository.findByAuthorId(request.getToAuthorId());
		
//		// 랜덤 편지 발송. 발신인이 없는 경우.
//		// 같은 언어, 국가, 타임존이 확인된 사람. 7일동안 2번이상 일기를 쓴 사람에게.
//		super.getDateTimeZone(timeZoneId);
//		toAuthor = authorRepository.findRandomAuthor(author.getAuthorId(), language, country, timeZoneId);
			
		if (toAuthor == null) {
			throw new OdException(OdResponseType.USER_NOT_FOUND);
		}
		
		String toAuthorId = toAuthor.getAuthorId();
		String toAuthorNickname = StringUtils.defaultIfEmpty(request.getToAuthorNickname(), toAuthor.getNickname());
		
		letter = new AuthorLetter();
		letter.setLetterId(request.getLetterId());
		letter.setLetterType(LetterType.NORMAL);
		letter.setFromAuthorId(author.getAuthorId());
		letter.setFromAuthorNickname(author.getNickname());
		letter.setToAuthorId(toAuthorId);
		letter.setToAuthorNickname(toAuthorNickname);
		letter.setContent(request.getContent());
		letter.setWrittenTime(new Timestamp(System.currentTimeMillis()));

		authorLetterRepository.save(letter);

		return ApiResponse.make(OdResponseType.OK);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AuthorLetterResponse>>> getLetters(@Valid GetLettersParameter parameter) {
		Author author = super.getAuthor();
		
		List<AuthorLetter> letters;
		if (Range.INCOMMING.equals(parameter.getRange())) {
			letters = authorLetterRepository.findByToAuthorId(author.getAuthorId());
		} else if (Range.OUTGOING.equals(parameter.getRange())) {
			letters = authorLetterRepository.findByFromAuthorId(author.getAuthorId());
		} else {
			letters = authorLetterRepository.findByToAuthorIdOrFromAuthorId(author.getAuthorId(), author.getAuthorId());
		}

		List<AuthorLetterResponse> responses = new ArrayList<>();
		for (AuthorLetter letter : letters) {
			if (letter.isDeleted()) {
				continue;
			}
			
			AuthorLetterResponse response = new AuthorLetterResponse();
			response.setLetterId(letter.getLetterId());
			response.setLetterType(letter.getLetterType());
			response.setFromAuthorId(letter.getFromAuthorId());
			response.setFromAuthorNickname(letter.getFromAuthorNickname());
			response.setToAuthorId(letter.getToAuthorId());
			response.setToAuthorNickname(letter.getToAuthorNickname());
			response.setContent(letter.getContent());
			response.setWrittenTime(letter.getWrittenTime().getTime());
			responses.add(response);
		}

		return ApiResponse.make(OdResponseType.OK, responses);
	}

	@DeleteMapping(value = "/{letterId}")
	public ResponseEntity<ApiResponse<Object>> deleteLetter(@PathVariable(name = "letterId") String letterId) throws OdException {
		
		AuthorLetter letter = authorLetterRepository.findByLetterId(letterId);
		if (letter == null || letter.isDeleted()) {
			throw new OdException(OdResponseType.LETTER_NOT_FOUND);
		}
		letter.setDeleted(true);
		authorLetterRepository.save(letter);
		return ApiResponse.make(OdResponseType.OK);
	}
}
