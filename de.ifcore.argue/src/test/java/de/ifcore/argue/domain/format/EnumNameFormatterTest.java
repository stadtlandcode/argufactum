package de.ifcore.argue.domain.format;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.enumerations.TopicThesis;

public class EnumNameFormatterTest
{
	private EnumNameFormatter formatter;
	private final Locale locale = Locale.GERMAN;

	@Before
	public void setup()
	{
		MessageSource messageSource = mock(MessageSource.class);
		Mockito.when(messageSource.getMessage("TopicThesis.PRO", null, locale)).thenReturn("Pro");
		Mockito.when(messageSource.getMessage("TopicThesis.CONTRA", null, locale)).thenReturn("Contra");
		formatter = new EnumNameFormatter(messageSource, TopicThesis.class);
	}

	@Test
	public void testPrint()
	{
		assertEquals("Pro", formatter.print(TopicThesis.PRO, locale));
		assertEquals("Contra", formatter.print(TopicThesis.CONTRA, locale));
	}

	@Test
	public void testParse() throws ParseException
	{
		assertEquals(TopicThesis.PRO, formatter.parse("Pro", locale));
		assertEquals(TopicThesis.PRO, formatter.parse("PRO", locale));
	}

}
