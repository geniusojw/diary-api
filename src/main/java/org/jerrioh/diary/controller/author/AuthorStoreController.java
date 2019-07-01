package org.jerrioh.diary.controller.author;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.api.weather.WeatherClient;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.BuyPostItRequest;
import org.jerrioh.diary.controller.author.payload.ChangeDescriptionResponse;
import org.jerrioh.diary.controller.author.payload.ChangeNicknameResponse;
import org.jerrioh.diary.controller.author.payload.ChocolateDonationRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupCreateRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.DiaryGroupSupportRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupSupportRequest.SupportType;
import org.jerrioh.diary.controller.author.payload.GetWeatherRequest;
import org.jerrioh.diary.controller.author.payload.GetWeatherResponse;
import org.jerrioh.diary.controller.author.payload.PurchaseMusicResponse;
import org.jerrioh.diary.controller.author.payload.PurchaseThemeResponse;
import org.jerrioh.diary.controller.author.payload.StoreStatusResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.jerrioh.diary.domain.Post;
import org.jerrioh.diary.domain.Post.PostStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/store")
public class AuthorStoreController extends AbstractAuthorController {
	
	@Value(value = "${apis.openweathermap.auth}")
	private String weatherApiKey;

	@Autowired
	private WeatherClient weatherClient;
	
	private static final Random RANDOM = new Random();
	
	// configuration values
	private static final int DESCRIPTION_AND_NICKNAME_CHANGE_HOURS = 1;
	private static final int DIARY_GROUP_INITITAL_MAX_AUTHOR_COUNT = 2;
	private static final int DIARY_GROUP_INITITAL_DURATION_DAYS = 3;
	private static final int DIARY_GROUP_LIMIT_MAX_AUTHOR_COUNT = 7;
	private static final int DIARY_GROUP_LIMIT_DURATION_DAYS = 7;
	
	private Map<String, Long> authorTimestamps = new ConcurrentHashMap<>();
	
	static enum Item {
		WEATHER						("ITEM_WEATHER", 0),
		POST_IT						("ITEM_POST_IT", 0),
		CHANGE_DESCRIPTION			("ITEM_CHANGE_DESCRIPTION", 3),
		CHANGE_NICKNAME				("ITEM_CHANGE_NICKNAME", 3),
		PURCHASE_THEME				("ITEM_PURCHASE_THEME", 10),
		PURCHASE_MUSIC				("ITEM_PURCHASE_MUSIC", 10),
		DIARY_GROUP_INVITATION		("ITEM_DIARY_GROUP_INVITATION", 4),
		DIARY_GROUP_SUPPORT			("ITEM_DIARY_GROUP_SUPPORT", 2),
		CHOCOLATE_DONATION			("ITEM_CHOCOLATE_DONATION", 2),
		;

		private Item(String itemId, int price) {
			this.itemId = itemId;
			this.price = price;
		}
		private String itemId;
		private int price;
	}

	@GetMapping(value = "/status")
	public ResponseEntity<ApiResponse<StoreStatusResponse>> status() {
		Author author = super.getAuthor();

		Map<String, Integer> priceMap = new HashMap<>();
		
		for (Item item : Item.values()) {
			priceMap.put(item.itemId, item.price);
		}
		Post post = postRepository.findMyNotPosted(author.getAuthorId());
		if (post != null) {
			priceMap.put(Item.POST_IT.itemId, -1);
		}
		if (authorRepository.descriptionChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			priceMap.put(Item.CHANGE_DESCRIPTION.itemId, -1);
		}
		if (authorRepository.nickNameChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			priceMap.put(Item.CHANGE_NICKNAME.itemId, -1);
		}
		DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedOrAcceptableByAuthorId(author.getAuthorId());
		if (diaryGroup != null) {
			priceMap.put(Item.DIARY_GROUP_INVITATION.itemId, -1);
		}
		
		StoreStatusResponse response = new StoreStatusResponse();
		response.setChocolates(author.getChocolates());
		response.setPriceMap(priceMap);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/weather")
	public ResponseEntity<ApiResponse<GetWeatherResponse>> getWeather(@Valid GetWeatherRequest request,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		return purchase(Item.WEATHER, timestamp, () -> {
			String cityName = request.getCity();
			String countryCode = request.getCountry();
			
			String query = String.join(",", cityName, countryCode);
			ResponseEntity<org.jerrioh.diary.api.weather.payload.GetWeatherResponse> feignResponse = weatherClient.weather(query, weatherApiKey);
			String description = feignResponse.getBody().getWeather().get(0).getDescription();
			
			String message = messageSource.getMessage("weather.description", language, description);
			
			GetWeatherResponse response = new GetWeatherResponse();
			response.setDescription(message);
			return ApiResponse.make(OdResponseType.OK, response);
		});
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/buy-post-it")
	public ResponseEntity<ApiResponse<Object>> butPostIt(@Valid BuyPostItRequest request,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchaseNoPrice(Item.POST_IT, request.getPrice(), timestamp, () -> {
			Author author = AuthorStoreController.this.getAuthor();
			Post post = postRepository.findMyNotPosted(author.getAuthorId());
			if (post != null) {
				throw new OdException(OdResponseType.PRECONDITION_FAILED);
			}
			
			post = new Post();
			post.setPostId(generatePostId());
			post.setPostStatus(PostStatus.NOT_POSTED);
			post.setAuthorId(author.getAuthorId());
			post.setAuthorNickname(author.getNickname());
			post.setChocolates(request.getPrice());
			post.setContent(null);
			post.setLanguage(null);
			post.setCountry(null);
			post.setTimeZoneId(null);
			post.setWrittenTime(null);
			
			postRepository.save(post);
			
			return ApiResponse.make(OdResponseType.OK);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-description")
	public ResponseEntity<ApiResponse<ChangeDescriptionResponse>> changeDescription(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchase(Item.CHANGE_DESCRIPTION, timestamp, () -> {
			Author author = AuthorStoreController.this.getAuthor();
			if (authorRepository.descriptionChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
				OdLogger.info("Can not change description yet.");
				throw new OdException(OdResponseType.PRECONDITION_FAILED);
			}

			String aboutYou = talkAboutYou();
			String description = generateDescription();
			author.setDescription(description);

			authorRepository.save(author);
			authorRepository.insertDescriptionHistory(author.getAuthorId());

			ChangeDescriptionResponse response = new ChangeDescriptionResponse();
			response.setAboutYou(aboutYou);
			response.setDescription(description);
			return ApiResponse.make(OdResponseType.OK, response);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-nickname")
	public ResponseEntity<ApiResponse<ChangeNicknameResponse>> changeNickname(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchase(Item.CHANGE_NICKNAME, timestamp, () -> {
			Author author = super.getAuthor();
			if (authorRepository.nickNameChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
				OdLogger.info("Can not change nickname yet.");
				throw new OdException(OdResponseType.PRECONDITION_FAILED);
			}

			String nickname = generateNickName();
			author.setNickname(nickname);
			
			authorRepository.save(author);
			authorRepository.insertNickNameHistory(author.getAuthorId());

			ChangeNicknameResponse response = new ChangeNicknameResponse();
			response.setNickname(nickname);
			return ApiResponse.make(OdResponseType.OK, response);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/purchase-theme")
	public ResponseEntity<ApiResponse<PurchaseThemeResponse>> purchaseTheme(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchase(Item.PURCHASE_THEME, timestamp, () -> {
			// select random theme
			String themeName;
			String[] patterns = new String[4];
			try {
				ClassPathResource classPathResource = new ClassPathResource("diary-theme");
				File[] themeDirectories = classPathResource.getFile().listFiles();
				int randomIndex = RANDOM.nextInt(themeDirectories.length);
				File randomThemeDirectory = themeDirectories[randomIndex];
				
				themeName = randomThemeDirectory.getName();
				OdLogger.info("selected theme={}", themeName);
				for (int index = 0; index < 4; index++) {
					File imageFile = new File(randomThemeDirectory.getPath() + String.format("\\pattern%d.png", index));
					byte[] bytes = Files.readAllBytes(imageFile.toPath());
					patterns[index] = new String(Base64.getEncoder().encode(bytes));
				}
				
			} catch (IOException e) {
				System.out.println(e.toString());
				throw new OdException(OdResponseType.FILE_READ_ERROR);
			}

			PurchaseThemeResponse response = new PurchaseThemeResponse();
			response.setThemeName(themeName);
			response.setPattern0(patterns[0]);
			response.setPattern1(patterns[1]);
			response.setPattern2(patterns[2]);
			response.setPattern3(patterns[3]);
			return ApiResponse.make(OdResponseType.OK, response);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/purchase-music")
	public ResponseEntity<ApiResponse<PurchaseMusicResponse>> purchaseMusic(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchase(Item.PURCHASE_MUSIC, timestamp, () -> {
			String musicName;
			String musicData;
			try {
				ClassPathResource classPathResource = new ClassPathResource("diary-music");
				File[] mp3Files = classPathResource.getFile().listFiles();
				int randomIndex = RANDOM.nextInt(mp3Files.length);
				File randomMp3File = mp3Files[randomIndex];
				
				musicName = randomMp3File.getName();
				OdLogger.info("selected music={}", musicName);
				byte[] bytes = Files.readAllBytes(randomMp3File.toPath());
				musicData = new String(Base64.getEncoder().encode(bytes));
				
			} catch (IOException e) {
				throw new OdException(OdResponseType.FILE_READ_ERROR);
			}
			

			PurchaseMusicResponse response = new PurchaseMusicResponse();
			response.setMusicName(musicName);
			response.setMusicData(musicData);
			return ApiResponse.make(OdResponseType.OK, response);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/diary-group-invitation")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> createDiaryGroup(@RequestBody @Valid DiaryGroupCreateRequest request,
			@RequestHeader(value = OdHeaders.LANGUAGE, required = true) String language,
			@RequestHeader(value = OdHeaders.COUNTRY, required = true) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID, required = true) String timeZoneId,
			@RequestHeader(value = OdHeaders.TIMESTAMP, required = true) Long timestamp) throws OdException {
		return purchase(Item.DIARY_GROUP_INVITATION, timestamp, () -> {
			Author author = super.getAuthor();
			DateTimeZone dateTimeZone = super.getDateTimeZone(timeZoneId);
			
			DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedOrAcceptableByAuthorId(author.getAuthorId());
			if (diaryGroup != null) {
				throw new OdException(OdResponseType.DIARY_GROUP_CONFLICT);
			}

			DateTime dateTime = new DateTime().withZone(dateTimeZone);
			
			int plusStartDays = 1;
			if (TimeUnit.HOURS.convert(dateTime.getMillisOfDay(), TimeUnit.MILLISECONDS) > 12) {
				plusStartDays++;
			}
			DateTime startDateTime = dateTime.plusDays(plusStartDays).withMillisOfDay(0); // group diary GMT기준으로 다음날 0시, 초대시작시간이 12시 이후라면 다다음날 0시 시작
			DateTime endDateTime = startDateTime.plusDays(DIARY_GROUP_INITITAL_DURATION_DAYS);

			diaryGroup = new DiaryGroup();
			diaryGroup.setHostAuthorId(author.getAuthorId());
			diaryGroup.setDiaryGroupName(null);
			diaryGroup.setKeyword(request.getKeyword());
			diaryGroup.setMaxAuthorCount(DIARY_GROUP_INITITAL_MAX_AUTHOR_COUNT);
			diaryGroup.setLanguage(language);
			diaryGroup.setCountry(country);
			diaryGroup.setTimeZoneId(timeZoneId);
			diaryGroup.setStartTime(new Timestamp(startDateTime.getMillis()));
			diaryGroup.setEndTime(new Timestamp(endDateTime.getMillis()));

			diaryGroupRepository.save(diaryGroup);

			DiaryGroupAuthor diaryGroupAuthor = new DiaryGroupAuthor();
			diaryGroupAuthor.setDiaryGroupId(diaryGroup.getDiaryGroupId());
			diaryGroupAuthor.setAuthorId(diaryGroup.getHostAuthorId());
			diaryGroupAuthor.setAuthorStatus(AuthorStatus.ACCEPT);
			diaryGroupAuthorRepository.save(diaryGroupAuthor);

			DiaryGroupResponse response = new DiaryGroupResponse();
			response.setDiaryGroupId(diaryGroup.getDiaryGroupId());
			response.setDiaryGroupName(diaryGroup.getDiaryGroupName());
			response.setHostAuthorId(diaryGroup.getHostAuthorId());
			response.setKeyword(diaryGroup.getKeyword());
			response.setCurrentAuthorCount(1);
			response.setMaxAuthorCount(diaryGroup.getMaxAuthorCount());
			response.setLanguage(diaryGroup.getLanguage());
			response.setCountry(diaryGroup.getCountry());
			response.setTimeZoneId(diaryGroup.getTimeZoneId());
			response.setStartTime(diaryGroup.getStartTime().getTime());
			response.setEndTime(diaryGroup.getEndTime().getTime());

			return ApiResponse.make(OdResponseType.OK, response);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/diary-group-support")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> supportDiaryGroup(@RequestBody @Valid DiaryGroupSupportRequest request,
			@RequestHeader(value = OdHeaders.TIMESTAMP, required = true) Long timestamp) throws OdException {
		return purchase(Item.DIARY_GROUP_SUPPORT, timestamp, () -> {
			Author author = super.getAuthor();
			
			DiaryGroup diaryGroup = diaryGroupRepository.findAcceptedByAuthorId(author.getAuthorId());
			if (diaryGroup == null) {
				throw new OdException(OdResponseType.DIARY_GROUP_NOT_FOUND);
			}
			
			if (SupportType.SCALE.equals(request.getSupportType())) {
				int maxAuthorCount = diaryGroup.getMaxAuthorCount() + 1;
				if (maxAuthorCount > DIARY_GROUP_LIMIT_MAX_AUTHOR_COUNT) {
					OdLogger.info("Exceeded the maximum. maxAuthorCount={}", maxAuthorCount);
					throw new OdException(OdResponseType.PRECONDITION_FAILED);	
				}
				diaryGroup.setMaxAuthorCount(maxAuthorCount);	
				
			} else if (SupportType.PERIOD.equals(request.getSupportType())) {
				DateTime startDateTime = new DateTime(diaryGroup.getStartTime());
				DateTime extendEndDateTime = new DateTime(diaryGroup.getEndTime()).plusDays(1);
				long durationMillis = extendEndDateTime.getMillis() - startDateTime.getMillis();
				long durationDays = TimeUnit.DAYS.convert(durationMillis, TimeUnit.MILLISECONDS);
				if (durationDays > DIARY_GROUP_LIMIT_DURATION_DAYS) {
					OdLogger.info("Exceeded the maximum. durationMillis={}, durationDays={}", durationMillis, durationDays);
					throw new OdException(OdResponseType.PRECONDITION_FAILED);	
				}
				diaryGroup.setEndTime(new Timestamp(extendEndDateTime.getMillis()));
				
			} else {
				OdLogger.info("Unexpected support type. request.getSupportType()={}", request.getSupportType());
				throw new OdException(OdResponseType.INTERNAL_SERVER_ERROR);
			}

			diaryGroupRepository.save(diaryGroup);
			
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
		});
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/chocolate-donation")
	public ResponseEntity<ApiResponse<Object>> chocolateDonation(@RequestBody @Valid ChocolateDonationRequest request,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		return purchase(Item.CHOCOLATE_DONATION, timestamp, () -> {
			// chocolate donation
			
			return ApiResponse.make(OdResponseType.OK);
		});
	}

	@FunctionalInterface
	private interface Purchaser<R> {
		ResponseEntity<ApiResponse<R>> purchase() throws OdException;
	}
	
	private <R> ResponseEntity<ApiResponse<R>> purchaseNoPrice(Item item, int price, Long timestamp, Purchaser<R> purchaser) throws OdException {
		Author author = super.getAuthor();
		
		Long previousTimestamp = authorTimestamps.get(author.getAuthorId());
		if (previousTimestamp != null && previousTimestamp.compareTo(timestamp) == 0) {
			OdLogger.info("Repetitive purchase request. author={}, price={}, item={}, timestamp={}", author.getAuthorId(), price, item, timestamp);
			throw new OdException(OdResponseType.TOO_MANY_REQUESTS);
		}
		authorTimestamps.put(author.getAuthorId(), timestamp);
		
		this.throwExceptionIfHasNotEnoughChocolates(author, price);
		ResponseEntity<ApiResponse<R>> purchase = purchaser.purchase();
		this.payChocolates(author, price, item.itemId);
		
		return purchase;
	}
	
	private <R> ResponseEntity<ApiResponse<R>> purchase(Item item, Long timestamp, Purchaser<R> purchaser) throws OdException {
		Author author = super.getAuthor();
		
		Long previousTimestamp = authorTimestamps.get(author.getAuthorId());
		if (previousTimestamp != null && previousTimestamp.compareTo(timestamp) == 0) {
			OdLogger.info("Repetitive purchase request. author={}, item={}, timestamp={}", author.getAuthorId(), item, timestamp);
			throw new OdException(OdResponseType.TOO_MANY_REQUESTS);
		}
		authorTimestamps.put(author.getAuthorId(), timestamp);
		
		this.throwExceptionIfHasNotEnoughChocolates(author, item.price);
		ResponseEntity<ApiResponse<R>> purchase = purchaser.purchase();
		this.payChocolates(author, item.price, item.itemId);
		
		return purchase;
	}

	private void throwExceptionIfHasNotEnoughChocolates(Author author, int numberOfChocolateRequired) throws OdException {
		if (author.getChocolates() < numberOfChocolateRequired) {
			OdLogger.info("not enough chocolates. author's={}, required={}", author.getChocolates(), numberOfChocolateRequired);
			throw new OdException(OdResponseType.PAYMENT_REQUIRED);
		}
	}
	
	private void payChocolates(Author author, int numberOfChocolateRequired, String details) throws OdException {
		this.throwExceptionIfHasNotEnoughChocolates(author, numberOfChocolateRequired);
		int remainingChocolates = author.getChocolates() - numberOfChocolateRequired;
		author.setChocolates(remainingChocolates);
		authorRepository.save(author);
		authorRepository.insertChocolateHistory(author.getAuthorId(), -numberOfChocolateRequired, details);
	}
}
