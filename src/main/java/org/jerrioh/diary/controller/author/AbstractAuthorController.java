package org.jerrioh.diary.controller.author;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.AuthorDiaryRepository;
import org.jerrioh.diary.domain.repo.AuthorLetterRepository;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupAuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupRepository;
import org.jerrioh.security.authentication.after.CompleteAuthorToken;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractAuthorController extends AbstractController {
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

	protected Author getAuthor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof CompleteAuthorToken && authentication.isAuthenticated()) {
			return (Author) authentication.getPrincipal();
		}
		throw new OdAuthenticationException();		
	}

	protected DateTimeZone getDateTimeZone(String timeZoneId) throws OdException {
		try {
			return DateTimeZone.forID(timeZoneId);
		} catch (IllegalArgumentException e) {
			OdLogger.error("IllegalArgumentException occured. message = {}", e.getMessage());
			throw new OdException(OdResponseType.BAD_REQUEST);
		}
	}

	protected String generateAuthorCode() {
		String authorCode = RandomStringUtils.randomAlphanumeric(16); // 16자리 코드
		return authorCode;
	}

	protected String generateNickName() {
		String nickname = "genius OJW - " + RandomUtils.nextInt(0, 10); // 거의 중복되지 않을 정도의 충분한 조합
		return nickname;
	}

	protected String generateDescription() {
		String description = "Super genius - " + RandomUtils.nextInt(0, 10); // author의 상태를 기준으로 생성된다. 앱의 핵심기능 중 하나. 하루에 한번 가능.
		return description;
	}
	
	protected String talkAboutYou() {
		String aboutYou = "당신은 아름답다. 일기는 좀 더 써야 한다.";
		return aboutYou;
	}
}
