package org.jerrioh.common;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer, WebMvcRegistrations {
	
	@Bean
	public MessageSource messageSource() {
		OdMessageSource messageSource = new OdMessageSource();
		messageSource.setBasename("i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
