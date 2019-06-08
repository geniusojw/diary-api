package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, String> {
	Author findByAuthorId(String authorId);
	Author findByAuthorIdAndAuthorCode(String authorId, String authorCode);
	
	@Query(value = "SELECT A.* FROM AUTHOR A JOIN AUTHOR_ANALYZED AA ON A.AUTHOR_ID = AA.AUTHOR_ID "
			+ "WHERE A.IS_DELETED = 0 "
			+ "AND A.AUTHOR_ID != :excludeAuthorId "
			+ "AND AA.LANGUAGE = :language "
			+ "AND AA.COUNTRY = :country "
			+ "AND AA.TIME_ZONE_ID = :timeZoneId "
			+ "AND (SELECT COUNT(1) FROM AUTHOR_DIARY AD WHERE A.AUTHOR_ID = AD.AUTHOR_ID AND AD.DIARY_DATE > NOW() - INTERVAL 40 DAY) >= 2 "
			+ "ORDER BY RAND() "
			+ "LIMIT 1", nativeQuery = true)
	Author findRandomAuthor(@Param("excludeAuthorId") String excludeAuthorId, @Param("language") String language, @Param("country") String country, @Param("timeZoneId") String timeZoneId);
}
