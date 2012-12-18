package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.ifcore.argue.domain.form.RegistrationPasswordInput;
import de.ifcore.argue.domain.models.UserSession;

public class RegistrationPasswordVerificationValidator implements
		ConstraintValidator<RegistrationPasswordVerification, RegistrationPasswordInput>
{
	private final UserSession userSession;
	private int minLength;
	private int maxLength;

	@Inject
	public RegistrationPasswordVerificationValidator(UserSession userSession)
	{
		this.userSession = userSession;
	}

	@Override
	public void initialize(RegistrationPasswordVerification constraintAnnotation)
	{
		minLength = constraintAnnotation.minLength();
		maxLength = constraintAnnotation.maxLength();
	}

	@Override
	public boolean isValid(RegistrationPasswordInput input, ConstraintValidatorContext context)
	{
		boolean valid;
		if (input.getPassword() == null)
			valid = userSession.getStoredPassword(input.getUuid()) != null;
		else
			valid = input.getPassword().length() >= minLength && input.getPassword().length() <= maxLength;
		return valid;
	}
}
