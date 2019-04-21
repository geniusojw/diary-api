package org.jerrioh.diary.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.util.OdLogger;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OdExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
		OdLogger.error("Exception occured", e);
		return ApiResponse.makeWithMessage(OdResponseType.INTERNAL_SERVER_ERROR, e.toString());
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleException(MethodArgumentNotValidException e) {
		OdLogger.error("MethodArgumentNotValidException occured", e);
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		Set<String> errors = fieldErrors.stream().map(fe -> fe.getField() + ":" + fe.getDefaultMessage()).collect(Collectors.toSet());
		String responseMessage = String.join(", ", errors);
		return ApiResponse.makeWithMessage(OdResponseType.BAD_REQUEST, responseMessage);
	}

	@ExceptionHandler(value = OdException.class)
	public ResponseEntity<ApiResponse<Object>> handleException(OdException e) {
		OdLogger.error("ODException occured", e);
		return ApiResponse.make(e.getOdResponseType(), e.getData());
	}

	@ExceptionHandler(value = OdAuthenticationException.class)
	public ResponseEntity<ApiResponse<Object>> handleException(OdAuthenticationException e) {
		OdLogger.error("ODAuthenticationException occured", e);
		return ApiResponse.make(OdResponseType.UNAUTHORIZED);
	}
}
