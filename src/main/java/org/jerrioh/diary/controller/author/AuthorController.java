package org.jerrioh.diary.controller.author;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.author.payload.AuthorRequest;
import org.jerrioh.diary.controller.author.payload.AuthorResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author")
public class AuthorController extends AbstractAuthorController {
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/start")
	public ResponseEntity<ApiResponse<AuthorResponse>> create(@RequestBody @Valid AuthorRequest request) throws OdException {
		Author author = authorRepository.findByAuthorId(request.getAuthorId());
		if (author != null) {
			throw new OdException(OdResponseType.USER_CONFLICT);
		}
		
		author = new Author();
		author.setAuthorId(request.getAuthorId());
		author.setAuthorCode(generateAuthorCode());
		author.setNickname(generateNickName());
		author.setDescription(generateDescription());
		author.setChocolates(20);
		authorRepository.save(author);

		authorRepository.insertNickNameHistory(author.getAuthorId());
		authorRepository.insertDescriptionHistory(author.getAuthorId());
		authorRepository.insertChocolateHistory(author.getAuthorId(), 0, "start");
		
		AuthorResponse response = authorToResponse(author);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<AuthorResponse>> authorInfo() {
		Author author = super.getAuthor();
		AuthorResponse response = authorToResponse(author);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@DeleteMapping
	public ResponseEntity<ApiResponse<Object>> delete() {
		Author author = super.getAuthor();
		
		author.setDeleted(true);
		authorRepository.save(author);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	private AuthorResponse authorToResponse(Author author) {
		AuthorResponse response = new AuthorResponse();
		response.setAuthorId(author.getAuthorId());
		response.setAuthorCode(author.getAuthorCode());
		response.setNickname(author.getNickname());
		response.setDescription(author.getDescription());
		response.setChocolates(author.getChocolates());
		return response;
	}
}
