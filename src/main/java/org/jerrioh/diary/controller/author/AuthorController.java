package org.jerrioh.diary.controller.author;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.diary.controller.author.payload.AuthorRequest;
import org.jerrioh.diary.controller.author.payload.AuthorResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author")
public class AuthorController extends AbstractAuthorController {
	
	@PostMapping(value = "/create")
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
		authorRepository.save(author);
		
		AuthorResponse response = authorToResponse(author);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<AuthorResponse>> authorInfo() {
		Author author = super.getAuthor();
		AuthorResponse response = authorToResponse(author);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@PostMapping(value = "/change-nick")
	public ResponseEntity<ApiResponse<AuthorResponse>> changeNickname() {
		Author author = super.getAuthor();
		author.setNickname(generateNickName());
		authorRepository.save(author);
		
		AuthorResponse response = authorToResponse(author);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	private AuthorResponse authorToResponse(Author author) {
		AuthorResponse response = new AuthorResponse();
		response.setAuthorId(author.getAuthorId());
		response.setAuthorCode(author.getAuthorCode());
		response.setNickname(author.getNickname());
		response.setDescription(author.getDescription());
		return response;
	}

	private String generateAuthorCode() {
		String authorCode = RandomStringUtils.randomAlphanumeric(16); // 16자리 코드
		return authorCode;
	}

	private String generateNickName() {
		String nickname = "genius OJW - " + RandomUtils.nextInt(0, 10); // 거의 중복되지 않을 정도의 충분한 조합
		return nickname;
	}

	private String generateDescription() {
		String description = "Super genius - " + RandomUtils.nextInt(0, 10); // author의 상태를 기준으로 생성된다. 앱의 핵심기능 중 하나. 하루에 한번 가능.
		return description;
	}
}
