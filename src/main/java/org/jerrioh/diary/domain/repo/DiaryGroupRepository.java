package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.DiaryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryGroupRepository extends JpaRepository<DiaryGroup, Long> {
	
	@Query(value = "SELECT dg.* FROM diary_group dg, diary_group_author dga "
			+ "WHERE dg.diary_group_id = dga.diary_group_id "
			+ "AND dg.end_time > now() "
			+ "AND dga.author_id = :authorId", nativeQuery = true)
	DiaryGroup findByAuthorId(@Param("authorId") String authorId);
	
	@Query(value = "SELECT dg.* FROM diary_group dg "
			+ "WHERE dg.start_time > now() "
			+ "AND dg.language = :language "
			+ "AND dg.country = :country "
			+ "AND dg.time_zone_id = :timeZoneId "
			+ "AND dg.max_author_count > (SELECT COUNT(1) FROM diary_group_author dga WHERE dga.diary_group_id = dg.diary_group_id AND dga.author_status IN (0, 1)) "
			+ "ORDER BY dg.start_time DESC "
			+ "LIMIT 1", nativeQuery = true)
	DiaryGroup findInviteDiaryGroup(@Param("language") String language, @Param("country") String country, @Param("timeZoneId") String timeZoneId);
}
