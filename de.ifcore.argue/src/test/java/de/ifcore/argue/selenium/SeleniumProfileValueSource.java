package de.ifcore.argue.selenium;

import org.springframework.test.annotation.ProfileValueSource;

public class SeleniumProfileValueSource implements ProfileValueSource
{
	private static final String PROPERTY = "runSeleniumTests";

	@Override
	public String get(String key)
	{
		if (PROPERTY.equals(key))
			return System.getProperty(key, "true");
		else
			return System.getProperty(key);
	}
}
