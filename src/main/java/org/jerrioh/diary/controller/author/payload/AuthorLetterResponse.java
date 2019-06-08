package org.jerrioh.diary.controller.author.payload;

public class AuthorLetterResponse {
	private String letterId;
	private int letterType;
	private String fromAuthorId;
	private String fromAuthorNickname;
	private String toAuthorId;
	private String toAuthorNickname;
	private String content;
	private long writtenTime;
	
	public String getLetterId() {
		return letterId;
	}
	public void setLetterId(String letterId) {
		this.letterId = letterId;
	}
	public int getLetterType() {
		return letterType;
	}
	public void setLetterType(int letterType) {
		this.letterType = letterType;
	}
	public String getFromAuthorId() {
		return fromAuthorId;
	}
	public void setFromAuthorId(String fromAuthorId) {
		this.fromAuthorId = fromAuthorId;
	}
	public String getFromAuthorNickname() {
		return fromAuthorNickname;
	}
	public void setFromAuthorNickname(String fromAuthorNickname) {
		this.fromAuthorNickname = fromAuthorNickname;
	}
	public String getToAuthorId() {
		return toAuthorId;
	}
	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
	}
	public String getToAuthorNickname() {
		return toAuthorNickname;
	}
	public void setToAuthorNickname(String toAuthorNickname) {
		this.toAuthorNickname = toAuthorNickname;
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
