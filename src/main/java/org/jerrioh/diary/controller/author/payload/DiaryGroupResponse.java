package org.jerrioh.diary.controller.author.payload;

public class DiaryGroupResponse {
	private long diaryGroupId;
	private String diaryGroupName;
	private String keyword;
	private int maxAuthorCount;
	private String country;
	private String language;
	private String timeZoneId;
	private long startTime;
	private long endTime;
	
	public long getDiaryGroupId() {
		return diaryGroupId;
	}
	public void setDiaryGroupId(long diaryGroupId) {
		this.diaryGroupId = diaryGroupId;
	}
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
	public int getMaxAuthorCount() {
		return maxAuthorCount;
	}
	public void setMaxAuthorCount(int maxAuthorCount) {
		this.maxAuthorCount = maxAuthorCount;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
