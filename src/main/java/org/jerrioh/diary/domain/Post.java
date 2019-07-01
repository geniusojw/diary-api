package org.jerrioh.diary.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jerrioh.common.Code;

@Entity
@Table(name = "post")
public class Post {
	public static class PostStatus extends Code {
		public static final int NOT_POSTED = 0;
		public static final int POSTED = 1;
	}
	
	@Id
	@Column(name = "post_id")
	private String postId;

	@Column(name = "post_status")
	private int postStatus;
	
	@Column(name = "author_id")
	private String authorId;
	
	@Column(name = "author_nickname")
	private String authorNickname;

	@Column(name = "chocolates")
	private int chocolates;

	@Column(name = "content")
	private String content;

	@Column(name = "language")
	private String language;

	@Column(name = "country")
	private String country;

	@Column(name = "time_zone_id")
	private String timeZoneId;
	
	@Column(name = "written_time")
	private Timestamp writtenTime;

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public int getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(int postStatus) {
		this.postStatus = postStatus;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorNickname() {
		return authorNickname;
	}

	public void setAuthorNickname(String authorNickname) {
		this.authorNickname = authorNickname;
	}

	public int getChocolates() {
		return chocolates;
	}

	public void setChocolates(int chocolates) {
		this.chocolates = chocolates;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public Timestamp getWrittenTime() {
		return writtenTime;
	}

	public void setWrittenTime(Timestamp writtenTime) {
		this.writtenTime = writtenTime;
	}
}
