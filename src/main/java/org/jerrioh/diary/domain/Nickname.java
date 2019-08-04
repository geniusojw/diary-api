package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nickname")
public class Nickname {
	@Id
	@Column(name = "nickname")
	private String nickname;

	@Column(name = "nickname_class_name")
	private String nicknameClassName;

	@Column(name = "language")
	private String language;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNicknameClassName() {
		return nicknameClassName;
	}

	public void setNicknameClassName(String nicknameClassName) {
		this.nicknameClassName = nicknameClassName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
