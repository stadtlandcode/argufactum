package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.ifcore.argue.services.UserService;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String>
{
	private final UserService userService;

	@Inject
	public UniqueUserEmailValidator(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void initialize(UniqueUserEmail constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context)
	{
		return email == null || !userService.isEmailTaken(email);
	}
}
