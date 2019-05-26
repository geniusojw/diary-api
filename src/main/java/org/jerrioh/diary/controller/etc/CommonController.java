package org.jerrioh.diary.controller.etc;

import java.util.ArrayList;
import java.util.List;

import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.controller.etc.payload.ApplicationInformation;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController extends AbstractController {
    @GetMapping("/application-information")
	public ResponseEntity<ApiResponse<ApplicationInformation>> getInformation() {
    	ApplicationInformation information = new ApplicationInformation();
    	
    	List<String> tips = new ArrayList<>();
    	tips.add("시간은 금이다 새기들아.");
    	tips.add("일기는 매일 12시에 저장됩니다. 일기는 그날 그날 씁시다.");
    	
    	information.setServertime(System.currentTimeMillis());
    	information.setTips(tips);
    	
    	return ApiResponse.make(OdResponseType.OK, information);
	}
}
