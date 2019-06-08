package org.jerrioh.diary.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jerrioh.common.Code;

@Entity
@Table(name = "letter")
public class AuthorLetter {
	public static class LetterType extends Code {
		public static final int NORMAL = 0;
		public static final int INVITATION = 1;
	}
	
	@Id
	@Column(name = "letter_id")
	private String letterId;
	
	@Column(name = "letter_type")
	private int letterType;
	
	@Column(name = "from_author_id")
	private String fromAuthorId;
	
	@Column(name = "from_author_nickname")
	private String fromAuthorNickname;

	@Column(name = "to_author_id")
	private String toAuthorId;

	@Column(name = "to_author_nickname")
	private String toAuthorNickname;

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

	public int getLetterType() {
		return letterType;
	}

	public void setLetterType(int letterType) {
		this.letterType = letterType;
	}

	public String getFromAuthorId() {
		return fromAuthorId;
	}

	public void setFromAuthorId(String fromAuthorId) {
		this.fromAuthorId = fromAuthorId;
	}

	public String getFromAuthorNickname() {
		return fromAuthorNickname;
	}

	public void setFromAuthorNickname(String fromAuthorNickname) {
		this.fromAuthorNickname = fromAuthorNickname;
	}

	public String getToAuthorId() {
		return toAuthorId;
	}

	public void setToAuthorId(String toAuthorId) {
		this.toAuthorId = toAuthorId;
	}

	public String getToAuthorNickname() {
		return toAuthorNickname;
	}

	public void setToAuthorNickname(String toAuthorNickname) {
		this.toAuthorNickname = toAuthorNickname;
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
