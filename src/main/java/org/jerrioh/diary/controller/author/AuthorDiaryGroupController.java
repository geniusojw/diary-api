package org.jerrioh.diary.controller.author;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.author.payload.AuthorDiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorDiary;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/diary-group")
public class AuthorDiaryGroupController extends AbstractAuthorController {
	
	@GetMapping
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> getDiaryGroup() throws OdException {
		Author author = super.getAuthor();

		DiaryGroup diaryGroup = diaryGroupRepository.findByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}

		DiaryGroupResponse response = new DiaryGroupResponse();
		response.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		response.setDiaryGroupName(diaryGroup.getDiaryGroupName());
		response.setKeyword(diaryGroup.getKeyword());
		response.setMaxAuthorCount(diaryGroup.getMaxAuthorCount());
		response.setLanguage(diaryGroup.getLanguage());
		response.setCountry(diaryGroup.getCountry());
		response.setTimeZoneId(diaryGroup.getTimeZoneId());
		response.setStartTime(diaryGroup.getStartTime().getTime());
		response.setEndTime(diaryGroup.getEndTime().getTime());
		
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@GetMapping("/yesterday-diaries")
	public ResponseEntity<ApiResponse<List<AuthorDiaryGroupResponse>>> readYesterdayDiaries() throws OdException {
		Author author = super.getAuthor();

		DiaryGroup diaryGroup = diaryGroupRepository.findByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
		String yesterdayDate = DateTime.now(DateTimeZone.forID(diaryGroup.getTimeZoneId())).minusDays(1).toString(dateTimeFormatter);
		
		// TODO one to many, many to one 적용 -> performance 개선
		List<AuthorDiaryGroupResponse> responses = new ArrayList<>();
		List<DiaryGroupAuthor> diaryGroupAuthors = diaryGroupAuthorRepository.findByDiaryGroupId(diaryGroup.getDiaryGroupId());
		for (DiaryGroupAuthor diaryGroupAuthor : diaryGroupAuthors) {
			if (diaryGroupAuthor.getAuthorStatus() == AuthorStatus.ACCEPT) {
				Author authorParticipated = authorRepository.findByAuthorId(diaryGroupAuthor.getAuthorId());
				if (authorParticipated != null && !authorParticipated.isDeleted()) {
					AuthorDiaryGroupResponse response = new AuthorDiaryGroupResponse();
					response.setAuthorId(authorParticipated.getAuthorId());
					response.setNickname(authorParticipated.getNickname());
					AuthorDiary diary = authorDiaryRepository.findByAuthorIdAndDiaryDate(authorParticipated.getAuthorId(), yesterdayDate);
					if (diary != null) {
						response.setTitle(diary.getTitle());
						response.setContent(diary.getContent());
					}
					responses.add(response);
				}
			}
		}
		
		return ApiResponse.make(OdResponseType.OK, responses);
	}
	
	@PostMapping("/invitation")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> findDiaryGroup() throws OdException {
		Author author = super.getAuthor();

		DiaryGroup diaryGroup = diaryGroupRepository.findByAuthorId(author.getAuthorId());
		if (diaryGroup != null) {
			throw new OdException(OdResponseType.DIARY_GROUP_CONFLICT);
		}
		
		List<AuthorDiary> authorDiaries = authorDiaryRepository.findTop3ByAuthorIdOrderByDiaryDateDesc(author.getAuthorId());
		if (authorDiaries.size() < 3) {
			OdLogger.debug("The number of diaries is too small. authorDiaries.size() = {}", authorDiaries.size());
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		// TODO sort by diary date desc
		String language = null;
		String country = null;
		String timeZoneId = null;
		
		for (int i = 0; i < 3; i++) {
			AuthorDiary authorDiary = authorDiaries.get(i);
			
			if (i == 0) {
				language = authorDiary.getLanguage();
				country = authorDiary.getCountry();
				timeZoneId = authorDiary.getTimeZoneId();
			} else {
				if (!StringUtils.equals(language, authorDiary.getLanguage())
						|| !StringUtils.equals(country, authorDiary.getCountry())
						|| !StringUtils.equals(timeZoneId, authorDiary.getTimeZoneId())) {
					OdLogger.debug("There was a change in language, country, or timezone of the mobile device");
					throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);		
				}
			}

			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd").withZone(DateTimeZone.forID(timeZoneId));
			DateTime diaryDateTime = DateTime.parse(authorDiary.getDiaryDate(), dateTimeFormatter);
			DateTime tenDaysBefore = DateTime.now(DateTimeZone.forID(timeZoneId)).withMillisOfDay(0).minusDays(10);
			
			if (diaryDateTime.isBefore(tenDaysBefore)) {
				OdLogger.debug("Not enough recent diaries.");
				throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
			}
		}
		
		// TODO language, country, 특정기준 order by start desc g
		diaryGroup = diaryGroupRepository.findInviteDiaryGroup(language, country, timeZoneId);
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}

		DiaryGroupAuthor diaryGroupAuthor = new DiaryGroupAuthor();
		diaryGroupAuthor.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		diaryGroupAuthor.setAuthorId(author.getAuthorId());
		diaryGroupAuthor.setAuthorStatus(AuthorStatus.INVITE);
		diaryGroupAuthorRepository.save(diaryGroupAuthor);

		DiaryGroupResponse response = new DiaryGroupResponse();
		response.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		response.setDiaryGroupName(diaryGroup.getDiaryGroupName());
		response.setKeyword(diaryGroup.getKeyword());
		response.setMaxAuthorCount(diaryGroup.getMaxAuthorCount());
		response.setLanguage(diaryGroup.getLanguage());
		response.setCountry(diaryGroup.getCountry());
		response.setTimeZoneId(diaryGroup.getTimeZoneId());
		response.setStartTime(diaryGroup.getStartTime().getTime());
		response.setEndTime(diaryGroup.getEndTime().getTime());
		
		return ApiResponse.make(OdResponseType.OK, response);
	}
}
