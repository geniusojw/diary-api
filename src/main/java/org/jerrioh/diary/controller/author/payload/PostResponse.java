package org.jerrioh.diary.controller.author.payload;

public class PostResponse {
	
	private String postId;
	private String authorNickname;
	private int chocolates;
	private String title;
	private String content;
	private long writtenTime;

	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getAuthorNickname() {
		return authorNickname;
	}
	public void setAuthorNickname(String authorNickname) {
		this.authorNickname = authorNickname;
	}
	public int getChocolates() {
		return chocolates;
	}
	public void setChocolates(int chocolates) {
		this.chocolates = chocolates;
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
