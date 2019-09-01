package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_result")
public class FeedbackResult {
	@Id
	@Column(name = "feedback_result_id")
	private long feedbackResultId;

	@Column(name = "korean_description")
	private String koreanDescription;

	@Column(name = "english_description")
	private String englishDescription;

	@Column(name = "ratio_neuroticism")
	private int ratioNeuroticism;

	@Column(name = "ratio_extraversion")
	private int ratioExtraversion;

	@Column(name = "ratio_openness")
	private int ratioOpenness;

	@Column(name = "ratio_agreeableness")
	private int ratioAgreeableness;

	@Column(name = "ratio_conscientiousness")
	private int ratiorConscientiousness;

	public long getFeedbackResultId() {
		return feedbackResultId;
	}

	public void setFeedbackResultId(long feedbackResultId) {
		this.feedbackResultId = feedbackResultId;
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

	public int getRatioNeuroticism() {
		return ratioNeuroticism;
	}

	public void setRatioNeuroticism(int ratioNeuroticism) {
		this.ratioNeuroticism = ratioNeuroticism;
	}

	public int getRatioExtraversion() {
		return ratioExtraversion;
	}

	public void setRatioExtraversion(int ratioExtraversion) {
		this.ratioExtraversion = ratioExtraversion;
	}

	public int getRatioOpenness() {
		return ratioOpenness;
	}

	public void setRatioOpenness(int ratioOpenness) {
		this.ratioOpenness = ratioOpenness;
	}

	public int getRatioAgreeableness() {
		return ratioAgreeableness;
	}

	public void setRatioAgreeableness(int ratioAgreeableness) {
		this.ratioAgreeableness = ratioAgreeableness;
	}

	public int getRatiorConscientiousness() {
		return ratiorConscientiousness;
	}

	public void setRatiorConscientiousness(int ratiorConscientiousness) {
		this.ratiorConscientiousness = ratiorConscientiousness;
	}
}
