package org.jerrioh.common.util.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LoggingFilter extends CommonsRequestLoggingFilter {

	public LoggingFilter() {
		super.setIncludeQueryString(true);
		super.setIncludeClientInfo(true);
		super.setIncludePayload(true);
		super.setIncludeHeaders(true);
		super.setMaxPayloadLength(1000);
	}

	private String beforeMessagePrefix = DEFAULT_BEFORE_MESSAGE_PREFIX;
	private String beforeMessageSuffix = DEFAULT_BEFORE_MESSAGE_SUFFIX;
	private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;
	private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;
	
	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return true;
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		logger.info(message);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		logger.info(message);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper requestToUse = request instanceof ContentCachingRequestWrapper
				? (ContentCachingRequestWrapper) request
				: new ContentCachingRequestWrapper(request, getMaxPayloadLength());
		ContentCachingResponseWrapper responseToUse = response instanceof ContentCachingResponseWrapper
				? (ContentCachingResponseWrapper) response
				: new ContentCachingResponseWrapper(response);

		try {
			filterChain.doFilter(requestToUse, responseToUse);
		} finally {
			beforeRequest(requestToUse, getBeforeMessage(requestToUse, beforeMessagePrefix, beforeMessageSuffix));
			afterRequest(requestToUse, getAfterMessage(responseToUse, afterMessagePrefix, afterMessageSuffix));
			responseToUse.copyBodyToResponse();
		}
	}

	private String getBeforeMessage(ContentCachingRequestWrapper request, String prefix, String suffix) {
		return super.createMessage(request, prefix, suffix);
	}

	private String getAfterMessage(ContentCachingResponseWrapper response, String prefix, String suffix) {
		StringBuilder msg = new StringBuilder();
		msg.append(prefix);
		msg.append("status=").append(response.getStatus()).append("(").append(HttpStatus.valueOf(response.getStatus()).getReasonPhrase()).append(")");
		
		if (isIncludeHeaders()) {
			HttpHeaders httpHeaders = new HttpHeaders();
			for (String headerName : response.getHeaderNames()) {
				for (String headerValue : response.getHeaders(headerName)) {
					httpHeaders.add(headerName, headerValue);
				}
			}
			msg.append(";headers=").append(httpHeaders);
		}

		if (isIncludePayload()) {
			byte[] content = response.getContentAsByteArray();
			if (content.length > 0) {
				try {
					msg.append(";payload=").append(new String(content, response.getCharacterEncoding()));
				} catch (UnsupportedEncodingException e) {
					msg.append(";payload=[unknown]");
				}
			}
		}

		msg.append(suffix);
		return msg.toString();
	}

}
