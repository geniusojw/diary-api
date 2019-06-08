package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class AuthorLetterRequest {
	@NotNull
	private String letterId;
	private String toAuthorId;
	private String toAuthorNickname;
	@NotNull
	private String content;
	
	public String getLetterId() {
		return letterId;
	}
	public void setLetterId(String letterId) {
		this.letterId = letterId;
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
}
