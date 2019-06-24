package org.jerrioh.diary.scheduler;

import org.jerrioh.common.OdMessageSource;
import org.jerrioh.diary.domain.repo.AuthorAnalyzedRepository;
import org.jerrioh.diary.domain.repo.AuthorDiaryRepository;
import org.jerrioh.diary.domain.repo.AuthorLetterRepository;
import org.jerrioh.diary.domain.repo.AuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupAuthorRepository;
import org.jerrioh.diary.domain.repo.DiaryGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractScheduler {
	
	//https://zamezzz.tistory.com/197
	protected static final String EVERY_5_SECONDS = "0/5 * * * * *";
	protected static final String ON_0_SECOND = "0 * * * * *";
	protected static final String ON_0_AND_12_HOUR = "0 0 0,12 * * *";
	protected static final String ON_0_AND_30_MINUTE = "0 0,30 * * * *";
	protected static final String ON_29_AND_59_MINUTE = "0 29,59 * * * *";

	@Autowired
	protected OdMessageSource messageSource;
	
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
