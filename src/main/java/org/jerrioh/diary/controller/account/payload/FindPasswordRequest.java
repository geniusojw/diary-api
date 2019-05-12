package org.jerrioh.diary.controller.account.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class FindPasswordRequest {
	@NotNull @Email
	private String accountEmail;

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}
}
