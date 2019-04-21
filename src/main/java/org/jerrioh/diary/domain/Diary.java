package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.diary.domain.Diary.DiaryPk;

@Entity
@Table(name = "diary")
@IdClass(DiaryPk.class)
public class Diary {
	public static class DiaryPk implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id @Column(name = "write_day")
		private String writeDay;

		@Id @Column(name = "write_user_id")
		private String writeUserId;
	}

	@Id @Column(name = "write_day")
	private String writeDay;

	@Id @Column(name = "write_user_id")
	private String writeUserId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

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
