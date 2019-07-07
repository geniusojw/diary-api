package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.diary.domain.AccountDiary.AccountDiaryPk;

@Entity
@Table(name = "account_diary")
@IdClass(AccountDiaryPk.class)
public class AccountDiary {
	public static class AccountDiaryPk implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "account_email")
		private String accountEmail;

		@Id
		@Column(name = "diary_date")
		private String diaryDate;
	}

	@Id
	@Column(name = "account_email")
	private String accountEmail;

	@Id
	@Column(name = "diary_date")
	private String diaryDate;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
