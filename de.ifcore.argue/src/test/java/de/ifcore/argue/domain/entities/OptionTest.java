package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockOption;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class OptionTest
{
	@Test
	public void testCompareTo()
	{
		ComparisonTopic comparisonTopic = mockComparisonTopic();
		Author author = mockAuthor();
		Date creationDate = new Date();
		Date creationDate2 = new Date(1000);

		Option option1 = new Option(comparisonTopic, "123", author);
		Option option2 = new Option(comparisonTopic, "123", author);
		Option option3 = new Option(comparisonTopic, "123", mockAuthor());
		Option option4 = new Option(comparisonTopic, "123", author);
		Option option5 = new Option(mockComparisonTopic(), "123", author);
		ReflectionTestUtils.setField(option1, "createdAt", creationDate);
		ReflectionTestUtils.setField(option2, "createdAt", creationDate);
		ReflectionTestUtils.setField(option3, "createdAt", creationDate);
		ReflectionTestUtils.setField(option3, "id", Long.valueOf(1));
		ReflectionTestUtils.setField(option4, "createdAt", creationDate2);
		ReflectionTestUtils.setField(option5, "createdAt", creationDate);

		assertEquals(0, option1.compareTo(option1));
		assertEquals(0, option1.compareTo(option2));
		assertTrue(option1.compareTo(option3) < 0);
		assertTrue(option1.compareTo(option4) > 0);
		assertEquals(0, option1.compareTo(option5));
	}

	@Test
	public void testEdit()
	{
		String label = "123456";
		Author author = mockAuthor();
		Option option = mockOption();

		boolean executed = option.edit(label, author);
		assertEquals(label, option.getLabel());
		assertTrue(executed);

		assertFalse(option.edit(label, author));
		try
		{
			option.edit(null, author);
			fail();
		}
		catch (IllegalArgumentException e)
		{
		}
	}
}
