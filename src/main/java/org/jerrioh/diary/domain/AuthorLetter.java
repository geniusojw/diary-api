package org.jerrioh.diary.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "letter")
public class AuthorLetter {
	@Id
	@Column(name = "letter_id")
	private String letterId;
	
	@Column(name = "from_author_id")
	private String fromAuthorId;

	@Column(name = "to_author_id")
	private String toAuthorId;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "writtenTime")
	private Timestamp writtenTime;

	public String getLetterId() {
		return letterId;
	}

	public void setLetterId(String letterId) {
		this.letterId = letterId;
	}

	public String getFromAuthorId() {
		return fromAuthorId;
	}

	public void setFromAuthorId(String fromAuthorId) {
		this.fromAuthorId = fromAuthorId;
	}

	public String getToAuthorId() {
		return toAuthorId;
	}

	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
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

	public Timestamp getWrittenTime() {
		return writtenTime;
	}

	public void setWrittenTime(Timestamp writtenTime) {
		this.writtenTime = writtenTime;
	}
}
