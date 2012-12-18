package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import de.ifcore.argue.dao.UserDao;

public class ExistingUsernameValidator implements ConstraintValidator<ExistingUsername, String>
{
	private final UserDao userDao;

	@Inject
	public ExistingUsernameValidator(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public void initialize(ExistingUsername constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context)
	{
		return StringUtils.isBlank(username) || userDao.findByUsername(username) != null;
	}
}
