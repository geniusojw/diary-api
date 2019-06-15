package org.jerrioh.diary.controller.account;

import org.jerrioh.common.exception.OdAuthenticationException;
import org.jerrioh.diary.controller.AbstractController;
import org.jerrioh.diary.domain.Account;
import org.jerrioh.diary.domain.repo.AccountDiaryRepository;
import org.jerrioh.diary.domain.repo.AccountRepository;
import org.jerrioh.security.authentication.after.CompleteAccountToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractAccountController extends AbstractController {
	
	@Autowired
	protected AccountRepository accountRepository;
	@Autowired
	protected AccountDiaryRepository accountDiaryRepository;

	protected Account getAccount() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof CompleteAccountToken && authentication.isAuthenticated()) {
			return (Account) authentication.getPrincipal();
		}
		throw new OdAuthenticationException();
	}
}
