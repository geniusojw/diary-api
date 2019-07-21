package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.common.Code;
import org.jerrioh.diary.domain.FeedbackAuthor.FeedbackAuthorPk;

@Entity
@Table(name = "feedback_author")
@IdClass(FeedbackAuthorPk.class)
public class FeedbackAuthor {
	public static class FeedbackAuthorType extends Code {
		public static final int WRITE = 0;
		// 1이상은 다양한 의미를 지니며, DB 및 프로퍼티로 관리된다.
	}
	
	public static class FeedbackAuthorPk implements Serializable {
		private static final long serialVersionUID = 1L;
		
		@Id
		@Column(name = "from_author_id")
		private String fromAuthorId;

		@Id
		@Column(name = "to_author_id")
		private String toAuthorId;

		@Id
		@Column(name = "diary_group_id")
		private Long diaryGroupId;
	}
	
	@Id
	@Column(name = "from_author_id")
	private String fromAuthorId;

	@Id
	@Column(name = "to_author_id")
	private String toAuthorId;

	@Id
	@Column(name = "diary_group_id")
	private Long diaryGroupId;

	@Column(name = "feedback_author_type")
	private int feedbackAuthorType;

	@Column(name = "feedback_author_write")
	private String feedbackAuthorWrite;

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

	public Long getDiaryGroupId() {
		return diaryGroupId;
	}

	public void setDiaryGroupId(Long diaryGroupId) {
		this.diaryGroupId = diaryGroupId;
	}

	public int getFeedbackAuthorType() {
		return feedbackAuthorType;
	}

	public void setFeedbackAuthorType(int feedbackAuthorType) {
		this.feedbackAuthorType = feedbackAuthorType;
	}

	public String getFeedbackAuthorWrite() {
		return feedbackAuthorWrite;
	}

	public void setFeedbackAuthorWrite(String feedbackAuthorWrite) {
		this.feedbackAuthorWrite = feedbackAuthorWrite;
	}
	
	
}
