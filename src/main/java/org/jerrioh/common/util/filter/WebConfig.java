package org.jerrioh.common.util.filter;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer, WebMvcRegistrations {

//	@Bean
//	public FilterRegistrationBean<LoggingFilter> loggingFilter2() {
//		FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>();
//		bean.setFilter(new LoggingFilter());
//		bean.addUrlPatterns("/**/*");
//		return bean;
//	}
}
