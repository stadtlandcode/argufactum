package de.ifcore.argue.utils;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class FormatUtilsTest
{
	@Test
	public void testFormatDateTime()
	{
		DateTime dateTime = new DateTime(2011, 10, 26, 16, 41, 23, DateTimeZone.UTC);
		assertEquals("2011-10-26T16:41:23Z", FormatUtils.formatDateTime(dateTime, null));
		assertEquals("26. Oktober 2011 16:41", FormatUtils.formatDateTime(dateTime, "dd. MMMM YYYY HH:mm"));
	}
}
