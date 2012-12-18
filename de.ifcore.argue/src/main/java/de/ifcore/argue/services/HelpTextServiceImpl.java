package de.ifcore.argue.services;

import java.util.Collections;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;

public class HelpTextServiceImpl implements HelpTextService
{
	private final Set<String> supportedLanguages;
	private final String defaultLanguage;

	public HelpTextServiceImpl(Set<String> supportedLanguages, String defaultLanguage)
	{
		if (supportedLanguages == null || defaultLanguage == null)
			throw new IllegalArgumentException();
		this.supportedLanguages = Collections.unmodifiableSet(supportedLanguages);
		this.defaultLanguage = defaultLanguage;
	}

	@Override
	public String getHelpLocale()
	{
		String language = LocaleContextHolder.getLocale().getLanguage();
		return supportedLanguages.contains(language) ? language : defaultLanguage;
	}
}
