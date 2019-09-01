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
import org.jerrioh.common.robot.RobotOJ.RobotResponse;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.api.weather.WeatherClient;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.BuyPostItRequest;
import org.jerrioh.diary.controller.author.payload.ChangeDescriptionResponse;
import org.jerrioh.diary.controller.author.payload.ChangeNicknameResponse;
import org.jerrioh.diary.controller.author.payload.ChocolateDonationRequest;
import org.jerrioh.diary.controller.author.payload.ChocolateDonationResponse;
import org.jerrioh.diary.controller.author.payload.CreateWiseSayingRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupCreateRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupResponse;
import org.jerrioh.diary.controller.author.payload.DiaryGroupSupportRequest;
import org.jerrioh.diary.controller.author.payload.DiaryGroupSupportRequest.SupportType;
import org.jerrioh.diary.controller.author.payload.GetWeatherRequest;
import org.jerrioh.diary.controller.author.payload.GetWeatherResponse;
import org.jerrioh.diary.controller.author.payload.GetWiseSayingResponse;
import org.jerrioh.diary.controller.author.payload.PurchaseMusicResponse;
import org.jerrioh.diary.controller.author.payload.PurchaseThemeResponse;
import org.jerrioh.diary.controller.author.payload.StoreStatusResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.jerrioh.diary.domain.DiaryGroup;
import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.AuthorStatus;
import org.jerrioh.diary.domain.Post;
import org.jerrioh.diary.domain.Post.PostStatus;
import org.jerrioh.diary.domain.WiseSaying;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	private final Random random = new Random();
	
	// configuration values
	private static final int DESCRIPTION_AND_NICKNAME_CHANGE_HOURS = 0;
	private static final int WISE_SAYING_HOURS = 3;
	private static final int DIARY_GROUP_INITITAL_MAX_AUTHOR_COUNT = 3;
	private static final int DIARY_GROUP_INITITAL_DURATION_DAYS = 3;
	private static final int DIARY_GROUP_LIMIT_MAX_AUTHOR_COUNT = 7;
	private static final int DIARY_GROUP_LIMIT_DURATION_DAYS = 7;
	
	private Map<String, Long> authorTimestamps = new ConcurrentHashMap<>();
	
	static enum Item {
		WISE_SAYING					("ITEM_WISE_SAYING", 1),
		CREATE_WISE_SAYING			("ITEM_CREATE_WISE_SAYING", 15),
		WEATHER						("ITEM_WEATHER", 0),
		POST_IT						("ITEM_POST_IT", 0),
		CHANGE_DESCRIPTION			("ITEM_CHANGE_DESCRIPTION", 3),
		CHANGE_NICKNAME				("ITEM_CHANGE_NICKNAME", 2),
		PURCHASE_THEME				("ITEM_PURCHASE_THEME", 10),
		PURCHASE_MUSIC				("ITEM_PURCHASE_MUSIC", 10),
		DIARY_GROUP_INVITATION		("ITEM_DIARY_GROUP_INVITATION", 4),
		DIARY_GROUP_SUPPORT			("ITEM_DIARY_GROUP_SUPPORT", 3),
		CHOCOLATE_DONATION			("ITEM_CHOCOLATE_DONATION", 0),
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
		
		Integer getable = authorRepository.wiseSayingGetable(author.getAuthorId(), WISE_SAYING_HOURS);
		if (getable != null && getable == 0) {
			priceMap.put(Item.WISE_SAYING.itemId, -1);
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
		} else {
			AuthorAnalyzed authorAnalyzed = authorAnalyzedRepository.findByAuthorId(author.getAuthorId());
			if (authorAnalyzed == null) {
				priceMap.put(Item.DIARY_GROUP_INVITATION.itemId, -1);	
			}
		}
		
		StoreStatusResponse response = new StoreStatusResponse();
		response.setChocolates(author.getChocolates());
		response.setPriceMap(priceMap);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/wise-saying")
	public ResponseEntity<ApiResponse<GetWiseSayingResponse>> getWiseSaying(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.WISE_SAYING, timestamp);

		WiseSaying wiseSaying = null;
		if (random.nextInt(5) == 0) { // author created : 0.1
			wiseSaying = wiseSayingRepository.findOneByLanguage(language, true);
			if (wiseSaying == null) {
				OdLogger.info("author created saying not found");
			}
		}
		if (wiseSaying == null) {
			String findLanguage = "kor".equals(language) ? "kor" : "eng";
			wiseSaying = wiseSayingRepository.findOneByLanguage(findLanguage, false);
		}
		
		this.purchaseComplete(Item.WISE_SAYING, String.valueOf(wiseSaying.getSayingId()));
		
		GetWiseSayingResponse response = new GetWiseSayingResponse();
		response.setSource(wiseSaying.getNickname());
		response.setWiseSaying(wiseSaying.getSaying());
		
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/create-wise-saying")
	public ResponseEntity<ApiResponse<Object>> createWiseSaying(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language,
			@RequestBody @Valid CreateWiseSayingRequest request) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.CREATE_WISE_SAYING, timestamp);

		Author author = this.getAuthor();
		
		WiseSaying wiseSaying = new WiseSaying();
		wiseSaying.setLanguage(language);
		wiseSaying.setNickname(author.getNickname());
		wiseSaying.setSaying(request.getSaying());
		wiseSaying.setAuthorCreated(true);
		
		this.purchaseComplete(Item.CREATE_WISE_SAYING, null);
		
		wiseSayingRepository.save(wiseSaying);
		
		return ApiResponse.make(OdResponseType.OK);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/weather")
	public ResponseEntity<ApiResponse<GetWeatherResponse>> getWeather(@RequestBody @Valid GetWeatherRequest request,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.WEATHER, timestamp);

		String cityName = request.getCity();
		String countryCode = request.getCountry();

		String query = String.join(",", cityName, countryCode);
		ResponseEntity<org.jerrioh.diary.api.weather.payload.GetWeatherResponse> feignResponse = weatherClient.weather(query, weatherApiKey);
		String weather = feignResponse.getBody().getWeather().get(0).getDescription();
		String description = messageSource.getMessage("weather.description", language, weather);
		
		this.purchaseComplete(Item.WEATHER, description);
		
		GetWeatherResponse response = new GetWeatherResponse();
		response.setDescription(description);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/buy-post-it")
	public ResponseEntity<ApiResponse<Object>> butPostIt(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestBody @Valid BuyPostItRequest request) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.POST_IT, request.getPrice());
		
		Author author = this.getAuthor();
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
		post.setContent("");
		post.setLanguage(null);
		post.setCountry(null);
		post.setTimeZoneId(null);
		post.setWrittenTime(null);
		postRepository.save(post);

		this.purchaseComplete(Item.POST_IT, request.getPrice(), null);
			
		return ApiResponse.make(OdResponseType.OK);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-description")
	public ResponseEntity<ApiResponse<ChangeDescriptionResponse>> changeDescription(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.CHANGE_DESCRIPTION, timestamp);
		
		Author author = this.getAuthor();
		if (authorRepository.descriptionChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			OdLogger.info("Can not change description yet.");
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}

		RobotResponse robotResponse = robotOJ.talkToRobot(language, author.getAuthorId());
		
		String message = robotResponse.getMessage();
		String description = robotResponse.getDescription();
		
		author.setDescription(description);

		authorRepository.save(author);
		authorRepository.insertDescriptionHistory(author.getAuthorId());
		
		this.purchaseComplete(Item.CHANGE_DESCRIPTION, description);
		
		ChangeDescriptionResponse response = new ChangeDescriptionResponse();
		response.setAboutYou(message);
		response.setDescription(description);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/change-nickname")
	public ResponseEntity<ApiResponse<ChangeNicknameResponse>> changeNickname(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.CHANGE_NICKNAME, timestamp);
		
		Author author = super.getAuthor();
		if (authorRepository.nickNameChangable(author.getAuthorId(), DESCRIPTION_AND_NICKNAME_CHANGE_HOURS) == 0) {
			OdLogger.info("Can not change nickname yet.");
			throw new OdException(OdResponseType.PRECONDITION_FAILED);
		}

		String nickname = generateNickname(language);
		author.setNickname(nickname);

		authorRepository.save(author);
		authorRepository.insertNickNameHistory(author.getAuthorId());
		
		this.purchaseComplete(Item.CHANGE_NICKNAME, nickname);

		ChangeNicknameResponse response = new ChangeNicknameResponse();
		response.setNickname(nickname);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/purchase-theme")
	public ResponseEntity<ApiResponse<PurchaseThemeResponse>> purchaseTheme(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.PURCHASE_THEME, timestamp);
		
		String themeName;
		try {
			ClassPathResource classPathResource = new ClassPathResource("diary-theme");
			File[] themeDirectories = classPathResource.getFile().listFiles();
			int randomIndex = random.nextInt(themeDirectories.length);
			File randomThemeDirectory = themeDirectories[randomIndex];
			
			// select random theme
			themeName = randomThemeDirectory.getName();
			OdLogger.info("selected theme={}", themeName);
			
		} catch (IOException e) {
			System.out.println(e.toString());
			throw new OdException(OdResponseType.FILE_READ_ERROR);
		}
		
		this.purchaseComplete(Item.PURCHASE_THEME, themeName);

		PurchaseThemeResponse response = new PurchaseThemeResponse();
		response.setThemeName(themeName);
		response.setPattern0(null);
		response.setPattern1(null);
		response.setPattern2(null);
		response.setPattern3(null);
		response.setBannerColor(null);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@PostMapping(value = "/download-theme/{themeName}")
	public ResponseEntity<ApiResponse<PurchaseThemeResponse>> downloadTheme(
			@PathVariable(name = "themeName") String themeName,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);

		String[] patterns = new String[4];
		String bannerColor;
		try {
			ClassPathResource classPathResource = new ClassPathResource("diary-theme\\" + themeName);
			File themeDirectory = classPathResource.getFile();
			
			for (int index = 0; index < 4; index++) {
				File imageFile = new File(themeDirectory.getPath() + String.format("\\pattern%d.png", index));
				byte[] bytes = Files.readAllBytes(imageFile.toPath());
				patterns[index] = new String(Base64.getEncoder().encode(bytes));
			}

			File textFile = new File(themeDirectory.getPath() + "\\banner");
			byte[] textBytes = Files.readAllBytes(textFile.toPath());
			bannerColor = new String(textBytes);
			
		} catch (IOException e) {
			throw new OdException(OdResponseType.FILE_READ_ERROR);
		}
		
		PurchaseThemeResponse response = new PurchaseThemeResponse();
		response.setThemeName(themeName);
		response.setPattern0(patterns[0]);
		response.setPattern1(patterns[1]);
		response.setPattern2(patterns[2]);
		response.setPattern3(patterns[3]);
		response.setBannerColor(bannerColor);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/purchase-music")
	public ResponseEntity<ApiResponse<PurchaseMusicResponse>> purchaseMusic(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		this.purchasePreCheck(Item.PURCHASE_MUSIC, timestamp);
		
		String musicName;
		try {
			ClassPathResource classPathResource = new ClassPathResource("diary-music");
			File[] mp3Files = classPathResource.getFile().listFiles();
			int randomIndex = random.nextInt(mp3Files.length);
			File randomMp3File = mp3Files[randomIndex];
			
			musicName = randomMp3File.getName();
			OdLogger.info("selected music={}", musicName);
			
		} catch (IOException e) {
			throw new OdException(OdResponseType.FILE_READ_ERROR);
		}
		
		this.purchaseComplete(Item.PURCHASE_MUSIC, null);
		
		PurchaseMusicResponse response = new PurchaseMusicResponse();
		response.setMusicName(musicName);
		response.setMusicData(null);
		return ApiResponse.make(OdResponseType.OK, response);
	}
	
	@PostMapping(value = "/download-music/{musicName}")
	public ResponseEntity<ApiResponse<PurchaseMusicResponse>> downloadMusic(
			@PathVariable(name = "musicName") String musicName,
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp) throws OdException {
		this.ignoreDuplicatedRequest(timestamp);
		
		String musicData;
		try {
			ClassPathResource classPathResource = new ClassPathResource("diary-music\\" + musicName);
			File mp3File = classPathResource.getFile();

			byte[] bytes = Files.readAllBytes(mp3File.toPath());
			musicData = new String(Base64.getEncoder().encode(bytes));
			
		} catch (IOException e) {
			throw new OdException(OdResponseType.FILE_READ_ERROR);
		}
		
		PurchaseMusicResponse response = new PurchaseMusicResponse();
		response.setMusicName(musicName);
		response.setMusicData(musicData);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/diary-group-invitation")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> createDiaryGroup(
			@RequestHeader(value = OdHeaders.LANGUAGE, required = true) String language,
			@RequestHeader(value = OdHeaders.COUNTRY, required = true) String country,
			@RequestHeader(value = OdHeaders.TIME_ZONE_ID, required = true) String timeZoneId,
			@RequestHeader(value = OdHeaders.TIMESTAMP, required = true) Long timestamp,
			@RequestBody @Valid DiaryGroupCreateRequest request) throws OdException {

		this.purchasePreCheck(Item.DIARY_GROUP_INVITATION, timestamp);
		
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
		
		this.purchaseComplete(Item.DIARY_GROUP_INVITATION, null);
		
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
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/diary-group-support")
	public ResponseEntity<ApiResponse<DiaryGroupResponse>> supportDiaryGroup(
			@RequestHeader(value = OdHeaders.TIMESTAMP, required = true) Long timestamp,
			@RequestBody @Valid DiaryGroupSupportRequest request) throws OdException {

		this.purchasePreCheck(Item.DIARY_GROUP_SUPPORT, timestamp);
		
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

		this.purchaseComplete(Item.DIARY_GROUP_SUPPORT, null);

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

	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/chocolate-donation")
	public ResponseEntity<ApiResponse<ChocolateDonationResponse>> chocolateDonation(
			@RequestHeader(value = OdHeaders.TIMESTAMP) Long timestamp,
			@RequestBody @Valid ChocolateDonationRequest request) throws OdException {

		this.purchasePreCheck(Item.CHOCOLATE_DONATION, request.getChocolates());
		
		Author author = super.getAuthor();
		int totalDonations = authorRepository.sumOfChocolatesUsed(author.getAuthorId(), Item.CHOCOLATE_DONATION.itemId);
		
		this.purchaseComplete(Item.CHOCOLATE_DONATION, request.getChocolates(), null);
		
		ChocolateDonationResponse response = new ChocolateDonationResponse();
		response.setTotalDonations(totalDonations * -1);
		return ApiResponse.make(OdResponseType.OK, response);
	}

	@FunctionalInterface
	private interface Purchaser<R> {
		R purchase() throws OdException;
	}
	
	private void ignoreDuplicatedRequest(Long timestamp) throws OdException {
		Author author = super.getAuthor();
		Long previousTimestamp = authorTimestamps.get(author.getAuthorId());
		if (previousTimestamp != null && previousTimestamp.compareTo(timestamp) == 0) {
			OdLogger.info("Repetitive purchase request. author={}, timestamp={}", author.getAuthorId(), timestamp);
			throw new OdException(OdResponseType.TOO_MANY_REQUESTS);
		}
		authorTimestamps.put(author.getAuthorId(), timestamp);
	}

	private void purchasePreCheck(Item item, Long timestamp) throws OdException {
		this.purchasePreCheck(item, item.price);
	}
	
	private void purchasePreCheck(Item item, int price) throws OdException {
		Author author = super.getAuthor();
		if (price < 0) {
			throw new OdException(OdResponseType.BAD_REQUEST);
		}
		this.throwExceptionIfHasNotEnoughChocolates(author, price);
	}
	
	private void purchaseComplete(Item item, String result) throws OdException {
		this.purchaseComplete(item, item.price, result);
	}
	
	private void purchaseComplete(Item item, int price, String result) throws OdException {
		Author author = super.getAuthor();
		if (price > 0) {
			String details = item.itemId;
			if (result != null) {
				details += "/" + result;
			}
			this.payChocolates(author, price, details);	
		}
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
