package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "author")
public class Author {
	@Id
	@Column(name = "author_id")
	private String authorId;

	@Column(name = "author_code")
	private String authorCode;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "description")
	private String description;
	
	@Column(name = "chocolates")
	private int chocolates;

	@Column(name = "is_deleted")
	private boolean isDeleted;

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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
