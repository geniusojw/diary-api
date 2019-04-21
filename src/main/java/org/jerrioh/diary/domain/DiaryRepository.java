package org.jerrioh.diary.domain;

import java.util.List;

import org.jerrioh.diary.domain.Diary.DiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, DiaryPk> {
	Diary findByWriteUserIdAndWriteDay(String writeUserId, String writeDay);
	List<Diary> findByWriteUserId(String writeUserId);
}
