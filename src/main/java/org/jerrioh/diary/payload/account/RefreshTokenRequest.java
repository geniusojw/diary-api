package org.jerrioh.diary.payload.account;

import javax.validation.constraints.NotNull;

public class RefreshTokenRequest {
	@NotNull
	private String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
