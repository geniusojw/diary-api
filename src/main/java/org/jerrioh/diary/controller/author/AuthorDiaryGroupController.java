
package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.Code;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.author.payload.AuthorDiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.DiaryGroupRespondRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupRespondRequest.InvitationResponseType;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.UpdateDiaryGroupRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.jerrioh.diary.domain.AuthorDiary;
import org.jerrioh.diary.domain.AuthorLetter;
import org.jerrioh.diary.domain.AuthorLetter.LetterType;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/diary-group")
public class AuthorDiaryGroupController extends AbstractAuthorController {
	
	private static final int NUMBER_OF_DIARIES_MINIMUM_REQUIREMENT_2 = 2;

	@GetMapping
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> getDiaryGroup() throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		List<DiaryGroupAuthor> diaryGroupAuthors = diaryGroupAuthorRepository.findByDiaryGroupId(diaryGroup.getDiaryGroupId());
		int currentAuthorCount = diaryGroupAuthors.size();

		DiaryGroupResponse response = new DiaryGroupResponse();
		response.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		response.setDiaryGroupName(diaryGroup.getDiaryGroupName());
		response.setHostAuthorId(diaryGroup.getHostAuthorId());
		response.setKeyword(diaryGroup.getKeyword());
		response.setCurrentAuthorCount(currentAuthorCount);
		response.setMaxAuthorCount(diaryGroup.getMaxAuthorCount());
		response.setLanguage(diaryGroup.getLanguage());
		response.setCountry(diaryGroup.getCountry());
		response.setTimeZoneId(diaryGroup.getTimeZoneId());
		response.setStartTime(diaryGroup.getStartTime().getTime());
		response.setEndTime(diaryGroup.getEndTime().getTime());
		
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Object>> updateDiaryGroup(@RequestBody @Valid UpdateDiaryGroupRequest request) throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		if (!StringUtils.equals(author.getAuthorId(), diaryGroup.getHostAuthorId())) {
			throw new OdException(OdResponseType.FORBIDDEN);
		}
		
		diaryGroup.setKeyword(request.getKeyword());
		diaryGroupRepository.save(diaryGroup);
		
		return ApiResponse.make(OdResponseType.OK);
	}
	
	@PostMapping("/be-invited")
	public ResponseEntity<ApiResponse<Object>> beInvited() throws OdException {
		Author author = super.getAuthor();

		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedOrAcceptableByAuthorId(author.getAuthorId());
		if (diaryGroup != null) {
			throw new OdException(OdResponseType.DIARY_GROUP_CONFLICT);
		}
		
		// 7일간 2개이상의 일기를 작성했어야 초대될 수 있다.
		List<AuthorDiary> authorDiaries = authorDiaryRepository.findByAuthorDiaryRecent10Days(author.getAuthorId());
		if (authorDiaries.size() < NUMBER_OF_DIARIES_MINIMUM_REQUIREMENT_2) {
			OdLogger.debug("The recent number of diaries is too small. authorDiaries.size() = {}", authorDiaries.size());
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		AuthorAnalyzed authorAnalyzed = authorAnalyzedRepository.findByAuthorId(author.getAuthorId());
		if (authorAnalyzed == null) {
			OdLogger.debug("Not enough information");
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		// 일기그룹을 검색한다.
		// TODO language, country, 특정기준 order by start desc
		diaryGroup = diaryGroupRepository.findInviteDiaryGroup(authorAnalyzed.getLanguage(), authorAnalyzed.getCountry(), authorAnalyzed.getTimeZoneId());
		if (diaryGroup == null) {
			OdLogger.debug("Not found. lanuage = {}, country = {}, timeZoneId = {}", authorAnalyzed.getLanguage(), authorAnalyzed.getCountry(), authorAnalyzed.getTimeZoneId());
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		this.beInvitedAndReceiveLetter(author, diaryGroup);

		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/respond")
	public ResponseEntity<ApiResponse<Object>> respond(@RequestBody @Valid DiaryGroupRespondRequest request) throws OdException {
		Author author = super.getAuthor();

		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedOrAcceptableByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		DiaryGroupAuthor diaryGroupAuthor = diaryGroupAuthorRepository.findByDiaryGroupIdAndAuthorId(diaryGroup.getDiaryGroupId(), author.getAuthorId());
		if (diaryGroupAuthor.getAuthorStatus() != AuthorStatus.INVITE) {
			OdLogger.info("Author status is not INVITE. authorStatus = {}", diaryGroupAuthor.getAuthorStatus());
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		if (InvitationResponseType.ACCEPT.equals(request.getInvitationResponseType())) {
			diaryGroupAuthor.setAuthorStatus(AuthorStatus.ACCEPT);
		} else if (InvitationResponseType.REFUSE.equals(request.getInvitationResponseType())) {
			diaryGroupAuthor.setAuthorStatus(AuthorStatus.REFUSE);
		} else {
			OdLogger.info("Unexpected invitation response type. request.getInvitationResponseType()={}", request.getInvitationResponseType());
			throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
		}
		
		diaryGroupAuthorRepository.save(diaryGroupAuthor);
		
		return ApiResponse.make(OdResponseType.OK);
	}
	
	@GetMapping("/yesterday-diaries")
	public ResponseEntity<ApiResponse<List<AuthorDiaryGroupResponse>>> readYesterdayDiaries() throws OdException {
		Author author = super.getAuthor();
		DiaryGroup diaryGroup = getStartedDiaryGroup(author);
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime today = DateTime.now(DateTimeZone.forID(diaryGroup.getTimeZoneId()));
		DateTime yesterdayDate = today.minusDays(1);
		String todayString = today.toString(dateTimeFormatter);
		String yesterdayString = yesterdayDate.toString(dateTimeFormatter);
		
		String firstDayString = new DateTime(diaryGroup.getStartTime()).withZone(DateTimeZone.forID(diaryGroup.getTimeZoneId())).toString(dateTimeFormatter);
		boolean isFirstDay = firstDayString.contentEquals(todayString);
		
		// TODO one to many, many to one 적용 -> performance 개선
		List<AuthorDiaryGroupResponse> responses = new ArrayList<>();
		
		List<DiaryGroupAuthor> diaryGroupAuthors = diaryGroupAuthorRepository.findByDiaryGroupId(diaryGroup.getDiaryGroupId());
		for (DiaryGroupAuthor diaryGroupAuthor : diaryGroupAuthors) {
			if (diaryGroupAuthor.getAuthorStatus() != AuthorStatus.ACCEPT) {
				continue;
			}
			
			Author authorParticipated = authorRepository.findByAuthorId(diaryGroupAuthor.getAuthorId());
			if (authorParticipated == null || authorParticipated.isDeleted()) {
				continue;
			}

			AuthorDiaryGroupResponse response = new AuthorDiaryGroupResponse();
			response.setAuthorId(authorParticipated.getAuthorId());
			response.setNickname(authorParticipated.getNickname());
			response.setFirstDay(isFirstDay);
			
			AuthorDiary todayDiary = authorDiaryRepository.findByAuthorIdAndDiaryDate(authorParticipated.getAuthorId(), todayString);
			if (todayDiary != null && !todayDiary.isDeleted()) {
				response.setTodayDate(todayDiary.getDiaryDate());
				response.setTodayTitle(todayDiary.getTitle());
				response.setTodayContent(todayDiary.getContent());
			}
			
			if (!isFirstDay) {
				AuthorDiary yesterdayDiary = authorDiaryRepository.findByAuthorIdAndDiaryDate(authorParticipated.getAuthorId(), yesterdayString);
				if (yesterdayDiary != null && !yesterdayDiary.isDeleted()) {
					response.setYesterdayDate(yesterdayDiary.getDiaryDate());
					response.setYesterdayTitle(yesterdayDiary.getTitle());
					response.setYesterdayContent(yesterdayDiary.getContent());
				}
			}
			responses.add(response);
		}
		
		return ApiResponse.make(OdResponseType.OK, responses);
	}

	private void beInvitedAndReceiveLetter(Author author, DiaryGroup diaryGroup) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.fullDateTime().withZone(DateTimeZone.forID(diaryGroup.getTimeZoneId()));
		long durationDays = (diaryGroup.getEndTime().getTime() - diaryGroup.getStartTime().getTime()) / TimeUnit.DAYS.toMillis(1);
		
		String letterContent;
		
		if ("kor".equals(diaryGroup.getLanguage())) {
			dateTimeFormatter = dateTimeFormatter.withLocale(Locale.KOREA);
			String startTime = dateTimeFormatter.print(diaryGroup.getStartTime().getTime());
			String endTime = dateTimeFormatter.print(diaryGroup.getEndTime().getTime());
			
			letterContent = "당신은 초대당했다.\n수락할 경우 " + durationDays + "일 간의 그룹일기에 참여하게 됩니다. 그룹일기는 특정키워드가 주어지며... blah blah.. " + startTime + " ~ " + endTime;
			
		} else {
			dateTimeFormatter = dateTimeFormatter.withLocale(Locale.US);
			String startTime = dateTimeFormatter.print(diaryGroup.getStartTime().getTime());
			String endTime = dateTimeFormatter.print(diaryGroup.getEndTime().getTime());
			
			letterContent = "You are invited.\nIf 수락, " + durationDays + "일 간의 그룹일기에 참여하게 됩니다. 그룹일기는 특정키워드가 주어지며... blah blah.." + startTime + " ~ " + endTime;
		}

		long currentTime = System.currentTimeMillis();

		// 일기그룹 초대편지 발송
		AuthorLetter inviteLetter = new AuthorLetter();
		inviteLetter.setLetterId(diaryGroup.getHostAuthorId() + "-" + currentTime);
		inviteLetter.setLetterType(LetterType.INVITATION);
		inviteLetter.setFromAuthorId(Code.SYSTEM_AUTHOR_ID);
		inviteLetter.setFromAuthorNickname(Code.SYSTEM_AUTHOR_NICKNAME);
		inviteLetter.setToAuthorId(author.getAuthorId());
		inviteLetter.setToAuthorNickname(author.getNickname());
		inviteLetter.setContent(letterContent);
		inviteLetter.setWrittenTime(new Timestamp(currentTime));
		authorLetterRepository.save(inviteLetter);
		
		// 일기그룹 초대
		DiaryGroupAuthor diaryGroupAuthor = new DiaryGroupAuthor();
		diaryGroupAuthor.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		diaryGroupAuthor.setAuthorId(author.getAuthorId());
		diaryGroupAuthor.setAuthorStatus(AuthorStatus.INVITE);
		diaryGroupAuthorRepository.save(diaryGroupAuthor);
	}
}
