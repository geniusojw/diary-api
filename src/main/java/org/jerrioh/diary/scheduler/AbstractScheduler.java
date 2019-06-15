package org.jerrioh.diary.scheduler;

import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.AuthorDiaryRepository;
import org.jerrioh.diary.domain.repo.AuthorLetterRepository;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupAuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractScheduler {
	
	protected static final String EVERY_0_SECOND = "0 * * * * *";
	protected static final String EVERY_0_AND_12_HOUR = "0 0 0,12 * * *";
	protected static final String EVERY_0_AND_30_MINUTE = "0 0/30 * * * *";
	
	@Autowired
	protected AuthorRepository authorRepository;
	@Autowired
	protected AuthorAnalyzedRepository authorAnalyzedRepository;
	@Autowired
	protected AuthorDiaryRepository authorDiaryRepository;
	@Autowired
	protected AuthorLetterRepository authorLetterRepository;
	@Autowired
	protected DiaryGroupRepository diaryGroupRepository;
	@Autowired
	protected DiaryGroupAuthorRepository diaryGroupAuthorRepository;

}
