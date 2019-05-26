package org.jerrioh.diary.controller.author.payload;

public class DiaryGroupAuthorResponse {
	private String authorId;
	private int authorStatus;
	
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public int getAuthorStatus() {
		return authorStatus;
	}
	public void setAuthorStatus(int authorStatus) {
		this.authorStatus = authorStatus;
	}
}
