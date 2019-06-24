package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DiaryGroupRespondRequest {
	public static class InvitationResponseType {
		public static final String ACCEPT = "accept";
		public static final String REFUSE = "refuse";
	}
	
	@NotNull
	@Pattern(regexp = "accept|refuse")
	private String invitationResponseType;

	public String getInvitationResponseType() {
		return invitationResponseType;
	}

	public void setInvitationResponseType(String invitationResponseType) {
		this.invitationResponseType = invitationResponseType;
	}
}
