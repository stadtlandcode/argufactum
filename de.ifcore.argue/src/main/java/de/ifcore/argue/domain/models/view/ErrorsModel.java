package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ErrorsModel implements ViewModel
{
	private static final long serialVersionUID = 2446846253322324931L;

	private final int errors;
	private final Set<String> globalErrors;
	private final Map<String, String> fieldErrors;

	public ErrorsModel(Errors errors, MessageSource messageSource)
	{
		this.errors = errors.getErrorCount();

		Locale locale = LocaleContextHolder.getLocale();
		Set<String> globalErrors = new LinkedHashSet<>();
		for (ObjectError error : errors.getGlobalErrors())
		{
			globalErrors.add(messageSource.getMessage(error, locale));
		}
		this.globalErrors = Collections.unmodifiableSet(globalErrors);

		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError error : errors.getFieldErrors())
		{
			fieldErrors.put(error.getField(), messageSource.getMessage(error, locale));
		}
		this.fieldErrors = Collections.unmodifiableMap(fieldErrors);
	}

	public int getErrors()
	{
		return errors;
	}

	public Set<String> getGlobalErrors()
	{
		return globalErrors;
	}

	public Map<String, String> getFieldErrors()
	{
		return fieldErrors;
	}
}
