package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.PostRequest;
import org.jerrioh.diary.controller.author.payload.PostResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.Post;
import org.jerrioh.diary.domain.Post.PostStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/posts")
public class PostController extends AbstractAuthorController {

	@GetMapping(value = "/has-post")
	public ResponseEntity<ApiResponse<PostResponse>> hasPost() throws OdException {
		Author author = super.getAuthor();
		Post post = postRepository.findMyNotPosted(author.getAuthorId());
		if (post == null) {
			throw new OdException(OdResponseType.POST_NOT_FOUND);
		}

		PostResponse response = new PostResponse();
		response.setPostId(post.getPostId());
		response.setAuthorNickname(post.getAuthorNickname());
		response.setChocolates(post.getChocolates());
		response.setContent(post.getContent());
		response.setWrittenTime(0L);
		
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> post(@RequestBody @Valid PostRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestHeader(value = OdHeaders.COUNTRY) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID) String timeZoneId) throws OdException {
		Author author = super.getAuthor();
		Post post = postRepository.findMyNotPosted(author.getAuthorId());
		if (post == null) {
			throw new OdException(OdResponseType.POST_NOT_FOUND);
		}

		post.setPostStatus(PostStatus.POSTED);
		post.setAuthorNickname(author.getNickname());
		post.setContent(request.getContent());
		post.setLanguage(language);
		post.setCountry(country);
		post.setTimeZoneId(timeZoneId);
		post.setWrittenTime(new Timestamp(System.currentTimeMillis()));
		
		postRepository.save(post);

		return ApiResponse.make(OdResponseType.OK);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PostResponse>>> getPosts(
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestHeader(value = OdHeaders.COUNTRY) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID) String timeZoneId) throws OdException {
		Timestamp yesterday = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
		List<Post> posts = postRepository.findTodaysPosts(yesterday, language, country, timeZoneId);
		
		List<PostResponse> responses = new ArrayList<>();
		for (Post post : posts) {
			PostResponse response = new PostResponse();
			response.setPostId(post.getPostId());
			response.setAuthorNickname(post.getAuthorNickname());
			response.setChocolates(post.getChocolates());
			response.setContent(post.getContent());
			response.setWrittenTime(post.getWrittenTime().getTime());
			responses.add(response);
		}

		return ApiResponse.make(OdResponseType.OK, responses);
	}

}
