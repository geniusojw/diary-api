package org.jerrioh.diary.controller.author.payload;

import java.util.List;

public class YesterdayDiariesResponse {
	
	private boolean firstDay;
	private boolean lastDay;
	private String yesterdayDate;
	private String todayDate;
	private List<AuthorDiaryGroupResponse> authorDiaries;

	public boolean isFirstDay() {
		return firstDay;
	}
	public void setFirstDay(boolean firstDay) {
		this.firstDay = firstDay;
	}
	public boolean isLastDay() {
		return lastDay;
	}
	public void setLastDay(boolean lastDay) {
		this.lastDay = lastDay;
	}
	public String getYesterdayDate() {
		return yesterdayDate;
	}
	public void setYesterdayDate(String yesterdayDate) {
		this.yesterdayDate = yesterdayDate;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	public List<AuthorDiaryGroupResponse> getAuthorDiaries() {
		return authorDiaries;
	}
	public void setAuthorDiaries(List<AuthorDiaryGroupResponse> authorDiaries) {
		this.authorDiaries = authorDiaries;
	}
}
