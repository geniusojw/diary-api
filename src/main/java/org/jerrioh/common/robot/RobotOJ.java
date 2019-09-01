package org.jerrioh.common.robot;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jerrioh.common.util.OdLogger;
import org.jerrioh.common.util.StringUtil;
import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.jerrioh.diary.domain.FeedbackMessage;
import org.jerrioh.diary.domain.FeedbackResult;
import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.FeedbackMessageRepository;
import org.jerrioh.diary.domain.repo.FeedbackResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotOJ {
	
	private static final int INDEX_NEUROTICISM = 0;
	private static final int INDEX_EXTRAVERSION = 1;
	private static final int INDEX_OPENNESS = 2;
	private static final int INDEX_AGREEABLENESS = 3;
	private static final int INDEX_CONSCIENTIOUSNESS = 4;
	
	@Autowired
	private AuthorAnalyzedRepository authorAnalyzedRepository;
	
	@Autowired
	private FeedbackResultRepository feedbackResultRepository;

	@Autowired
	private FeedbackMessageRepository feedbackMessageRepository;
	
	private final Random random = new Random();
	
	private List<FeedbackResult> feedbackResults;
	private Map<String, List<FeedbackMessage>> messageMap;
	
	@PostConstruct
	public void postConstruct() {
		feedbackResults = feedbackResultRepository.findAll();
		messageMap = feedbackMessageRepository.findAll().stream().collect(Collectors.groupingBy(m -> keyForMessage(m.getFactorType(), m.isNegative())));
	}
	
	public static class RobotResponse {
		private String description;
		private String message;
		public String getDescription() {
			return description;
		}
		public String getMessage() {
			return message;
		}
	}
	
	public String startDescription(String language) {
		if ("kor".equals(language)) {
			return "일기초보";
		} else {
			return "beginner";
		}
	}
	
	public RobotResponse talkToRobot(String language, String authorId) {
		AuthorAnalyzed authorAnalyzed = authorAnalyzedRepository.findByAuthorId(authorId);
		
		int n = authorAnalyzed.getFactorNeuroticism();
		int e = authorAnalyzed.getFactorExtraversion();
		int o = authorAnalyzed.getFactorOpenness();
		int a = authorAnalyzed.getFactorAgreeableness();
		int c = authorAnalyzed.getFactorConscientiousness();
		
		int total = Math.abs(n) + Math.abs(e) + Math.abs(o) + Math.abs(a) + Math.abs(c);
		
		RobotResponse robotResponse = new RobotResponse();
		if (total == 0) {
			if ("kor".equals(language)) {
				robotResponse.description = "분석이 실패한 사람";
				robotResponse.message = "분석할 수가 없다.";
				
			} else {
				robotResponse.description = "error person";
				robotResponse.message = "cannot...";
			}
			return robotResponse;
		}
		
		int nRatio = n * 10000 / total;
		int eRatio = e * 10000 / total;
		int oRatio = o * 10000 / total;
		int aRatio = a * 10000 / total;
		int cRatio = c * 10000 / total;

		// feedback result 값중 가장 비슷한 결과 찾기
		FeedbackResult minimumResult = null;
		int minimumDiff = Integer.MAX_VALUE;
		for (FeedbackResult feedbackResult : feedbackResults) {
			int diff = 0;
			diff += Math.abs(nRatio - feedbackResult.getRatioNeuroticism());
			diff += Math.abs(eRatio - feedbackResult.getRatioExtraversion());
			diff += Math.abs(oRatio - feedbackResult.getRatioOpenness());
			diff += Math.abs(aRatio - feedbackResult.getRatioAgreeableness());
			diff += Math.abs(cRatio - feedbackResult.getRatiorConscientiousness());
			
			if (diff < minimumDiff) {
				minimumResult = feedbackResult;
				minimumDiff = diff;
			}
		}

		// 자신의 요소 중 가장 높은 2개 요소의 메시지
		int[] ratios = { nRatio, eRatio, oRatio, aRatio, cRatio };

		int firstIndex;
		int secondIndex;
		if (Math.abs(ratios[0]) > Math.abs(ratios[1])) {
			firstIndex = 0; secondIndex = 1;
		} else {
			firstIndex = 1; secondIndex = 0;
		}
		
		for (int index = 2; index < 5; index++) {
			if (Math.abs(ratios[index]) > Math.abs(ratios[firstIndex])) {
				secondIndex = firstIndex;
				firstIndex = index;
			} else if (Math.abs(ratios[index]) > Math.abs(ratios[secondIndex])) {
				secondIndex = index;
			}
		}

		if (minimumResult != null) {
			OdLogger.info("###################################");
			OdLogger.info("description = {}, {}", minimumResult.getKoreanDescription(), minimumResult.getEnglishDescription());
			OdLogger.info("nRatio = {} % {}", nRatio, minimumResult.getRatioNeuroticism());
			OdLogger.info("eRatio = {} % {}", eRatio, minimumResult.getRatioExtraversion());
			OdLogger.info("oRatio = {} % {}", oRatio, minimumResult.getRatioOpenness());
			OdLogger.info("aRatio = {} % {}", aRatio, minimumResult.getRatioAgreeableness());
			OdLogger.info("cRatio = {} % {}", cRatio, minimumResult.getRatiorConscientiousness());
			OdLogger.info("###################################");
			

			List<FeedbackMessage> messages1 = messageMap.get(keyForMessage(firstIndex, ratios[firstIndex] < 0));
			List<FeedbackMessage> messages2 = messageMap.get(keyForMessage(secondIndex, ratios[secondIndex] < 0));
			
			if ("kor".equals(language)) {
				robotResponse.description = minimumResult.getKoreanDescription();
				robotResponse.message = new StringBuilder().append(" - ").append(messages1.get(random.nextInt(messages1.size())).getKoreanDescription()).append("\n")
													  .append(" - ").append(messages2.get(random.nextInt(messages2.size())).getKoreanDescription()).toString();
				
			} else {
				robotResponse.description = minimumResult.getEnglishDescription();
				robotResponse.message = new StringBuilder().append(" - ").append(messages1.get(random.nextInt(messages1.size())).getEnglishDescription()).append("\n")
													  .append(" - ").append(messages2.get(random.nextInt(messages2.size())).getEnglishDescription()).toString();
			}
		} else {
			if ("kor".equals(language)) {
				robotResponse.description = "분석이 실패한 사람";
				robotResponse.message = "분석할 수가 없다.";
				
			} else {
				robotResponse.description = "error person";
				robotResponse.message = "cannot...";
			}
		}
		
		return robotResponse;
	}

	protected String keyForMessage(int index, boolean isNegative) {
		return String.valueOf(index) + String.valueOf(isNegative);
	}
	
	
	private String talkAboutYou() {
		// (정보)
		// robot talk 날짜 기록
		// author 다이어리 기록, group diary 기록
		// store 구매 기록
		// analysis 기록
		
		// 1. diary + tutorial
		// 지금 바쁘니까 나중에 와라#_# / 로봇에게 거절당한 사람 / A person rejected by a robot
		// 2. behavior
		// 3. analysis, big5
		// 4. humor
		// 5. tips
		
		
		// (feedback 정보가 없는 경우)
		// 시작하는 사람, Starter
		String tutorialMessage1 = "지금은 바쁘니까 1시간 이후에 다시 오라#_#"; // 로봇에게 거절당한 사람, A person rejected by a robot
		String tutorialMessage2 = "안녕- 내 이름은 OJ- 로봇이다- 일기는 열심히- 그럼 돌아가라;"; // 일기 열심히 쓰기로 한 사람, A person who decided to write a diary diligently
		String tutorialMessage3 = "바쁘다#_# 돌아가라;"; // 로봇에서 시간을 낭비하는 사람, A person wastes time on robots
		String tutorialMessage4 = "너 분석한다- ...... 정보가 없다;"; // 알 수 없는 사람,
		String tutorialMessage5 = "너에 대해 정보가 없다- 정보가 없으면- 팬티엄3 슈퍼컴퓨터인- 나도 방법이 없다;"; // 정보가 없는 사람,
		String tutorialMessage6 = "너에 대한 분석- 하기 위해서는- 정보를 내놔라;"; // 분석하기 위한 정보가 없는 사람,
		
		// (feedback 정보가 없는 경우 1~4개)
		String tutorialMessage7 = "너 분석한다- ...... 정보가 있다#_# 눈꼽만큼 있다;"; // 정보가 눈꼽만큼 있는 사람,
		String tutorialMessage8 = "너 분석한다- ...... 정보를 발견했다- 정보가 아직 모자르다;"; // 일기모임에 참여만 해본 사람,
		String tutorialMessage9 = "너 분석한다- ...... 분석결과: 멍청하다- 이건 농담이다- 하하;"; // 멍청한 %nickname,
		String tutorialMessage10 = "너 분석한다- ...... 분석결과: 아주 멍청하다- 농담아니다- 하하하;"; // 아주 멍청한 %nickname,
		String tutorialMessage11 = "너에 대한 정보- 조금 있다- 그러나 이 정도로는- 분석 어렵다;"; // 앱 사용법을 터득해가고 있는 사람,
		
		// (feedback 정보가 없는 경우 5개이상)
		String tutorialMessage12 = "슈퍼컴퓨터 OJ- 분석결과-";
		String tutorialMessage13 = "너는 이런 사람이다-";
		String tutorialMessage14 = "오늘은 컨디션이 좋다- 내 분석 정확하다-";
		String tutorialMessage15 = "너에 대해 확실하게 알아버렸다-";

		// (특수)
		String tutorialMessage16 = "너- 일기 열심히 쓴다- 좋다#_# 그런데 일기모임은 참여를 안해서- 분석은 어렵다;"; // 앱 사용법을 터득해가고 있는 사람,
		String tutorialMessage17 = "너- 일기를 열심히 쓴다- 좋은 습관이다-"; // 앱 사용법을 터득해가고 있는 사람,
		
		String tutorialMessage18 = "너- 일기 잘 안쓴다- 맞지-"; // 일기 잘 안쓰는 사람,
		String tutorialMessage19 = "너- 일기 잘 안쓴다- 나는 실망했다-"; // 실망스러운 사람,
		
		String tutorialMessage20 = "오랜만이다#_# 오랜만이자만 너 기억한다- 철수 맞으시죠? 아니시라고요. 죄송합니다."; // 철수,
		String tutorialMessage21 = "삐리삐리- 너- %d일만이다."; // 철수,
		
		String tutorialMessage22 = "너- 시간 기부했다- 시간이 남아도냐- 시간은 금이다-"; // 시간이 남아도는 사람,
		String tutorialMessage23 = "너- 음악 샀다- 'OJ Time'이란 음악 들어봤냐? 내가 만든 거다-"; // 음악 산 사람,
		
		String tutorialMessage24 = "퍼블릭 광장에 가봤냐? 나- 거기서 100시간짜리 포스트잇으로 도배한 적 있다- 시간 무척 많다;"; // 광장시장 가고 싶어,
		String tutorialMessage25 = "나 로봇이다#_# 고사양이다- 너- 저사양이다- 멍청하다- 하하하;"; // 갑자기 멍청해진 %nickname,
		
		return StringUtil.randomString(tutorialMessage1,
				tutorialMessage2,
				tutorialMessage3,
				tutorialMessage4,
				tutorialMessage5,
				tutorialMessage6,
				tutorialMessage7,
				tutorialMessage8,
				tutorialMessage9,
				tutorialMessage10,
				tutorialMessage11,
				tutorialMessage12,
				tutorialMessage13,
				tutorialMessage14,
				tutorialMessage15,
				tutorialMessage16,
				tutorialMessage17,
				tutorialMessage18,
				tutorialMessage19,
				tutorialMessage20,
				tutorialMessage21,
				tutorialMessage22,
				tutorialMessage23,
				tutorialMessage24,
				tutorialMessage25);
	}

	private String generateDescription() {
		return "A person who decided to write a diary diligently";
		// TODO author의 상태를 기준으로 생성된다. 앱의 핵심기능 중 하나. 하루에 한번 가능.
//		return StringUtil.randomString("일기를 거의 안 쓰는 사람",
//				"쉬지않고 일만 하는 사람",
//				"움직이지 않고 밥만 먹는 사람",
//				"초콜렛 기부왕",
//				"똑똑한 사람",
//				"초콜렛 기부왕");
	}
}
