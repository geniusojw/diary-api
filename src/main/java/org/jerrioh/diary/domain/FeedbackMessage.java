package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_message")
public class FeedbackMessage {
	@Id
	@Column(name = "feedback_message_id")
	private long feedbackMessageId;

	@Column(name = "korean_description")
	private String koreanDescription;

	@Column(name = "english_description")
	private String englishDescription;

	@Column(name = "factor_type")
	private int factorType;

	@Column(name = "negative")
	private boolean negative;

	public long getFeedbackMessageId() {
		return feedbackMessageId;
	}

	public void setFeedbackMessageId(long feedbackMessageId) {
		this.feedbackMessageId = feedbackMessageId;
	}

	public String getKoreanDescription() {
		return koreanDescription;
	}

	public void setKoreanDescription(String koreanDescription) {
		this.koreanDescription = koreanDescription;
	}

	public String getEnglishDescription() {
		return englishDescription;
	}

	public void setEnglishDescription(String englishDescription) {
		this.englishDescription = englishDescription;
	}

	public int getFactorType() {
		return factorType;
	}

	public void setFactorType(int factorType) {
		this.factorType = factorType;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}
}
