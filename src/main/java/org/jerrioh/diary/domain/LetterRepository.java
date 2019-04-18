package org.jerrioh.diary.domain;

import org.jerrioh.diary.domain.Letter.LetterPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, LetterPk> {
}
