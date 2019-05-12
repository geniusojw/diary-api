package org.jerrioh.diary.controller.account.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDiaryRequest {
	@NotNull
	private String diaryDate;
	private String title;
	@Size(min = 0, max = 3000)
	private String content;
	
	public String getDiaryDate() {
		return diaryDate;
	}
	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
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
