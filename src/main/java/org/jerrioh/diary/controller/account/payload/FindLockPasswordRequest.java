package org.jerrioh.diary.controller.account.payload;

import javax.validation.constraints.NotNull;

public class FindLockPasswordRequest {
	@NotNull
	private String lock;

	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}
}
