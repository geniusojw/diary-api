package org.jerrioh.diary.controller.account.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountRequest {
	@NotNull @Email
	private String accountEmail;
	
	@NotNull @Size(min = 4, max = 15)
	private String password;
	
	@NotNull
	private String authorId;

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
