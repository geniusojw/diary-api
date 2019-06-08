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
}
