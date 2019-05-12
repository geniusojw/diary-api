package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorRequest {
	@NotNull @Size(min = 36, max = 100)
	private String authorId;

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
