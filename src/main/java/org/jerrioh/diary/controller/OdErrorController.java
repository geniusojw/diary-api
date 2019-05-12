package org.jerrioh.diary.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OdErrorController implements ErrorController {
	private static final String ERROR_PAGE = "/error";

	@RequestMapping("/error")
	public ResponseEntity<ApiResponse<Object>> handleError(HttpServletRequest request, HttpServletResponse response, Exception e) {
		OdResponseType odResponseType = OdResponseType.getODResponseTypeByStatusCode(response.getStatus());
		if (odResponseType == null) {
			return ApiResponse.makeUnknown(response.getStatus(), "Error");
		}
		return ApiResponse.make(odResponseType);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PAGE;
	}
}
