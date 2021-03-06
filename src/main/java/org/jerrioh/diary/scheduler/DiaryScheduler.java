package org.jerrioh.diary.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.domain.Author;
import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.jerrioh.diary.domain.AuthorDiary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiaryScheduler extends AbstractScheduler {
	
	@Scheduled(cron = ON_0_AND_12_HOUR)
	public void updateAuthorAnalyzed() {
		OdLogger.info("Scheduler - updateAuthorAnalyzed() started");
		
		BiFunction<String, Integer, Integer> increaseCountFunction = (key, count) -> {
			return count == null ? 1 : count + 1;
		};
		
		List<Author> allAuthors = authorRepository.findAll();
		
		List<AuthorAnalyzed> allAuthorAnalyzeds = authorAnalyzedRepository.findAll();
		Map<String, AuthorAnalyzed> authorAnalyzedMap = allAuthorAnalyzeds.stream().collect(Collectors.toMap(AuthorAnalyzed::getAuthorId, analyzed -> analyzed));

		for (Author author : allAuthors) {
			if (author.isDeleted()) {
				continue;
			}
			List<AuthorDiary> authorDiaries = authorDiaryRepository.findByAuthorIdOrderByDiaryDateDesc(author.getAuthorId());
			if (authorDiaries.size() < 3) {
				continue;
			}
			
			String language = null;
			String country = null;
			String timeZoneId = null;
			
			Map<String, Integer> languageCountMap = new HashMap<>();
			Map<String, Integer> countryCountMap = new HashMap<>();
			Map<String, Integer> timeZoneIdCountMap = new HashMap<>();
			
			boolean hasEnoughInformation = false;
			for (AuthorDiary authorDiary : authorDiaries) {
				languageCountMap.compute(authorDiary.getLanguage(), increaseCountFunction);
				countryCountMap.compute(authorDiary.getCountry(), increaseCountFunction);
				timeZoneIdCountMap.compute(authorDiary.getTimeZoneId(), increaseCountFunction);

				if (language == null && languageCountMap.get(authorDiary.getLanguage()) >= 2) {
					language = authorDiary.getLanguage();
				}
				if (country == null && countryCountMap.get(authorDiary.getCountry()) >= 2) {
					country = authorDiary.getCountry();
				}
				if (timeZoneId == null && timeZoneIdCountMap.get(authorDiary.getTimeZoneId()) >= 2) {
					timeZoneId = authorDiary.getTimeZoneId();
				}
				
				if (StringUtils.isNoneEmpty(language, country, timeZoneId)) {
					hasEnoughInformation = true;
					break;
				}
			}
			
			if (hasEnoughInformation) {
				AuthorAnalyzed authorAnalyzed = authorAnalyzedMap.get(author.getAuthorId());
				if (authorAnalyzed == null) {
					authorAnalyzed = new AuthorAnalyzed();
					authorAnalyzed.setAuthorId(author.getAuthorId());
					
					authorAnalyzed.setFactorNeuroticism(0);
					authorAnalyzed.setFactorExtraversion(0);
					authorAnalyzed.setFactorOpenness(0);
					authorAnalyzed.setFactorAgreeableness(0);
					authorAnalyzed.setFactorConscientiousness(0);
					
					OdLogger.info("new author!!");
				}

				if (!StringUtils.equals(language, authorAnalyzed.getLanguage())
						|| !StringUtils.equals(country, authorAnalyzed.getCountry())
						|| !StringUtils.equals(timeZoneId, authorAnalyzed.getTimeZoneId())) {
					authorAnalyzed.setLanguage(language);
					authorAnalyzed.setCountry(country);
					authorAnalyzed.setTimeZoneId(timeZoneId);
					
					authorAnalyzedRepository.save(authorAnalyzed);
					OdLogger.info("saved. authorId = {}", author.getAuthorId());
				}
			}
		}

		OdLogger.info("Scheduler - updateAuthorAnalyzed() finished");
	}
}
