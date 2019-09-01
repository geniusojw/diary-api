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
			+ "AND (SELECT COUNT(1) FROM AUTHOR_DIARY AD WHERE A.AUTHOR_ID = AD.AUTHOR_ID AND AD.DIARY_DATE > NOW() - INTERVAL 10 DAY) >= 2 "
			+ "ORDER BY RAND() "
			+ "LIMIT 1", nativeQuery = true)
	Author findRandomAuthor(@Param("excludeAuthorId") String excludeAuthorId, @Param("language") String language, @Param("country") String country, @Param("timeZoneId") String timeZoneId);
	
	@Query(value = "SELECT NOW() > CREATION_TIME + INTERVAL :wiseSayingHours HOUR FROM AUTHOR_CHOCOLATES_HISTORY WHERE AUTHOR_ID = :authorId AND DETAILS LIKE 'ITEM_WISE_SAYING%' ORDER BY CREATION_TIME DESC LIMIT 1;", nativeQuery = true)
	Integer wiseSayingGetable(String authorId, int wiseSayingHours);

	@Query(value = "SELECT NOW() > CREATION_TIME + INTERVAL :hours HOUR FROM AUTHOR_DESCRIPTION_HISTORY WHERE AUTHOR_ID = :authorId ORDER BY SEQUENCE DESC LIMIT 1;", nativeQuery = true)
	int descriptionChangable(@Param("authorId") String authorId, @Param("hours") int hours);
	
	@Query(value = "SELECT NOW() > CREATION_TIME + INTERVAL :hours HOUR FROM AUTHOR_NICKNAME_HISTORY WHERE AUTHOR_ID = :authorId ORDER BY SEQUENCE DESC LIMIT 1;", nativeQuery = true)
	int nickNameChangable(@Param("authorId") String authorId, @Param("hours") int hours);
	
	@Query(value = "INSERT INTO AUTHOR_DESCRIPTION_HISTORY (AUTHOR_ID, SEQUENCE, DESCRIPTION) "
			+ "SELECT AUTHOR_ID, (SELECT IFNULL(MAX(SEQUENCE), 0) + 1 FROM AUTHOR_DESCRIPTION_HISTORY WHERE AUTHOR_ID = :authorId), DESCRIPTION "
			+ "FROM AUTHOR WHERE AUTHOR_ID = :authorId", nativeQuery = true)
	void insertDescriptionHistory(@Param("authorId") String authorId);
	
	@Query(value = "INSERT INTO AUTHOR_NICKNAME_HISTORY (AUTHOR_ID, SEQUENCE, NICKNAME) "
			+ "SELECT AUTHOR_ID, (SELECT IFNULL(MAX(SEQUENCE), 0) + 1 FROM AUTHOR_NICKNAME_HISTORY WHERE AUTHOR_ID = :authorId), NICKNAME "
			+ "FROM AUTHOR WHERE AUTHOR_ID = :authorId", nativeQuery = true)
	void insertNickNameHistory(@Param("authorId") String authorId);
	
	@Query(value = "INSERT INTO AUTHOR_CHOCOLATES_HISTORY (AUTHOR_ID, SEQUENCE, CHOCOLATES_CHANGED, CHOCOLATES_RESULT, DETAILS) "
			+ "SELECT AUTHOR_ID, (SELECT IFNULL(MAX(SEQUENCE), 0) + 1 FROM AUTHOR_CHOCOLATES_HISTORY WHERE AUTHOR_ID = :authorId), :chocolatesChanged, CHOCOLATES, :details "
			+ "FROM AUTHOR WHERE AUTHOR_ID = :authorId", nativeQuery = true)
	void insertChocolateHistory(@Param("authorId") String authorId, @Param("chocolatesChanged") int chocolatesChanged, @Param("details") String details);
	
	@Query(value = "SELECT IFNULL(SUM(CHOCOLATES_CHANGED), 0) FROM AUTHOR_CHOCOLATES_HISTORY "
			+ "WHERE AUTHOR_ID = :authorId "
			+ "AND DETAILS = :details ", nativeQuery = true)
	int sumOfChocolatesUsed(@Param("authorId") String authorId, @Param("details") String details);
}
