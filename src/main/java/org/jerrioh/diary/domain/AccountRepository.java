package org.jerrioh.diary.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
	List<Account> findByUserId(String userId);
}
