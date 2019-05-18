package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.AuthorLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorLetterRepository extends JpaRepository<AuthorLetter, String> {

	AuthorLetter findByLetterId(String letterId);
	List<AuthorLetter> findByToAuthorId(String toAuthorId);
}
