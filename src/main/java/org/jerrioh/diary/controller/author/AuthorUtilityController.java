package org.jerrioh.diary.controller.author;

import javax.validation.Valid;

import org.jerrioh.common.OdMessageSource;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.api.weather.WeatherClient;
import org.jerrioh.diary.controller.OdHeaders;
import org.jerrioh.diary.controller.author.payload.GetWeatherResponse;
import org.jerrioh.diary.controller.author.requestparam.GetWeatherParameter;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/author/util")
public class AuthorUtilityController extends AuthorController {
	
	@Value(value = "${apis.openweathermap.auth}")
	private String weatherApiKey;
	
	@Autowired
	private WeatherClient weatherClient;
	
	@Autowired
	private OdMessageSource messageSource;

	@GetMapping(value = "/weather")
	public ResponseEntity<ApiResponse<GetWeatherResponse>> getWeather(@Valid GetWeatherParameter parameter,
			@RequestHeader(value = OdHeaders.LANGUAGE) String language) throws OdException {
		String cityName = parameter.getCity();
		String countryCode = parameter.getCountry();
		
		String query = String.join(",", cityName, countryCode);
		ResponseEntity<org.jerrioh.diary.api.weather.payload.GetWeatherResponse> feignResponse = weatherClient.weather(query, weatherApiKey);
		String description = feignResponse.getBody().getWeather().get(0).getDescription();
		
		String message = messageSource.getMessage("weather.description", language, description);
		
		GetWeatherResponse response = new GetWeatherResponse();
		response.setDescription(message);
		return ApiResponse.make(OdResponseType.OK, response);
	}
}
