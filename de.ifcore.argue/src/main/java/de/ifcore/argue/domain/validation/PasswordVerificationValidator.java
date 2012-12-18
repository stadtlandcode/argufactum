package de.ifcore.argue.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import de.ifcore.argue.domain.form.PasswordInput;

public class PasswordVerificationValidator implements ConstraintValidator<PasswordVerification, PasswordInput>
{
	@Override
	public void initialize(PasswordVerification constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(PasswordInput input, ConstraintValidatorContext context)
	{
		return StringUtils.equals(input.getPassword(), input.getPassword2());
	}
}
