package de.ifcore.argue.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.junit.Test;

public class EmbeddableAuthorAttributionTest
{
	@Test
	public void testAddToAttributionList()
	{
		EmbeddableAuthorAttribution authorAttribution = new EmbeddableAuthorAttribution();

		authorAttribution.addToAuthorList("hans", "hans", "hans");
		assertEquals(0, authorAttribution.getAuthorList().size());

		authorAttribution.addToAuthorList("gunther", "hans", "hans");
		assertEquals(0, authorAttribution.getAuthorList().size());

		authorAttribution.addToAuthorList("gunther", "gunther", "hans");
		assertEquals(0, authorAttribution.getAuthorList().size());

		authorAttribution.addToAuthorList("hans", "gunther", "hans");
		assertEquals(2, authorAttribution.getAuthorList().size());

		authorAttribution.addToAuthorList("petra", "hans", "hans");
		assertEquals(3, authorAttribution.getAuthorList().size());

		authorAttribution.addToAuthorList("petra", "hans", "hans");
		assertEquals(3, authorAttribution.getAuthorList().size());
		assertEquals("hans;gunther;petra", authorAttribution.getAuthors());
	}

	@Test
	public void testOfferAttributionList()
	{
		EmbeddableAuthorAttribution authorAttribution = new EmbeddableAuthorAttribution();
		assertFalse(authorAttribution.offerAttributionList(new LinkedHashSet<>(Arrays.asList("abc")),
				Arrays.asList("abc")));
		assertFalse(authorAttribution.offerAttributionList(new LinkedHashSet<>(Arrays.asList("abc")),
				Arrays.asList("abc", "abc")));
		assertFalse(authorAttribution.offerAttributionList(new LinkedHashSet<>(Arrays.asList("abc", "def")),
				Arrays.asList("abc", "def")));
		assertTrue(authorAttribution.offerAttributionList(new LinkedHashSet<>(Arrays.asList("abc", "def")),
				Arrays.asList("abc", "abc")));
	}
}
