package org.jerrioh.diary.scheduler;

import org.jerrioh.common.util.OdLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler extends AbstractScheduler {

	public static final String CACHE_WEATHER = "cache.weather";
	public static final String CACHE_FEEDBACK_AUTHOR = "cache.feedback.author";

	@Autowired
	private CacheManager cacheManager;

	@Scheduled(cron = ON_0_AND_30_MINUTE)
	public void clearWeatherCache() {
		OdLogger.info("Scheduler - clear cache() started");
//		evictCaches(CACHE_WEATHER);
		evictAllCaches();
		OdLogger.info("Scheduler - clear cache() finished");
	}

//	private void evictCaches(String cacheName) {
//		cacheManager.getCacheNames().stream().forEach(c -> {
//			if (StringUtils.equals(c, cacheName)) {
//				OdLogger.info("{} cleared.", c);
//				cacheManager.getCache(c).clear();	
//			}
//		});
//	}

	private void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> {
			OdLogger.info("{} cleared.", cacheName);
			cacheManager.getCache(cacheName).clear();
		});
	}
}
