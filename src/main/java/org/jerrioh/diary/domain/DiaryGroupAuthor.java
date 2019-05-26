package org.jerrioh.diary.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.jerrioh.common.Code;
import org.jerrioh.diary.domain.DiaryGroupAuthor.DiaryGroupAuthorPk;

@Entity
@Table(name = "diary_group_author")
@IdClass(DiaryGroupAuthorPk.class)
public class DiaryGroupAuthor {
	public static class AuthorStatus extends Code {
		public static final int INVITE = 0;
		public static final int ACCEPT = 1;
		public static final int REFUSE = 2;
	}

	public static class DiaryGroupAuthorPk implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "diary_group_id")
		private long diaryGroupId;

		@Id
		@Column(name = "author_id")
		private String authorId;
	}

	@Id
	@Column(name = "diary_group_id")
	private long diaryGroupId;

	@Id
	@Column(name = "author_id")
	private String authorId;

	@Column(name = "author_status")
	private int authorStatus;

	public long getDiaryGroupId() {
		return diaryGroupId;
	}

	public void setDiaryGroupId(long diaryGroupId) {
		this.diaryGroupId = diaryGroupId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public int getAuthorStatus() {
		return authorStatus;
	}

	public void setAuthorStatus(int authorStatus) {
		this.authorStatus = authorStatus;
	}
}
