package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class FeedbackDiaryRequest {
	@NotNull
	private String toAuthorId;
	@NotNull
	private String diaryDate;
	@NotNull
	private Integer feedbackDiaryType;

	public String getToAuthorId() {
		return toAuthorId;
	}

	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
	}

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public Integer getFeedbackDiaryType() {
		return feedbackDiaryType;
	}

	public void setFeedbackDiaryType(Integer feedbackDiaryType) {
		this.feedbackDiaryType = feedbackDiaryType;
	}
}
