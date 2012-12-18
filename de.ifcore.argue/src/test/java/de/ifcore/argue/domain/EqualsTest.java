package de.ifcore.argue.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import de.ifcore.argue.domain.entities.Author;

public class EqualsTest
{
	@Test
	public void testEntities()
	{
		EqualsVerifier.forClass(Author.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
