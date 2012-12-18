package de.ifcore.argue.domain.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String>
{
	private final static Pattern pattern = Pattern
			.compile("^[A-Za-z0-9üöäÜÖÄ][A-Za-z0-9üöäÜÖÄ\\Q -.\\E]*[A-Za-z0-9üöäÜÖÄ]$");
	private int minLength;
	private int maxLength;

	@Override
	public void initialize(Username constraintAnnotation)
	{
		minLength = constraintAnnotation.minLength();
		maxLength = constraintAnnotation.maxLength();
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context)
	{
		return username != null && username.length() >= minLength && username.length() <= maxLength
				&& pattern.matcher(username).matches();
	}

	public static Pattern getPattern()
	{
		return pattern;
	}
}
