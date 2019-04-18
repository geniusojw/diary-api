package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "letter")
public class Letter {
	@Embeddable
	public static class LetterPk implements Serializable {
		private static final long serialVersionUID = 1L;
		
		@Column(name = "write_day")
		private String writeDay;

		@Column(name = "write_user_id")
		private String writeUserId;

		@Column(name = "read_user_id")
		private String readUserId;

		public String getWriteDay() {
			return writeDay;
		}

		public void setWriteDay(String writeDay) {
			this.writeDay = writeDay;
		}

		public String getWriteUserId() {
			return writeUserId;
		}

		public void setWriteUserId(String writeUserId) {
			this.writeUserId = writeUserId;
		}

		public String getReadUserId() {
			return readUserId;
		}

		public void setReadUserId(String readUserId) {
			this.readUserId = readUserId;
		}
	}
	
	@EmbeddedId
	private LetterPk letterPk;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

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