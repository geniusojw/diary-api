package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.diary.domain.AuthorDiary.AuthorDiaryPk;

@Entity
@Table(name = "author_diary")
@IdClass(AuthorDiaryPk.class)
public class AuthorDiary {
	public static class AuthorDiaryPk implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "author_id")
		private String authorId;

		@Id
		@Column(name = "diary_date")
		private String diaryDate;
	}

	@Id
	@Column(name = "author_id")
	private String authorId;

	@Id
	@Column(name = "diary_date")
	private String diaryDate;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

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

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
