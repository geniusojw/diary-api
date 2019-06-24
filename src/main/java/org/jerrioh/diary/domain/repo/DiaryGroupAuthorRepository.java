package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.DiaryGroupAuthor;
import org.jerrioh.diary.domain.DiaryGroupAuthor.DiaryGroupAuthorPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryGroupAuthorRepository extends JpaRepository<DiaryGroupAuthor, DiaryGroupAuthorPk> {

	List<DiaryGroupAuthor> findByDiaryGroupId(long diaryGroupId);
	DiaryGroupAuthor findByDiaryGroupIdAndAuthorId(long diaryGroupId, String authorId);
}
