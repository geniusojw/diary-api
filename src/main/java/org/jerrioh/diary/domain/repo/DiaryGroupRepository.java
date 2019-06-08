package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.DiaryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryGroupRepository extends JpaRepository<DiaryGroup, Long> {
	
	@Query(value = "SELECT DG.* FROM DIARY_GROUP DG, DIARY_GROUP_AUTHOR DGA "
			+ "WHERE DG.DIARY_GROUP_ID = DGA.DIARY_GROUP_ID "
			+ "AND DG.END_TIME > NOW() "
			+ "AND DGA.AUTHOR_ID = :authorId", nativeQuery = true)
	DiaryGroup findByAuthorId(@Param("authorId") String authorId);
	
	@Query(value = "SELECT DG.* FROM DIARY_GROUP DG "
			+ "WHERE DG.START_TIME > NOW() "
			+ "AND DG.LANGUAGE = :language "
			+ "AND DG.COUNTRY = :country "
			+ "AND DG.TIME_ZONE_ID = :timeZoneId "
			+ "AND DG.MAX_AUTHOR_COUNT > (SELECT COUNT(1) FROM DIARY_GROUP_AUTHOR DGA WHERE DGA.DIARY_GROUP_ID = DG.DIARY_GROUP_ID AND DGA.AUTHOR_STATUS IN (0, 1)) "
			+ "ORDER BY DG.START_TIME DESC "
			+ "LIMIT 1", nativeQuery = true)
	DiaryGroup findInviteDiaryGroup(@Param("language") String language, @Param("country") String country, @Param("timeZoneId") String timeZoneId);
}
