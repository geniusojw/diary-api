package org.jerrioh.diary.controller;

import org.jerrioh.common.OdMessageSource;
import org.jerrioh.common.validator.OdValidator;
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
	protected OdMessageSource messageSource;

	@Autowired
	protected AuthenticationManager authenticationManager;
}
