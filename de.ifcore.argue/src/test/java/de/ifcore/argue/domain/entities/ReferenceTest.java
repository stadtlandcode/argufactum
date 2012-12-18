package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockFact;
import static de.ifcore.argue.utils.EntityTestUtils.mockReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ReferenceTest
{
	@Test
	public void testCreateReference()
	{
		Fact fact = mockFact();
		String url = "www.ARGUFACTUM.de";
		Author author = mockAuthor();
		Reference reference = Reference.instanceOf(url, fact, author);

		assertEquals(fact, reference.getFact());
		assertEquals("argufactum.de", reference.getText());
		assertEquals("http://www.ARGUFACTUM.de", reference.getUrl());
		assertEquals(author, reference.getAuthor());
		assertNull(reference.getId());
		assertNotNull(reference.getCreatedAt());
		assertFalse(fact.getReferences().isEmpty());
		assertEquals(reference, fact.getReferences().iterator().next());

		Reference reference2 = Reference.instanceOf("test1", fact, author);
		assertEquals("test1", reference2.getText());
		assertNull(reference2.getUrl());
	}

	@Test
	public void testCompareTo()
	{
		Fact fact = mockFact();
		Author author = mockAuthor();
		Date creationDate = new Date();

		Reference reference = Reference.instanceOf("12345", fact, author);
		ReflectionTestUtils.setField(reference, "createdAt", creationDate);
		Reference reference2 = Reference.instanceOf("12345", fact, author);
		ReflectionTestUtils.setField(reference2, "createdAt", creationDate);

		Reference reference3 = Reference.instanceOf("abcdef", fact, author);
		ReflectionTestUtils.setField(reference3, "createdAt", creationDate);
		ReflectionTestUtils.setField(reference3, "id", Long.valueOf(1));

		assertEquals(0, reference.compareTo(reference));
		assertEquals(0, reference.compareTo(reference2));
		assertTrue(reference.compareTo(reference3) != 0);
	}

	@Test
	public void testCopyTo()
	{
		Reference source = mockReference();

		Fact destFact = mockFact();
		Reference dest = source.copyTo(destFact, 0);
		assertEquals(destFact, dest.getFact());
		assertEquals(source.getText(), dest.getText());
		assertEquals(source.getUrl(), dest.getUrl());
		assertNotSame(source.getAuthor(), dest.getAuthor());
	}
}
