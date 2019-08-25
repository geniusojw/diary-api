package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class CreateWiseSayingRequest {
	@NotNull
	private String saying;

	public String getSaying() {
		return saying;
	}

	public void setSaying(String saying) {
		this.saying = saying;
	}
}
