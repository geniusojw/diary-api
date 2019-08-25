package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wise_saying")
public class WiseSaying {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "saying_id")
	private long sayingId;

	@Column(name = "language")
	private String language;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "saying")
	private String saying;

	@Column(name = "author_created")
	private boolean authorCreated;

	public long getSayingId() {
		return sayingId;
	}

	public void setSayingId(long sayingId) {
		this.sayingId = sayingId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSaying() {
		return saying;
	}

	public void setSaying(String saying) {
		this.saying = saying;
	}

	public boolean isAuthorCreated() {
		return authorCreated;
	}

	public void setAuthorCreated(boolean authorCreated) {
		this.authorCreated = authorCreated;
	}
}
