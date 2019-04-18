package org.jerrioh.diary.controller.etc;

import org.jerrioh.common.util.TimeUtil;
import org.jerrioh.diary.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
	
    @GetMapping("/server-time")
	public ResponseEntity<ApiResponse<String>> getInformation() {
    	ApiResponse<String> apiResponse = new ApiResponse<>();
    	apiResponse.setData(TimeUtil.serverTime());
		return ResponseEntity.ok(apiResponse);
	}

}
