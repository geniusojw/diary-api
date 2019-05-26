package org.jerrioh.diary.controller.author.payload;

public class AuthorLetterResponse {
	private String letterId;
	private String fromAuthorId;
	private String title;
	private String content;
	private long writtenTime;
	
	public String getLetterId() {
		return letterId;
	}
	public void setLetterId(String letterId) {
		this.letterId = letterId;
	}
	public String getFromAuthorId() {
		return fromAuthorId;
	}
	public void setFromAuthorId(String fromAuthorId) {
		this.fromAuthorId = fromAuthorId;
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
	public long getWrittenTime() {
		return writtenTime;
	}
	public void setWrittenTime(long writtenTime) {
		this.writtenTime = writtenTime;
	}
}
