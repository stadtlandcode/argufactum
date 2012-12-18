package de.ifcore.argue.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.Printer;
import org.springframework.format.number.NumberFormatter;

public class FormatUtils
{
	private static final DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
	private static final Printer<Number> numberFormatter = new NumberFormatter();

	/**
	 * @param value
	 * @param pattern
	 *            nullable. default pattern is ISO8601
	 * @return
	 */
	public static String formatDateTime(DateTime value, String pattern)
	{
		if (value == null)
			throw new IllegalArgumentException();

		DateTimeFormatter formatter = isoDateTimeFormatter;
		if (pattern != null)
			formatter = DateTimeFormat.forPattern(pattern);
		return formatter.print(value);
	}

	public static String formatNumber(Integer value)
	{
		return numberFormatter.print(value, LocaleContextHolder.getLocale());
	}
}
