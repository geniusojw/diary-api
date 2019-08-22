package org.jerrioh.diary.controller.author;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.FeedbackAuthorRequest;
import org.jerrioh.diary.controller.author.payload.FeedbackAuthorTypeRequest;
import org.jerrioh.diary.controller.author.payload.FeedbackDiaryRequest;
import org.jerrioh.diary.controller.author.payload.GetFeedbackTypeResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.Feedback;
import org.jerrioh.diary.domain.FeedbackAuthor;
import org.jerrioh.diary.domain.FeedbackDiary;
import org.jerrioh.diary.domain.FeedbackDiary.FeedbackDiaryType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/feedback")
public class AuthorDiaryGroupFeedbackController extends AbstractAuthorController {
	
	private long authorTypeTime = 0L;
	private List<Integer> authorTypes = null;
	private Random random = new Random();
	
	private Feedback[] feedbacks;
	
	@PostMapping("/author/types")
	public ResponseEntity<ApiResponse<GetFeedbackTypeResponse>> getRandomFeebackTypes(
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestBody @Valid FeedbackAuthorTypeRequest request) throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = super.getStartedDiaryGroup(author);

		this.checkToAuthorId(author.getAuthorId(), request.getToAuthorId(), diaryGroup.getDiaryGroupId());
		
		FeedbackAuthor feedback = feedbackAuthorRepository.findByFromAuthorIdAndToAuthorIdAndDiaryGroupId(
				author.getAuthorId(), request.getToAuthorId(), diaryGroup.getDiaryGroupId());
		
		if (feedback != null) {
			throw new OdException(OdResponseType.FEEDBACK_CONFLICT);
		}
		
		List<Integer> aboutTypes = this.getRandomAuthorTypes();

		int type0Index = aboutTypes.get(0);
		int type1Index = aboutTypes.get(1);
		int type2Index = aboutTypes.get(2);
		
		GetFeedbackTypeResponse response = new GetFeedbackTypeResponse();
		response.setAuthorType0(type0Index + 1); // type은 1부터 시작이다. 0은 직접입력
		response.setAuthorType1(type1Index + 1);
		response.setAuthorType2(type2Index + 1);
		
		if ("kor".equals(language)) {
			response.setAuthorTypeDescription0(feedbacks[type0Index].getKoreanDescription());
			response.setAuthorTypeDescription1(feedbacks[type1Index].getKoreanDescription());
			response.setAuthorTypeDescription2(feedbacks[type2Index].getKoreanDescription());
		} else {
			response.setAuthorTypeDescription0(feedbacks[type0Index].getEnglishDescription());
			response.setAuthorTypeDescription1(feedbacks[type1Index].getEnglishDescription());
			response.setAuthorTypeDescription2(feedbacks[type2Index].getEnglishDescription());
		}
		
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/author")
	public ResponseEntity<ApiResponse<Object>> feedbackAuthor(@RequestBody @Valid FeedbackAuthorRequest request) throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = super.getStartedDiaryGroup(author);

		this.checkToAuthorId(author.getAuthorId(), request.getToAuthorId(), diaryGroup.getDiaryGroupId());
		
		FeedbackAuthor feedbackAuthor = feedbackAuthorRepository.findByFromAuthorIdAndToAuthorIdAndDiaryGroupId(
				author.getAuthorId(), request.getToAuthorId(), diaryGroup.getDiaryGroupId());
		
		if (feedbackAuthor != null) {
			throw new OdException(OdResponseType.FEEDBACK_CONFLICT);
		}
		
		feedbackAuthor = new FeedbackAuthor();
		feedbackAuthor.setFromAuthorId(author.getAuthorId());
		feedbackAuthor.setToAuthorId(request.getToAuthorId());
		feedbackAuthor.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		feedbackAuthor.setFeedbackAuthorType(request.getFeedbackAuthorType());
		feedbackAuthor.setFeedbackAuthorWrite(request.getFeedbackAuthorWrite());
		feedbackAuthorRepository.save(feedbackAuthor);
		
		// 0: 직접작성, 1 이상 : 타입선택
		if (request.getFeedbackAuthorType() > 0) {
			Feedback feedback = feedbacks[request.getFeedbackAuthorType() - 1];
			
			AuthorAnalyzed authorAnalyzed = authorAnalyzedRepository.findByAuthorId(author.getAuthorId());
			if (authorAnalyzed != null) {
				authorAnalyzed.setFactorNeuroticism(authorAnalyzed.getFactorNeuroticism() + feedback.getFactorNeuroticism());
				authorAnalyzed.setFactorExtraversion(authorAnalyzed.getFactorExtraversion() + feedback.getFactorExtraversion());
				authorAnalyzed.setFactorOpenness(authorAnalyzed.getFactorOpenness() + feedback.getFactorOpenness());
				authorAnalyzed.setFactorAgreeableness(authorAnalyzed.getFactorAgreeableness() + feedback.getFactorAgreeableness());
				authorAnalyzed.setFactorConscientiousness(authorAnalyzed.getFactorConscientiousness() + feedback.getFactorConscientiousness());
				authorAnalyzedRepository.save(authorAnalyzed);
			}
		}
		
		return ApiResponse.make(OdResponseType.OK);
	}
	
	@PostMapping(value = "/diary")
	public ResponseEntity<ApiResponse<Object>> feedbackDiary(@RequestBody @Valid FeedbackDiaryRequest request) throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = super.getStartedDiaryGroup(author);

		this.checkToAuthorId(author.getAuthorId(), request.getToAuthorId(), diaryGroup.getDiaryGroupId());
		
		long offsetDays = 0L;
		if (request.getFeedbackDiaryType() == FeedbackDiaryType.GOOD) {
			offsetDays = -1L;
		}
		super.checkInvalidDate(request.getDiaryDate(), offsetDays);
		
		FeedbackDiary feedback = feedbackDiaryRepository.findByFromAuthorIdAndToAuthorIdAndAndDiaryDateAndFeedbackDiaryType(
				author.getAuthorId(), request.getToAuthorId(), request.getDiaryDate(), request.getFeedbackDiaryType());
		
		if (feedback != null) {
			throw new OdException(OdResponseType.FEEDBACK_CONFLICT);
		}
		
		feedback = new FeedbackDiary();
		feedback.setFromAuthorId(author.getAuthorId());
		feedback.setToAuthorId(request.getToAuthorId());
		feedback.setDiaryDate(request.getDiaryDate());
		feedback.setFeedbackDiaryType(request.getFeedbackDiaryType());
		feedbackDiaryRepository.save(feedback);
		
		return ApiResponse.make(OdResponseType.OK);
	}

	private void checkToAuthorId(String fromAuthorId, String toAuthorId, Long diaryGroupId) throws OdException {
		if (fromAuthorId.equals(toAuthorId)) {
			throw new OdException(OdResponseType.BAD_REQUEST);
		}
		
		List<DiaryGroupAuthor> diaryGroupAuthors = diaryGroupAuthorRepository.findByDiaryGroupId(diaryGroupId);
		if (!diaryGroupAuthors.stream().map(DiaryGroupAuthor::getAuthorId).collect(Collectors.toSet()).contains(toAuthorId)) {
			throw new OdException(OdResponseType.USER_NOT_FOUND);
		}
	}
	
	// Cache 수동 구현. Cacheable 동작하지 않는 이유는?
	private List<Integer> getRandomAuthorTypes() throws OdException {
		if (feedbacks == null) {
			feedbacks = feedbackRepository.findAll().toArray(new Feedback[] {});
		}
		
		long currentTime = System.currentTimeMillis();
		if (currentTime < this.authorTypeTime + TimeUnit.MINUTES.toMillis(1)) {
			return this.authorTypes;
		}
		
		List<Integer> authorTypes = new ArrayList<>();
		int tryCount = 0;
		while (authorTypes.size() < 3 && tryCount < 50) {
			int randomAboutType = random.nextInt(feedbacks.length);
			if (!authorTypes.contains(randomAboutType)) {
				authorTypes.add(randomAboutType);
			}
			tryCount++;
		}
		if (authorTypes.size() != 3) {
			throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
		}
		this.authorTypes = authorTypes;
		this.authorTypeTime = currentTime;
		return authorTypes;
	}
}
