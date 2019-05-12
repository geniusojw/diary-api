package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
	Account findByAccountEmail(String accountEmail);
}
