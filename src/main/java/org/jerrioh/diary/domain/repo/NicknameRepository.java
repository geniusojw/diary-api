package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NicknameRepository extends JpaRepository<Nickname, String> {
	
	@Query(value = "SELECT * "
			+ "FROM NICKNAME "
			+ "WHERE NICKNAME NOT IN (SELECT DISTINCT NICKNAME FROM AUTHOR) "
			+ "AND LANGUAGE = :language "
			+ "ORDER BY RAND() "
			+ "LIMIT 1", nativeQuery = true)
	Nickname findNotUsedOne(@Param("language") String language);
}
