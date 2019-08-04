package org.jerrioh.diary.controller.author;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.jerrioh.common.OdMessageSource;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.common.util.StringUtil;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.Nickname;
import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.AuthorDiaryRepository;
import org.jerrioh.diary.domain.repo.AuthorLetterRepository;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupAuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupRepository;
import org.jerrioh.diary.domain.repo.FeedbackAuthorRepository;
import org.jerrioh.diary.domain.repo.FeedbackDiaryRepository;
import org.jerrioh.diary.domain.repo.NicknameRepository;
import org.jerrioh.diary.domain.repo.PostRepository;
import org.jerrioh.security.authentication.after.CompleteAuthorToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractAuthorController extends AbstractController {
	@Autowired
	protected AuthorRepository authorRepository;
	@Autowired
	protected AuthorAnalyzedRepository authorAnalyzedRepository;
	@Autowired
	protected AuthorDiaryRepository authorDiaryRepository;
	@Autowired
	protected AuthorLetterRepository authorLetterRepository;
	@Autowired
	protected DiaryGroupRepository diaryGroupRepository;
	@Autowired
	protected DiaryGroupAuthorRepository diaryGroupAuthorRepository;
	@Autowired
	protected FeedbackDiaryRepository feedbackDiaryRepository;
	@Autowired
	protected FeedbackAuthorRepository  feedbackAuthorRepository;
	@Autowired
	protected PostRepository postRepository;
	@Autowired
	protected NicknameRepository nicknameRepository;
	@Autowired
	protected OdMessageSource messageSource;

	protected Author getAuthor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof CompleteAuthorToken && authentication.isAuthenticated()) {
			return (Author) authentication.getPrincipal();
		}
		throw new OdAuthenticationException();		
	}

	protected void checkInvalidDate(String diaryDate, long dayOffset) {
		ZonedDateTime minGmtDate = ZonedDateTime.now(ZoneOffset.MIN);
		ZonedDateTime maxGmtDate = ZonedDateTime.now(ZoneOffset.MAX);
		
		minGmtDate = minGmtDate.plusDays(dayOffset);
		maxGmtDate = maxGmtDate.plusDays(dayOffset);
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		int minimumPermit = Integer.parseInt(minGmtDate.format(dateTimeFormatter));
		int maximumPermit = Integer.parseInt(maxGmtDate.format(dateTimeFormatter));
		int diaryDateInteger = Integer.parseInt(diaryDate);
		if (diaryDateInteger < minimumPermit || maximumPermit < diaryDateInteger) {
			OdLogger.info("invalid diaryDate. diaryDate = {} ", diaryDate);
			throw new OdAuthenticationException();
		}
	}

	protected DateTimeZone getDateTimeZone(String timeZoneId) throws OdException {
		try {
			return DateTimeZone.forID(timeZoneId);
		} catch (IllegalArgumentException e) {
			OdLogger.error("IllegalArgumentException occured. message = {}", e.getMessage());
			throw new OdException(OdResponseType.BAD_REQUEST);
		}
	}

	protected DiaryGroup getStartedDiaryGroup(Author author) throws OdException {
		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedByAuthorId(author.getAuthorId());
		if (diaryGroup == null) {
			throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
		}
		
		DateTime startDateTime = new DateTime(diaryGroup.getStartTime());
		DateTime now = DateTime.now();
		if (startDateTime.compareTo(now) > 0) { // 시작 전
			OdLogger.info("Diary group is not started. startDateTime = {}, now = {}", startDateTime, now);
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}
		return diaryGroup;
	}
	

	protected String generateAuthorCode() {
		String authorCode = RandomStringUtils.randomAlphanumeric(16); // 16자리 코드
		return authorCode;
	}

	protected String generateNickname(String language) {
		// TODO 거의 중복되지 않을 정도의 충분한 조합 필요
		// 동물
		// 개 : 삽살개, 시바견
		// 도마뱀 : 코모도새끼도마뱀, 코모도왕대왕
		// 사자 : 왕중의왕 사자, 정글의왕 사자, 밀림의왕 사자
		// 원숭이 : 긴팔원숭이, 일본원숭이, 
		// 토끼 : 
		// 고릴라 : 동킹콩킹
		// 드래곤 : 화염드래곤
		// 바퀴벌레
		//
		// 사람
		// 동네사람 : 형, 할아버지, 할머니, 아주머니, 아저씨, 형
		// 프로그래머 : 미쳐버린 코더, 정의의 해커, 
		//
		// 위대한사람
		// 오정욱 : 천재, 미친천재
		//
		// 기계
		// 변신로봇 : 2단변신, 3단변신, 4단변신, 5단변신, 6단변신, 7단변신, 8단변신, 9단변신, 10단변신, 11단변신
		// 
		// 무생물
		// 배설물 : 더러운똥오줌, 구토잔해물
		
		Nickname nickname = nicknameRepository.findNotUsedOne("kor".equals(language) ? "kor" : "eng");
		if (nickname == null) {
			return "unknown";
		} else {
			return nickname.getNickname();
		}
	}

	protected String generateDescription() {
		// TODO author의 상태를 기준으로 생성된다. 앱의 핵심기능 중 하나. 하루에 한번 가능.
		return StringUtil.randomString("일기를 거의 안 쓰는 사람",
				"쉬지않고 일만 하는 사람",
				"움직이지 않고 밥만 먹는 사람",
				"초콜렛 기부왕",
				"똑똑한 사람",
				"초콜렛 기부왕");
	}
	
	protected String talkAboutYou() {
		// TODO author의 상태를 기준으로 생성된다. 앱의 핵심기능 중 하나. 하루에 한번 가능.
//		String aboutYou = "당신은 아름답다. 일기는 좀 더 써야 한다.";
		return StringUtil.randomString("당신은 일기를 좀 더 써야된다.",
				"일기모임에 참여해보세요. 등등",
				"당신은 사실 멍청합니다. 사람들은 당신이 멍청하다고 생각합니다.");
	}

	protected String generatePostId() {
		return UUID.randomUUID().toString();
	}
}
