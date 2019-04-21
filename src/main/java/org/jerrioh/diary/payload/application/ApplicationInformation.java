package org.jerrioh.diary.payload.application;

import java.util.List;

public class ApplicationInformation {
	private long servertime;
	private List<String> tips;

	public long getServertime() {
		return servertime;
	}
	public void setServertime(long servertime) {
		this.servertime = servertime;
	}
	public List<String> getTips() {
		return tips;
	}
	public void setTips(List<String> tips) {
		this.tips = tips;
	}
}
