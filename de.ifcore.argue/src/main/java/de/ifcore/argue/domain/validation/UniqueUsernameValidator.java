package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.ifcore.argue.dao.UserDao;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>
{
	private final UserDao userDao;

	@Inject
	public UniqueUsernameValidator(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public void initialize(UniqueUsername constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context)
	{
		return username == null || userDao.findByUsername(username) == null;
	}
}
