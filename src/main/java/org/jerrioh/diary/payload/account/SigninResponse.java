package org.jerrioh.diary.payload.account;

import javax.validation.constraints.NotNull;

public class SigninResponse {
	@NotNull
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
