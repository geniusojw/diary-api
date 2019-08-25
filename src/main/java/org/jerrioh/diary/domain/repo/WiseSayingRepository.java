package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.WiseSaying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WiseSayingRepository extends JpaRepository<WiseSaying, Long> {

	@Query(value = "SELECT * "
			+ "FROM WISE_SAYING "
			+ "WHERE LANGUAGE = :language "
			+ "AND AUTHOR_CREATED = :authorCreated "
			+ "ORDER BY RAND() "
			+ "LIMIT 1", nativeQuery = true)
	WiseSaying findOneByLanguage(@Param("language") String language, @Param("authorCreated") boolean authorCreated);
}
