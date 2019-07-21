package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.FeedbackAuthor;
import org.jerrioh.diary.domain.FeedbackAuthor.FeedbackAuthorPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface FeedbackAuthorRepository extends JpaRepository<FeedbackAuthor, FeedbackAuthorPk> {

	FeedbackAuthor findByFromAuthorIdAndToAuthorIdAndDiaryGroupId(
			@Param("fromAuthorId") String fromAuthorId, @Param("toAuthorId") String toAuthorId,
			@Param("diaryGroupId") Long diaryGroupId);
}
