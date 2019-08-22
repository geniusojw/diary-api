package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
