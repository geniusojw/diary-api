package org.jerrioh.diary.controller.author.payload;

public class AuthorDiaryGroupResponse {
	private String authorId;
	private String nickname;
	private boolean firstDay;
	private String yesterdayDate;
	private String yesterdayTitle;
	private String yesterdayContent;
	private String todayDate;
	private String todayTitle;
	private String todayContent;
	
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public boolean isFirstDay() {
		return firstDay;
	}
	public void setFirstDay(boolean firstDay) {
		this.firstDay = firstDay;
	}
	public String getYesterdayDate() {
		return yesterdayDate;
	}
	public void setYesterdayDate(String yesterdayDate) {
		this.yesterdayDate = yesterdayDate;
	}
	public String getYesterdayTitle() {
		return yesterdayTitle;
	}
	public void setYesterdayTitle(String yesterdayTitle) {
		this.yesterdayTitle = yesterdayTitle;
	}
	public String getYesterdayContent() {
		return yesterdayContent;
	}
	public void setYesterdayContent(String yesterdayContent) {
		this.yesterdayContent = yesterdayContent;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	public String getTodayTitle() {
		return todayTitle;
	}
	public void setTodayTitle(String todayTitle) {
		this.todayTitle = todayTitle;
	}
	public String getTodayContent() {
		return todayContent;
	}
	public void setTodayContent(String todayContent) {
		this.todayContent = todayContent;
	}
}
