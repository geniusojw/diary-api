package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.author.payload.AuthorLetterRequest;
import org.jerrioh.diary.controller.author.payload.AuthorLetterResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorLetter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/letters")
public class AuthorLetterController extends AbstractAuthorController {

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> send(@RequestBody @Valid AuthorLetterRequest request) throws OdException {
		Author author = super.getAuthor();
		AuthorLetter letter = authorLetterRepository.findByLetterId(request.getLetterId());
		if (letter != null) {
			throw new OdException(OdResponseType.LETTER_CONFLICT);
		}
		Author toAuthor = authorRepository.findByAuthorId(request.getToAuthorId());
		if (toAuthor == null) {
			throw new OdException(OdResponseType.USER_NOT_FOUND);
		}

		letter = new AuthorLetter();
		letter.setLetterId(request.getLetterId());
		letter.setFromAuthorId(author.getAuthorId());
		letter.setToAuthorId(request.getToAuthorId());
		letter.setTitle(request.getTitle());
		letter.setContent(request.getContent());
		letter.setWrittenTime(new Timestamp(System.currentTimeMillis()));

		authorLetterRepository.save(letter);

		return ApiResponse.make(OdResponseType.OK);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AuthorLetterResponse>>> recieveAll() {
		Author author = super.getAuthor();
		List<AuthorLetter> letters = authorLetterRepository.findByToAuthorId(author.getAuthorId());

		List<AuthorLetterResponse> responses = new ArrayList<>();
		for (AuthorLetter letter : letters) {
			Author fromAuthor = authorRepository.findByAuthorId(letter.getFromAuthorId());
			String fromAuthorNickname = fromAuthor != null ? fromAuthor.getNickname() : "unknown";
			
			AuthorLetterResponse response = new AuthorLetterResponse();
			response.setLetterId(letter.getLetterId());
			response.setFromAuthorId(letter.getFromAuthorId());
			response.setFromAuthorNickname(fromAuthorNickname);
			response.setTitle(letter.getTitle());
			response.setContent(letter.getContent());
			response.setWrittenTime(letter.getWrittenTime().getTime());
			responses.add(response);
		}

		return ApiResponse.make(OdResponseType.OK, responses);
	}
}
