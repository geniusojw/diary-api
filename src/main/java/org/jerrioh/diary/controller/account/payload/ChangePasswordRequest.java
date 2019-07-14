package org.jerrioh.diary.controller.account.payload;

import javax.validation.constraints.NotNull;

public class ChangePasswordRequest {
	
	@NotNull
	private String oldPassword;
	@NotNull
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
