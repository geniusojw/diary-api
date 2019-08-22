package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback {
	@Id
	@Column(name = "feedback_author_type")
	private int feedbackAuthorType;

	@Column(name = "korean_description")
	private String koreanDescription;

	@Column(name = "english_description")
	private String englishDescription;

	@Column(name = "factor_neuroticism")
	private int factorNeuroticism; // 신경성(N)

	@Column(name = "factor_extraversion")
	private int factorExtraversion; // 외향성(E)

	@Column(name = "factor_openness")
	private int factorOpenness; // (경험에 대한) 개방성(O)

	@Column(name = "factor_agreeableness")
	private int factorAgreeableness; // 우호성(A)

	@Column(name = "factor_conscientiousness")
	private int factorConscientiousness; // 성실성(C)

	public int getFeedbackAuthorType() {
		return feedbackAuthorType;
	}

	public void setFeedbackAuthorType(int feedbackAuthorType) {
		this.feedbackAuthorType = feedbackAuthorType;
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

	public int getFactorNeuroticism() {
		return factorNeuroticism;
	}

	public void setFactorNeuroticism(int factorNeuroticism) {
		this.factorNeuroticism = factorNeuroticism;
	}

	public int getFactorExtraversion() {
		return factorExtraversion;
	}

	public void setFactorExtraversion(int factorExtraversion) {
		this.factorExtraversion = factorExtraversion;
	}

	public int getFactorOpenness() {
		return factorOpenness;
	}

	public void setFactorOpenness(int factorOpenness) {
		this.factorOpenness = factorOpenness;
	}

	public int getFactorAgreeableness() {
		return factorAgreeableness;
	}

	public void setFactorAgreeableness(int factorAgreeableness) {
		this.factorAgreeableness = factorAgreeableness;
	}

	public int getFactorConscientiousness() {
		return factorConscientiousness;
	}

	public void setFactorConscientiousness(int factorConscientiousness) {
		this.factorConscientiousness = factorConscientiousness;
	}
}
