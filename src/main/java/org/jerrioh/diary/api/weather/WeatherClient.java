package org.jerrioh.diary.api.weather;

import org.jerrioh.diary.api.weather.payload.GetWeatherResponse;
import org.jerrioh.diary.scheduler.CacheScheduler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${apis.openweathermap.name}", url = "${apis.openweathermap.url}")
public interface WeatherClient {

	@GetMapping(value = "/data/2.5/weather")
	@Cacheable(value = CacheScheduler.CACHE_WEATHER)
	ResponseEntity<GetWeatherResponse> weather(@RequestParam String q, @RequestParam String appid);

}
