package org.jerrioh.common;

import org.springframework.http.HttpStatus;

public enum OdResponseType {
	OK(HttpStatus.OK.value(), 0, "OK"),

	BAD_REQUEST(400, 0, "Bad Request"),
	UNAUTHORIZED(401, 0, "Unauthorized"),
	
	NOT_FOUND(404, 0, "Not Found"),
	USER_NOT_FOUND(404, 1, "User Not Found"),
	DIARY_NOT_FOUND(404, 2, "Diary Not Found"),
	
	CONFLICT(409, 0, "Conflict"),
	USER_CONFLICT(409, 1, "User Already Exsits"),
	DIARY_CONFLICT(409, 2, "Diary Already Exsits"),
	
	INTERNAL_SERVER_ERROR(500, 0, "Internal Server Error");
	
	private int statusCode;
	private int detailCode;
	private int code;
	private String defaultMessage;
	
	private OdResponseType(int statusCode, int detailCode, String defaultMessage) {
		this.statusCode = statusCode;
		this.detailCode = detailCode;
		this.code = Integer.parseInt(String.format("%03d", statusCode).concat(String.format("%03d", detailCode)));
		this.defaultMessage = defaultMessage;
	}

	public int statusCode() {
		return statusCode;
	}
	
	public int code() {
		return code;
	}

	public String message() {
		return defaultMessage;
	}
	
	public static OdResponseType getODResponseTypeByStatusCode(int statusCode) {
		for (OdResponseType odResponseType : OdResponseType.values()) {
			if (statusCode == odResponseType.statusCode && odResponseType.detailCode == 0) {
				return odResponseType;
			}
		}
		return null;
	}
}
