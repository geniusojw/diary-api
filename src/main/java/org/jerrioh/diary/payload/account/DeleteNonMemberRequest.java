package org.jerrioh.diary.payload.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class DeleteNonMemberRequest {
	@NotNull @Email
	private String memberUserId;

	public String getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(String memberUserId) {
		this.memberUserId = memberUserId;
	}
}
