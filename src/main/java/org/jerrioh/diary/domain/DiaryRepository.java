package org.jerrioh.diary.domain;

import org.jerrioh.diary.domain.Diary.DiaryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, DiaryPk> {
}
