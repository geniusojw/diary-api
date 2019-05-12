package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorDiaryRequest {
	@NotNull
	private String diaryDate;
	private String title;
	@Size(min = 0, max = 3000)
	private String content;
	@NotNull
	private String language;
	@NotNull
	private String country;
	
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
}
