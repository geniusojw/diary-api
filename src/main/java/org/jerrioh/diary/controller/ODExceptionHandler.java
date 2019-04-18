package org.jerrioh.diary.controller;

import org.jerrioh.common.exception.ODAuthenticationException;
import org.jerrioh.common.exception.ODException;
import org.jerrioh.common.util.Log;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ODExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
		Log.error("Exception occured");
		return ResponseEntity.ok().build();
	}
	
	@ExceptionHandler(value = ODException.class)
	public ResponseEntity<ApiResponse<String>> handleException(ODException e) {
		Log.error("ODException occured");
		return ResponseEntity.ok().build();
	}
	
	@ExceptionHandler(value = ODAuthenticationException.class)
	public ResponseEntity<ApiResponse<String>> handleException(ODAuthenticationException e) {
		Log.error("ODAuthenticationException occured");
		return ResponseEntity.ok().build();
	}
}
