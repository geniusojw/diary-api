package org.jerrioh.diary.controller;

import org.jerrioh.common.validator.OdValidator;
import org.jerrioh.diary.domain.AccountRepository;
import org.jerrioh.diary.domain.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class AbstractController {
	@InitBinder
	private void initBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new OdValidator());
	}

	@Autowired
	protected AuthenticationManager authenticationManager;
	@Autowired
	protected AccountRepository accountRepository;
	@Autowired
	protected DiaryRepository diaryRepository;
}
