package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, String> {
}
