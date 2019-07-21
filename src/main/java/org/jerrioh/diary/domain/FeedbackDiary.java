package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.common.Code;
import org.jerrioh.diary.domain.FeedbackDiary.FeedbackDiaryPk;

@Entity
@Table(name = "feedback_diary")
@IdClass(FeedbackDiaryPk.class)
public class FeedbackDiary {
	public static class FeedbackDiaryType extends Code {
		public static final int GOOD = 0;
		public static final int LIKE = 1;
	}
	
	public static class FeedbackDiaryPk implements Serializable {
		private static final long serialVersionUID = 1L;
		
		@Id
		@Column(name = "from_author_id")
		private String fromAuthorId;

		@Id
		@Column(name = "to_author_id")
		private String toAuthorId;

		@Id
		@Column(name = "diary_date")
		private String diaryDate;

		@Id
		@Column(name = "feedback_diary_type")
		private int feedbackDiaryType;
	}
	
	@Id
	@Column(name = "from_author_id")
	private String fromAuthorId;

	@Id
	@Column(name = "to_author_id")
	private String toAuthorId;

	@Id
	@Column(name = "diary_date")
	private String diaryDate;

	@Id
	@Column(name = "feedback_diary_type")
	private int feedbackDiaryType;

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

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public int getFeedbackDiaryType() {
		return feedbackDiaryType;
	}

	public void setFeedbackDiaryType(int feedbackDiaryType) {
		this.feedbackDiaryType = feedbackDiaryType;
	}
}
