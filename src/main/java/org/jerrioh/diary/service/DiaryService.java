package org.jerrioh.diary.service;

import org.jerrioh.diary.domain.Diary;
import org.jerrioh.diary.domain.Diary.DiaryPk;
import org.jerrioh.diary.domain.DiaryRepository;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
	private DiaryRepository diaryRepository;
	
	public Diary read(String writeUserId, String writeDay) {
		DiaryPk diaryPk = new DiaryPk();
		diaryPk.setWriteDay(writeDay);
		diaryPk.setWriteUserId(writeUserId);
		
		return diaryRepository.getOne(diaryPk);
	}

}
