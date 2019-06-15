package org.jerrioh.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.Nullable;

public class OdMessageSource extends ResourceBundleMessageSource {
	
	private Map<String, Locale> localeMap = new HashMap<>();

	public OdMessageSource() {
		super();
		localeMap.put("kor", Locale.KOREAN);
		localeMap.put("eng", Locale.ENGLISH);
	}

	public String getMessage(String code, String language, @Nullable Object... args) {
		return super.getMessage(code, args, localeMap.getOrDefault(language, Locale.ENGLISH));
	}
}
