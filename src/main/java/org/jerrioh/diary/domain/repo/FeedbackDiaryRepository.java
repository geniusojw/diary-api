package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.FeedbackDiary;
import org.jerrioh.diary.domain.FeedbackDiary.FeedbackDiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface FeedbackDiaryRepository extends JpaRepository<FeedbackDiary, FeedbackDiaryPk> {

	FeedbackDiary findByFromAuthorIdAndToAuthorIdAndAndDiaryDateAndFeedbackDiaryType(
			@Param("fromAuthorId") String fromAuthorId, @Param("toAuthorId") String toAuthorId,
			@Param("diaryDate") String diaryDate, @Param("feedbackDiaryType") int feedbackDiaryType);

	List<FeedbackDiary> findByToAuthorIdAndAndDiaryDate(String authorId, String todayString);
}
