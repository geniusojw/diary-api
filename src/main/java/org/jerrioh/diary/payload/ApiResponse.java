package org.jerrioh.diary.payload;

import org.jerrioh.common.OdResponseType;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> {
	private int statusCode;
	private int code;
	private String message;
	private T data;

	public static ResponseEntity<ApiResponse<Object>> make(OdResponseType odResponseType) {
		return make(odResponseType, null);
	}
	public static <T> ResponseEntity<ApiResponse<T>> make(OdResponseType odResponseType, T data) {
		return makeWithMessage(odResponseType, odResponseType.message(), data);
	}
	public static ResponseEntity<ApiResponse<Object>> makeWithMessage(OdResponseType odResponseType, String message) {
		return makeWithMessage(odResponseType, message, null);
	}
	public static <T> ResponseEntity<ApiResponse<T>> makeWithMessage(OdResponseType odResponseType, String message, T data) {
		ApiResponse<T> apiResponse = new ApiResponse<>(odResponseType.statusCode(), odResponseType.code(), message, data);
		return ResponseEntity.status(odResponseType.statusCode()).body(apiResponse);
	}
	public static ResponseEntity<ApiResponse<Object>> makeUnknown(int statusCode, String message) {
		ApiResponse<Object> apiResponse = new ApiResponse<>(statusCode, -1, message, null);
		return ResponseEntity.status(statusCode).body(apiResponse);
	}
	private ApiResponse(int statusCode, int code, String message, T data) {
		this.statusCode = statusCode;
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public int getHttpStatus() {
		return statusCode;
	}
	public void setHttpStatus(int httpStatus) {
		this.statusCode = httpStatus;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
