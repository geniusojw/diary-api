package org.jerrioh.diary.controller.author.payload;

public class AuthorDiaryGroupResponse {
	
	private String authorId;
	private String nickname;
	private String yesterdayTitle;
	private String yesterdayContent;;
	private String todayTitle;
	private String todayContent;
	private boolean todayLike;
	private boolean yesterdayGood;

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
	public boolean isTodayLike() {
		return todayLike;
	}
	public void setTodayLike(boolean todayLike) {
		this.todayLike = todayLike;
	}
	public boolean isYesterdayGood() {
		return yesterdayGood;
	}
	public void setYesterdayGood(boolean yesterdayGood) {
		this.yesterdayGood = yesterdayGood;
	}
}
