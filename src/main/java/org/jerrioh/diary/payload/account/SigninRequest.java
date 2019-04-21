package org.jerrioh.diary.payload.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SigninRequest {
	@NotNull
	private String userId;
	@NotNull @Size(min = 4, max = 10)
	private String password;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
