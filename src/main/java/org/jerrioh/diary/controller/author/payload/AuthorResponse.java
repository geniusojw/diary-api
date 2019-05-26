package org.jerrioh.diary.controller.author.payload;

public class AuthorResponse {
	private String authorId;
	private String authorCode;
	private String nickname;
	private String description;
	private int chocolates;

	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorCode() {
		return authorCode;
	}
	public void setAuthorCode(String authorCode) {
		this.authorCode = authorCode;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getChocolates() {
		return chocolates;
	}
	public void setChocolates(int chocolates) {
		this.chocolates = chocolates;
	}
}
