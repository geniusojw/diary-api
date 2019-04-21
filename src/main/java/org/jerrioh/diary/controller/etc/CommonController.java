package org.jerrioh.diary.controller.etc;

import org.jerrioh.common.OdResponseType;
import org.jerrioh.common.util.TimeUtil;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController extends AbstractController {
	
    @GetMapping("/server-time")
	public ResponseEntity<ApiResponse<String>> getInformation() {
    	return ApiResponse.make(OdResponseType.OK, TimeUtil.serverTime());
	}

}
