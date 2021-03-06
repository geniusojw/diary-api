package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "author_analyzed")
public class AuthorAnalyzed {
	@Id
	@Column(name = "author_id")
	private String authorId;

	@Column(name = "language")
	private String language;

	@Column(name = "country")
	private String country;

	@Column(name = "time_zone_id")
	private String timeZoneId;
	
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
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
