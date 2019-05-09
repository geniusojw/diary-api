package org.jerrioh.diary.controller.account;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.exception.OdException;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController extends AbstractController {

	@GetMapping(value = "/alias")
	public ResponseEntity<ApiResponse<Object>> aboutMe() throws OdException {

		
		return ApiResponse.make(OdResponseType.OK);
	}

}
