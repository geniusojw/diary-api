package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class UpdateDiaryGroupRequest {
	@NotNull
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
