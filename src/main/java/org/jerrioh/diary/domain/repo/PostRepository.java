package org.jerrioh.diary.domain.repo;

import java.sql.Timestamp;
import java.util.List;

import org.jerrioh.diary.domain.Post;
import org.jerrioh.diary.domain.Post.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, String> {
	
	@Query(value = "SELECT * FROM POST "
			+ "WHERE POST_STATUS = " + PostStatus.NOT_POSTED + " "
			+ "AND author_id = :authorId "
			+ "LIMIT 1", nativeQuery = true)
	Post findMyNotPosted(@Param("authorId") String authorId);
	
	@Query(value = "SELECT * FROM POST "
			+ "WHERE POST_STATUS = 1 "
			+ "AND WRITTEN_TIME >= :fromTime "
			+ "AND LANGUAGE = :language "
			+ "AND COUNTRY = :country "
			+ "AND TIME_ZONE_ID = :timeZoneId "
			+ "ORDER BY CHOCOLATES, WRITTEN_TIME DESC "
			+ "LIMIT 50", nativeQuery = true)
	List<Post> findTodaysPosts(@Param("fromTime") Timestamp fromTime, @Param("language") String language, @Param("country") String country, @Param("timeZoneId") String timeZoneId);
}
