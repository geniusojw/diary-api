package org.jerrioh.diary.domain.repo;

import java.util.List;

import org.jerrioh.diary.domain.AccountDiary;
import org.jerrioh.diary.domain.AccountDiary.AccountDiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDiaryRepository extends JpaRepository<AccountDiary, AccountDiaryPk> {
	AccountDiary findByAccountEmailAndDiaryDate(String accountEmail, String diaryDate);
	List<AccountDiary> findByAccountEmail(String accountEmail);
}
