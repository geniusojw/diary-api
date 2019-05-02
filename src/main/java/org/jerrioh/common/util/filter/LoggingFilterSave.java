package org.jerrioh.common.util.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jerrioh.common.util.OdLogger;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class LoggingFilterSave extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper requestWrapper = wrapRequest(request);
		ContentCachingResponseWrapper responseWrapper = wrapResponse(response);
		try {
			beforeRequest(requestWrapper);
			filterChain.doFilter(request, response);
		} finally {
			afterRequest(responseWrapper);
			responseWrapper.copyBodyToResponse();
		}
	}

	protected void beforeRequest(ContentCachingRequestWrapper request) {
		String requestURI = request.getRequestURI();
		if (!StringUtils.isEmpty(request.getQueryString())) {
			requestURI += "?" + request.getQueryString();
		}
		OdLogger.info("REQUEST:  {} {}", request.getMethod(), requestURI);
		for (String headerName : Collections.list(request.getHeaderNames())) {
			for (String headerValue : Collections.list(request.getHeaders(headerName))) {
				OdLogger.info(" - {} : {}", headerName, headerValue);
			}
		}
		
		byte[] content = request.getContentAsByteArray();
		if (content.length > 0) {
			String contentString;
			try {
				contentString = new String(content, request.getCharacterEncoding());
				OdLogger.info("REQUEST BODY : {}", contentString);
			} catch (UnsupportedEncodingException e) {
				OdLogger.info("UnsupportedEncodingException", e);
			}
		}
	}

	protected void afterRequest(ContentCachingResponseWrapper response) {
		OdLogger.info("RESPONSE:  {} {}", response.getStatus(), HttpStatus.valueOf(response.getStatus()).getReasonPhrase());
		for (String headerName : response.getHeaderNames()) {
			for (String headerValue : response.getHeaders(headerName)) {
				OdLogger.info(" - {} : {}", headerName, headerValue);
			}
		}
		
		byte[] content = response.getContentAsByteArray();
		if (content.length > 0) {
			String contentString;
			try {
				contentString = new String(content, response.getCharacterEncoding());
				OdLogger.info("RESPONSE BODY : {}", contentString);
			} catch (UnsupportedEncodingException e) {
				OdLogger.info("UnsupportedEncodingException", e);
			}
		}
	}

	private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
		if (request instanceof ContentCachingRequestWrapper) {
			return (ContentCachingRequestWrapper) request;
		} else {
			return new ContentCachingRequestWrapper(request);
//			return WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		}
	}

	private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
		if (response instanceof ContentCachingResponseWrapper) {
			return (ContentCachingResponseWrapper) response;
		} else {
			return new ContentCachingResponseWrapper(response);
//			return WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
		}
	}
}