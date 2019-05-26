package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class InviteTicketRequest {
	private String diaryGroupName;
	private String keyword;
	@NotNull
	private String language;
	@NotNull
	private String country;
	@NotNull
	private String timeZoneId;
	
	public String getDiaryGroupName() {
		return diaryGroupName;
	}
	public void setDiaryGroupName(String diaryGroupName) {
		this.diaryGroupName = diaryGroupName;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
