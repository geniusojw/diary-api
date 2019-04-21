package org.jerrioh.diary.payload.diary;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DiaryWriteBulkRequest {
	@NotNull
	private String writeDay;
	private String title;
	@NotNull
	@Size(min = 1, max = 2000)
	private String content;

	public String getWriteDay() {
		return writeDay;
	}
	public void setWriteDay(String writeDay) {
		this.writeDay = writeDay;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
