package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class FeedbackAuthorTypeRequest {
	@NotNull
	private String toAuthorId;

	public String getToAuthorId() {
		return toAuthorId;
	}

	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
	}
}
