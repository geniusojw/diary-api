package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, String> {
	Author findByAuthorId(String authorId);
	Author findByAuthorIdAndAuthorCode(String authorId, String authorCode);
}
