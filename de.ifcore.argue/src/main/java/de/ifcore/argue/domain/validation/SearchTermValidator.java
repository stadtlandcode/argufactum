package de.ifcore.argue.domain.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class SearchTermValidator implements ConstraintValidator<SearchTerm, String>
{
	private int minLength;
	private final Pattern pattern = Pattern.compile("[A-Za-z0-9- äöüÄÖÜß?!]+");

	@Override
	public void initialize(SearchTerm constraintAnnotation)
	{
		this.minLength = constraintAnnotation.min();
	}

	@Override
	public boolean isValid(String searchTerm, ConstraintValidatorContext context)
	{
		return StringUtils.isNotBlank(searchTerm) && searchTerm.length() >= minLength
				&& pattern.matcher(searchTerm).matches();
	}
}
