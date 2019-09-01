package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.FeedbackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackMessageRepository extends JpaRepository<FeedbackMessage, Long> {
}
