package org.jerrioh.diary.payload.diary;

public class DiaryResponse {
	private String writeDay;
	private String writeUserId;
	private String title;
	private String content;

	public String getWriteDay() {
		return writeDay;
	}
	public void setWriteDay(String writeDay) {
		this.writeDay = writeDay;
	}
	public String getWriteUserId() {
		return writeUserId;
	}
	public void setWriteUserId(String writeUserId) {
		this.writeUserId = writeUserId;
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
}
