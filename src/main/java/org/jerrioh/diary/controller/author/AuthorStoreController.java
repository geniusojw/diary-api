package org.jerrioh.diary.controller.author;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.controller.author.payload.ChangeDescriptionResponse;
import org.jerrioh.diary.controller.author.payload.ChangeDiaryThemeResponse;
import org.jerrioh.diary.controller.author.payload.ChangeNicknameResponse;
import org.jerrioh.diary.controller.author.payload.ChocolateDonationRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.InviteTicketRequest;
import org.jerrioh.diary.controller.author.payload.StoreStatusResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/store")
public class AuthorStoreController extends AbstractAuthorController {
	
	private static final int DESCRIPTION_AND_NICKNAME_CHANGE_HOURS = 1;
	
	private static final Map<String, Integer> PRICE_MAP = new HashMap<>();
	
	static enum ItemPrice {
		CHANGE_DESCRIPTION			("ITEM_CHANGE_DESCRIPTION", 1),
		CHANGE_NICKNAME				("ITEM_CHANGE_NICKNAME", 1),
		CHANGE_DIARY_THEME			("ITEM_CHANGE_DIARY_THEME", 10),
		INVITE_TICKET1				("ITEM_INVITE_TICKET1", 10),
		INVITE_TICKET2				("ITEM_INVITE_TICKET2", 20),
		ALIAS_FEATURE_UNLIMITED_USE	("ITEM_ALIAS_FEATURE_UNLIMITED_USE", 5),
		CHOCOLATE_DONATION			("ITEM_CHOCOLATE_DONATION", 0);

		private ItemPrice(String itemId, int price) {
			this.itemId = itemId;
			this.price = price;
		}
		private String itemId;
		private int price;
	}
	
	static {
		for (ItemPrice productPrice : ItemPrice.values()) {
			PRICE_MAP.put(productPrice.itemId, productPrice.price);
		}
	}
	
	@GetMapping(value = "/status")
	public ResponseEntity<ApiResponse<StoreStatusResponse>> status() {
		Author author = super.getAuthor();

		for (ItemPrice productPrice : ItemPrice.values()) {
			PRICE_MAP.put(productPrice.itemId, productPrice.price);
		}
		if (authorRepository.descriptionChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			PRICE_MAP.put(ItemPrice.CHANGE_DESCRIPTION.itemId, -1);
		}
		if (authorRepository.nickNameChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			PRICE_MAP.put(ItemPrice.CHANGE_NICKNAME.itemId, -1);
		}
		DiaryGroup diaryGroup = diaryGroupRepository.findByAuthorId(author.getAuthorId());
		if (diaryGroup != null) {
			PRICE_MAP.put(ItemPrice.INVITE_TICKET1.itemId, -1);
			PRICE_MAP.put(ItemPrice.INVITE_TICKET2.itemId, -1);
		}
		
		StoreStatusResponse response = new StoreStatusResponse();
		response.setChocolates(author.getChocolates());
		response.setPriceMap(PRICE_MAP);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-description")
	public ResponseEntity<ApiResponse<ChangeDescriptionResponse>> changeDescription() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, ItemPrice.CHANGE_DESCRIPTION.price);
		
		if (authorRepository.descriptionChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			OdLogger.info("Can not change description yet.");
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}

		String aboutYou = talkAboutYou();
		String description = generateDescription();
		author.setDescription(description);
		
		authorRepository.save(author);
		authorRepository.insertDescriptionHistory(author.getAuthorId());
		
		this.payChocolates(author, ItemPrice.CHANGE_DESCRIPTION.price, ItemPrice.CHANGE_DESCRIPTION.itemId);

		ChangeDescriptionResponse response = new ChangeDescriptionResponse();
		response.setAboutYou(aboutYou);
		response.setDescription(description);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-nickname")
	public ResponseEntity<ApiResponse<ChangeNicknameResponse>> changeNickname() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, ItemPrice.CHANGE_NICKNAME.price);
		
		if (authorRepository.nickNameChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			OdLogger.info("Can not change nickname yet.");
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}

		String nickname = generateNickName();
		author.setNickname(nickname);
		
		authorRepository.save(author);
		authorRepository.insertNickNameHistory(author.getAuthorId());
		
		this.payChocolates(author, ItemPrice.CHANGE_NICKNAME.price, ItemPrice.CHANGE_NICKNAME.itemId);

		ChangeNicknameResponse response = new ChangeNicknameResponse();
		response.setNickname(nickname);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@PostMapping(value = "/change-diary-theme")
	public ResponseEntity<ApiResponse<ChangeDiaryThemeResponse>> changeDiaryTheme() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, ItemPrice.CHANGE_DIARY_THEME.price);
		
		// select random theme
		
		this.payChocolates(author, ItemPrice.CHANGE_DIARY_THEME.price, ItemPrice.CHANGE_DIARY_THEME.itemId);

		ChangeDiaryThemeResponse response = new ChangeDiaryThemeResponse();
		response.setThemeName("test theme - 1");
		response.setPattern1("pattern1");
		response.setPattern2("pattern2");
		response.setPattern3("pattern3");
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional
	@PostMapping(value = "/invite-ticket1")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> invite1(@RequestBody @Valid InviteTicketRequest request) throws OdException {
		return invite(request, ItemPrice.INVITE_TICKET1.price, ItemPrice.INVITE_TICKET1.itemId, 3);
	}

	@Transactional
	@PostMapping(value = "/invite-ticket2")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> invite2(@RequestBody @Valid InviteTicketRequest request) throws OdException {
		return invite(request, ItemPrice.INVITE_TICKET2.price, ItemPrice.INVITE_TICKET2.itemId, 6);
	}

	@PostMapping(value = "/alias-feature-unlimited-use")
	public ResponseEntity<ApiResponse<Object>> aliasFeatureUnlimitedUse() throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, ItemPrice.ALIAS_FEATURE_UNLIMITED_USE.price);
		
		// purchae umlimited
		
		this.payChocolates(author, ItemPrice.ALIAS_FEATURE_UNLIMITED_USE.price, ItemPrice.ALIAS_FEATURE_UNLIMITED_USE.itemId);

		return ApiResponse.make(OdResponseType.OK);
	}

	@PostMapping(value = "/chocolate-donation")
	public ResponseEntity<ApiResponse<Object>> chocolateDonation(@RequestBody @Valid ChocolateDonationRequest request) throws OdException {
		Author author = super.getAuthor();
		this.throwExceptionIfHasNotEnoughChocolates(author, request.getChocolates());
		
		// donation
		
		this.payChocolates(author, request.getChocolates(), ItemPrice.CHOCOLATE_DONATION.itemId);

		return ApiResponse.make(OdResponseType.OK);
	}

	private ResponseEntity<ApiResponse<DiaryGroupResponse>> invite(InviteTicketRequest request, int productPrice, String itemId, int maxAuthorCount) throws OdException {
		Author author = super.getAuthor();
		DateTimeZone dateTimeZone = super.getDateTimeZone(request.getTimeZoneId());
		this.throwExceptionIfHasNotEnoughChocolates(author, productPrice);
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

		this.payChocolates(author, productPrice, itemId);

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
	
	private void payChocolates(Author author, int numberOfChocolateRequired, String details) throws OdException {
		this.throwExceptionIfHasNotEnoughChocolates(author, numberOfChocolateRequired);
		int remainingChocolates = author.getChocolates() - numberOfChocolateRequired;
		author.setChocolates(remainingChocolates);
		authorRepository.save(author);
		authorRepository.insertChocolateHistory(author.getAuthorId(), -numberOfChocolateRequired, details);
	}

	private void throwExceptionIfHasNotEnoughChocolates(Author author, int numberOfChocolateRequired) throws OdException {
		if (author.getChocolates() < numberOfChocolateRequired) {
			OdLogger.info("not enough chocolates. author's={}, required={}", author.getChocolates(), numberOfChocolateRequired);
			throw new OdException(OdResponseType.PAYMENT_REQUIRED);
		}
	}
}
