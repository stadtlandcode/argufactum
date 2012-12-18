package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockFact;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.Relevance;

public class FactTest
{
	@Test
	public void testCreateFact()
	{
		Author author = mockAuthor();
		Argument argument = mockArgument();
		String text = "12345";
		FactType factType = FactType.CONFIRMATIVE;
		Fact fact = new Fact(argument, factType, text, author);

		assertEquals(text, fact.getEvidence().getText());
		assertEquals(author, fact.getAuthor());
		assertEquals(argument, fact.getArgument());
		assertNotNull(fact.getReferences());
		assertTrue(fact.getReferences().isEmpty());
		assertNotNull(fact.getCreatedAt());
		assertNull(fact.getId());
	}

	@Test
	public void testCompareTo()
	{
		Argument argument = mockArgument();
		Author author = mockAuthor(mockUser());
		Date creationDate = new Date();

		Fact fact = new Fact(argument, FactType.CONFIRMATIVE, "12345", author);
		ReflectionTestUtils.setField(fact, "createdAt", creationDate);
		Fact fact2 = new Fact(argument, FactType.CONFIRMATIVE, "12345", author);
		ReflectionTestUtils.setField(fact2, "createdAt", creationDate);

		Fact fact3 = new Fact(argument, FactType.CONFIRMATIVE, "12345", mockAuthor());
		ReflectionTestUtils.setField(fact3, "createdAt", creationDate);
		ReflectionTestUtils.setField(fact3, "id", Long.valueOf(1));

		assertEquals(0, fact.compareTo(fact));
		assertEquals(0, fact.compareTo(fact2));
		assertTrue(fact.compareTo(fact3) < 0);

		new FactRelevanceVote(fact3, Relevance.LOW, author);
		assertTrue(fact.compareTo(fact3) > 0);
	}

	@Test
	public void testCopyTo()
	{
		Fact source = mockFact();
		source.editEvidence("123456", mockAuthor());
		Reference.instanceOf("www.wikipedia.org", source, mockAuthor());
		Reference.instanceOf("www.heise.de", source, mockAuthor());

		Argument destArgument = mockArgument();
		Fact dest = source.copyTo(destArgument, 0);
		assertEquals(destArgument, dest.getArgument());
		assertEquals(source.getText(), dest.getText());
		assertNotSame(source.getAuthor(), dest.getAuthor());
		assertNotSame(source.getEvidence(), dest.getEvidence());
		assertFalse(source.getEvidence().isFirst());
		assertTrue(dest.getEvidence().isFirst());
		assertEquals(2, dest.getReferences().size());
	}
}
