package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class PostRequest {
	@NotNull
	private String content;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
