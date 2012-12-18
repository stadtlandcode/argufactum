package de.ifcore.argue.domain.format;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

public class EnumNameFormatter implements Formatter<Enum<?>>
{
	private final MessageSource messageSource;
	private final Class<? extends Enum<?>> enumClass;

	public EnumNameFormatter(MessageSource messageSource, Class<? extends Enum<?>> enumClass)
	{
		this.messageSource = messageSource;
		this.enumClass = enumClass;
	}

	@Override
	public String print(Enum<?> enumConstant, Locale locale)
	{
		return messageSource.getMessage(enumClass.getSimpleName() + "." + enumConstant.name(), null, locale);
	}

	@Override
	public Enum<?> parse(String text, Locale locale) throws ParseException
	{
		for (Enum<?> enumConstant : enumClass.getEnumConstants())
		{
			if (text.equals(enumConstant.name()) || text.equals(print(enumConstant, locale)))
				return enumConstant;
		}
		return null;
	}
}
