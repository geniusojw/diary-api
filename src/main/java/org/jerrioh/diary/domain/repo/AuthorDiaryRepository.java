package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.AuthorDiary;
import org.jerrioh.diary.domain.AuthorDiary.AuthorDiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorDiaryRepository extends JpaRepository<AuthorDiary, AuthorDiaryPk> {
	
	AuthorDiary findByAuthorIdAndDiaryDate(String authorId, String diaryDate);
	List<AuthorDiary> findByAuthorIdOrderByDiaryDateDesc(String authorId);
	
	@Query(value = "SELECT * FROM AUTHOR_DIARY WHERE AUTHOR_ID = :authorId AND DIARY_DATE > NOW() - INTERVAL 7 DAY", nativeQuery = true)
	List<AuthorDiary> findByAuthorIdRecent7Days(@Param("authorId") String authorId);
}
