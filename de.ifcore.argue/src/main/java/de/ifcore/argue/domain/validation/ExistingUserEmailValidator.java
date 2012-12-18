package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import de.ifcore.argue.dao.UserDao;

public class ExistingUserEmailValidator implements ConstraintValidator<ExistingUserEmail, String>
{
	private final UserDao userDao;

	@Inject
	public ExistingUserEmailValidator(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public void initialize(ExistingUserEmail constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context)
	{
		return StringUtils.isBlank(email) || userDao.findByEmail(email) != null;
	}
}
