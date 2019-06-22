package org.jerrioh.diary.scheduler;

import org.jerrioh.common.util.OdLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler extends AbstractScheduler {

	public static final String CACHE_WEATHER = "cache.weather";

	@Autowired
	private CacheManager cacheManager;

	@Scheduled(cron = EVERY_0_AND_30_MINUTE)
	public void clearWeatherCache() {
		evictAllCaches();
	}

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> {
			OdLogger.info("{} cleared.", cacheName);
			cacheManager.getCache(cacheName).clear();
		});
	}
}
