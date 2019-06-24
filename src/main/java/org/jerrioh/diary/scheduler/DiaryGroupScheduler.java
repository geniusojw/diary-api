package org.jerrioh.diary.scheduler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.DiaryGroup;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiaryGroupScheduler extends AbstractScheduler {
	private static final Random RANDOM = new Random();

	private static final int MESSAGE_KEY_MAX_INDEX_COMBINATION_WHERE_ASSIST = 20;
	private static final int MESSAGE_KEY_MAX_INDEX_COMBINATION_WHERE = 25;

	private static final int MESSAGE_KEY_MAX_INDEX_COMBINATION_WHO_ASSIST = 37;
	private static final int MESSAGE_KEY_MAX_INDEX_COMBINATION_WHO = 35;

	@Scheduled(cron = ON_29_AND_59_MINUTE)
	public void updateDiaryGroupName() {
		OdLogger.info("Scheduler - updateDiaryGroupName() started");
		DateTime dateTime = DateTime.now();
		List<DiaryGroup> diaryGroups = diaryGroupRepository.findByDiaryGroupNameIsNullAndStartTimeLessThan(new Timestamp(dateTime.getMillis()));
		OdLogger.info("diaryGroup.size() = {}", diaryGroups.size());
		
		for (DiaryGroup diaryGroup : diaryGroups) {
			String diaryGroupName = generateDiaryGroupName(diaryGroup.getLanguage());
			OdLogger.info("Diary group name generated! diaryGroupName = {}", diaryGroupName);

			diaryGroup.setDiaryGroupName(diaryGroupName);
			diaryGroupRepository.save(diaryGroup);
//			OdLogger.info("generated(kor) = {}", generateDiaryGroupName("kor"));
//			OdLogger.info("generated(eng) = {}", generateDiaryGroupName("eng"));
		}
		
		OdLogger.info("Scheduler - updateDiaryGroupName() finished");
	}

	private String generateDiaryGroupName(String language) {
		String messageKeyWhereAssist = "combination.where.assist" + RANDOM.nextInt(MESSAGE_KEY_MAX_INDEX_COMBINATION_WHERE_ASSIST + 1);
		String messageKeyWhere = "combination.where" + RANDOM.nextInt(MESSAGE_KEY_MAX_INDEX_COMBINATION_WHERE + 1);
		String messageKeyWhoAssist = "combination.who.assist" + RANDOM.nextInt(MESSAGE_KEY_MAX_INDEX_COMBINATION_WHO_ASSIST + 1);
		String messageKeyWho = "combination.who" + RANDOM.nextInt(MESSAGE_KEY_MAX_INDEX_COMBINATION_WHO + 1);

		
		String where = messageSource.getMessage(messageKeyWhere, language);
		if (RANDOM.nextInt(5) > 0) {
			where = messageSource.getMessage(messageKeyWhereAssist, language, where);
		}
		
		String who = messageSource.getMessage(messageKeyWho, language);
		if (RANDOM.nextInt(10) > 0) {
			who = messageSource.getMessage(messageKeyWhoAssist, language, who);	
		}
		
		if (Locale.KOREAN.getISO3Language().equals(language)) {
			return where + " " + who;	
		} else {
			char ch = (char) (who.charAt(0) - 32);
			return ch + who.substring(1) + " " + where;
		}
	}
}
