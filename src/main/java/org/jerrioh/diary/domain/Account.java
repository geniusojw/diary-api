package org.jerrioh.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
	@Id
	@Column(name = "account_email")
	private String accountEmail;

	@Column(name = "password_enc")
	private String passwordEnc;

	@Column(name = "first_author_id")
	private String firstAuthorId;

	@Column(name = "last_author_id")
	private String lastAuthorId;

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getPasswordEnc() {
		return passwordEnc;
	}

	public void setPasswordEnc(String passwordEnc) {
		this.passwordEnc = passwordEnc;
	}

	public String getFirstAuthorId() {
		return firstAuthorId;
	}

	public void setFirstAuthorId(String firstAuthorId) {
		this.firstAuthorId = firstAuthorId;
	}

	public String getLastAuthorId() {
		return lastAuthorId;
	}

	public void setLastAuthorId(String lastAuthorId) {
		this.lastAuthorId = lastAuthorId;
	}
}
