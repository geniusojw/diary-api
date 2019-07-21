package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class FeedbackAuthorRequest {
	@NotNull
	private String toAuthorId;
	@NotNull
	private Integer feedbackAuthorType;

	private String feedbackAuthorWrite;

	public String getToAuthorId() {
		return toAuthorId;
	}

	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
	}

	public Integer getFeedbackAuthorType() {
		return feedbackAuthorType;
	}

	public void setFeedbackAuthorType(Integer feedbackAuthorType) {
		this.feedbackAuthorType = feedbackAuthorType;
	}

	public String getFeedbackAuthorWrite() {
		return feedbackAuthorWrite;
	}

	public void setFeedbackAuthorWrite(String feedbackAuthorWrite) {
		this.feedbackAuthorWrite = feedbackAuthorWrite;
	}
}
