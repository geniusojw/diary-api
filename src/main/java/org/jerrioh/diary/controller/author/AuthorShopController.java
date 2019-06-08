package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.author.payload.ChangeDescriptionResponse;
import org.jerrioh.diary.controller.author.payload.ChangeNicknameResponse;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.InviteTicketRequest;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/shop")
public class AuthorShopController extends AbstractAuthorController {
	
	public static final int CHANGE_DESCRIPTION_CHOCOLATES = 1;
	public static final int CHANGE_NICKNAME_CHOCOLATES = 1;
	public static final int ALIAS_FEATURE_UNLIMITED_USE_CHOCOLATES = 5;
	public static final int INVITE_TICKET1_CHOCOLATES = 10;
	public static final int INVITE_TICKET2_CHOCOLATES = 20;

	@PostMapping(value = "/change-description")
	public ResponseEntity<ApiResponse<ChangeDescriptionResponse>> changeDescription() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, CHANGE_DESCRIPTION_CHOCOLATES);

		String aboutYou = talkAboutYou();
		String description = generateDescription();
		author.setDescription(description);
		authorRepository.save(author);
		
		this.payChocolates(author, CHANGE_DESCRIPTION_CHOCOLATES);

		ChangeDescriptionResponse response = new ChangeDescriptionResponse();
		response.setAboutYou(aboutYou);
		response.setDescription(description);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/change-nickname")
	public ResponseEntity<ApiResponse<ChangeNicknameResponse>> changeNickname() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, CHANGE_NICKNAME_CHOCOLATES);

		String nickname = generateNickName();
		author.setNickname(nickname);
		authorRepository.save(author);
		
		this.payChocolates(author, CHANGE_NICKNAME_CHOCOLATES);

		ChangeNicknameResponse response = new ChangeNicknameResponse();
		response.setNickname(nickname);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/alias-feature-unlimited-use")
	public ResponseEntity<ApiResponse<Object>> aliasFeatureUnlimitedUse() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, ALIAS_FEATURE_UNLIMITED_USE_CHOCOLATES);
		this.payChocolates(author, ALIAS_FEATURE_UNLIMITED_USE_CHOCOLATES);

		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional
	@PostMapping(value = "/invite-ticket1")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> invite1(@RequestBody @Valid InviteTicketRequest request) throws OdException {
		return invite(request, INVITE_TICKET1_CHOCOLATES, 3);
	}

	@Transactional
	@PostMapping(value = "/invite-ticket2")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> invite2(@RequestBody @Valid InviteTicketRequest request) throws OdException {
		return invite(request, INVITE_TICKET2_CHOCOLATES, 6);
	}

	private ResponseEntity<ApiResponse<DiaryGroupResponse>> invite(InviteTicketRequest request, int numberOfChocolatesRequired, int maxAuthorCount) throws OdException {
		Author author = super.getAuthor();
		DateTimeZone dateTimeZone = super.getDateTimeZone(request.getTimeZoneId());
		this.throwExceptionIfHasNotEnoughChocolates(author, numberOfChocolatesRequired);
		DiaryGroup diaryGroup = diaryGroupRepository.findByAuthorId(author.getAuthorId());
		if (diaryGroup != null) {
			throw new OdException(OdResponseType.DIARY_GROUP_CONFLICT);
		}

		DateTime dateTime = new DateTime().withZone(dateTimeZone);
		int plusStartDays = 1;
		int durationDays = 3;
		if (TimeUnit.HOURS.convert(dateTime.getMillisOfDay(), TimeUnit.MILLISECONDS) > 12) {
			plusStartDays++;
		}
		DateTime startDateTime = dateTime.plusDays(plusStartDays).withMillisOfDay(0);
		DateTime endDateTime = startDateTime.plusDays(durationDays);

		diaryGroup = new DiaryGroup();
		diaryGroup.setHostAuthorId(author.getAuthorId());
		diaryGroup.setDiaryGroupName(request.getDiaryGroupName());
		diaryGroup.setKeyword(request.getKeyword());
		diaryGroup.setMaxAuthorCount(maxAuthorCount);
		diaryGroup.setLanguage(request.getLanguage());
		diaryGroup.setCountry(request.getCountry());
		diaryGroup.setTimeZoneId(request.getTimeZoneId());
		diaryGroup.setStartTime(new Timestamp(startDateTime.getMillis()));
		diaryGroup.setEndTime(new Timestamp(endDateTime.getMillis()));

		diaryGroupRepository.save(diaryGroup);

		DiaryGroupAuthor diaryGroupAuthor = new DiaryGroupAuthor();
		diaryGroupAuthor.setDiaryGroupId(diaryGroup.getDiaryGroupId());
		diaryGroupAuthor.setAuthorId(diaryGroup.getHostAuthorId());
		diaryGroupAuthor.setAuthorStatus(AuthorStatus.ACCEPT);
		diaryGroupAuthorRepository.save(diaryGroupAuthor);

		this.payChocolates(author, numberOfChocolatesRequired);

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
	
	private void payChocolates(Author author, int numberOfChocolatesRequired) throws OdException {
		this.throwExceptionIfHasNotEnoughChocolates(author, numberOfChocolatesRequired);
		int remainingChocolates = author.getChocolates() - numberOfChocolatesRequired;
		author.setChocolates(remainingChocolates);
		authorRepository.save(author);
	}

	private void throwExceptionIfHasNotEnoughChocolates(Author author, int numberOfChocolatesRequired) throws OdException {
		if (author.getChocolates() < numberOfChocolatesRequired) {
			OdLogger.info("not enough chocolates. author's={}, required={}", author.getChocolates(), numberOfChocolatesRequired);
			throw new OdException(OdResponseType.PAYMENT_REQUIRED);
		}
	}
}
