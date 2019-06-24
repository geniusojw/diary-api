package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DiaryGroupSupportRequest {
	public static class SupportType {
		public static final String SCALE = "scale"; // max author count
		public static final String PERIOD = "period"; // end time
	}
	
	@NotNull
	@Pattern(regexp = "scale|period")
	private String supportType;

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
}
