package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.AuthorDiary;
import org.jerrioh.diary.domain.AuthorDiary.AuthorDiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDiaryRepository extends JpaRepository<AuthorDiary, AuthorDiaryPk> {
	AuthorDiary findByAuthorIdAndDiaryDate(String authorId, String diaryDate);
	List<AuthorDiary> findByAuthorId(String authorId);
}
