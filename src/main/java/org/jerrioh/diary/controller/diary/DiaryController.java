package org.jerrioh.diary.controller.diary;

import java.util.List;

import org.jerrioh.common.util.Log;
import org.jerrioh.diary.payload.ApiResponse;
import org.jerrioh.diary.payload.diary.DiaryRequest;
import org.jerrioh.diary.payload.diary.DiaryResponse;
import org.jerrioh.diary.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {
	@Autowired
	private DiaryService diaryService;

	@GetMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<DiaryResponse>> read(@PathVariable(name = "writeDay") String writeDay) {
		Log.info("read");

		ApiResponse<DiaryResponse> apiResponse = new ApiResponse<>();
		return ResponseEntity.ok().body(apiResponse);
	}

	@PostMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<Object>> write(@PathVariable(name = "writeDay") String writeDay, @RequestBody DiaryRequest diaryRequest) {
		Log.info("write. diaryRequest=", diaryRequest);
		
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		return ResponseEntity.ok().body(apiResponse);
	}

	@DeleteMapping(value = "/{writeDay}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(name = "writeDay") String writeDay) {
		Log.info("delete");
		
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		return ResponseEntity.ok().body(apiResponse);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<DiaryResponse>>> readAll() {
		Log.info("readAll");

		ApiResponse<List<DiaryResponse>> apiResponse = new ApiResponse<>();
		return ResponseEntity.ok().body(apiResponse);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> writeAll(@RequestBody List<DiaryRequest> diaryRequest) {
		Log.info("writeAll");
		
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		return ResponseEntity.ok().body(apiResponse);
	}

}
