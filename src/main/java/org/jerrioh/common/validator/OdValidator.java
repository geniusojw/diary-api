package org.jerrioh.common.validator;

import java.util.Collection;

import javax.validation.Validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

public class OdValidator implements Validator {
	private SpringValidatorAdapter validator;

	public OdValidator() {
		this.validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target instanceof Collection) {
			for (Object object : (Collection<?>) target) {
				validate(object, errors);
			}
		} else {
			validator.validate(target, errors);
		}
	}
}
