package org.jerrioh.diary.controller.author;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.common.util.StringUtil;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.AuthorDiaryRepository;
import org.jerrioh.diary.domain.repo.AuthorLetterRepository;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupAuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupRepository;
import org.jerrioh.diary.domain.repo.PostRepository;
import org.jerrioh.security.authentication.after.CompleteAuthorToken;
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
	protected PostRepository postRepository;

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
		// TODO 거의 중복되지 않을 정도의 충분한 조합 필요
		return StringUtil.randomString("멍청이", "천재오정욱", "바퀴벌레", "똥오줌", "원숭이", "고릴라", "동네바보", "변신로봇");
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
