package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.AuthorAnalyzed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorAnalyzedRepository extends JpaRepository<AuthorAnalyzed, String> {
	AuthorAnalyzed findByAuthorId(String authorId);
}
