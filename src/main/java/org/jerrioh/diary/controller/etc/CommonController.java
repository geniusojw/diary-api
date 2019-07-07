package org.jerrioh.diary.controller.etc;

import javax.validation.Valid;

import org.jerrioh.common.exception.OdException;
import org.jerrioh.common.exception.OdResponseType;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.controller.etc.payload.AppResponse;
import org.jerrioh.diary.controller.payload.ApiResponse;
import org.jerrioh.diary.domain.App;
import org.jerrioh.diary.domain.repo.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/app")
public class CommonController extends AbstractController {

	@Autowired
	private AppRepository appRepository;
	
//    @GetMapping("/application-information")
//	public ResponseEntity<ApiResponse<ApplicationInformation>> getInformation() {
//    	ApplicationInformation information = new ApplicationInformation();
//    	
//    	List<String> tips = new ArrayList<>();
//    	tips.add("시간은 금이다 새기들아.");
//    	tips.add("일기는 매일 12시에 저장됩니다. 일기는 그날 그날 씁시다.");
//    	
//    	information.setServertime(System.currentTimeMillis());
//    	information.setTips(tips);
//    	
//    	return ApiResponse.make(OdResponseType.OK, information);
//	}
    
    @GetMapping("/versions/{version}")
	public ResponseEntity<ApiResponse<AppResponse>> getInformation(@Valid @PathVariable String version) throws OdException {
    	App app = appRepository.findById(version).orElseThrow(() -> new OdException(OdResponseType.APP_NOT_FOUND));
    	App latestApp = appRepository.findTopByOrderByVersionDesc();
    	
    	AppResponse response = new AppResponse();
    	response.setCurrentVersion(app.getVersion());
    	response.setCurrentStatus(response.getCurrentStatus());
    	response.setLatestVersion(latestApp.getVersion());
    	
    	return ApiResponse.make(OdResponseType.OK, response);
	}
}
