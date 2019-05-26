package org.jerrioh.diary.controller.author.payload;

public class ChangeDescriptionResponse {
	private String description;
	private String aboutYou;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAboutYou() {
		return aboutYou;
	}

	public void setAboutYou(String aboutYou) {
		this.aboutYou = aboutYou;
	}
}
