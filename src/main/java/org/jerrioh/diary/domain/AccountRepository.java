package org.jerrioh.diary.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
	Optional<Account> findById(String userId);
}
