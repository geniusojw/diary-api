package org.jerrioh.diary.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jerrioh.common.util.Log;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ODErrorController implements ErrorController {
	private static final String ERROR_PAGE = "/error";

	@RequestMapping("/error")
	public ResponseEntity<ApiResponse<String>> handleError(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		Log.error("error page directed");
		ApiResponse<String> apiResponse = new ApiResponse<>();
		apiResponse.setCode("ERROR - TBD");
		apiResponse.setMessage("ERROR - TBD");
		return ResponseEntity.ok().body(apiResponse);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PAGE;
	}
}
