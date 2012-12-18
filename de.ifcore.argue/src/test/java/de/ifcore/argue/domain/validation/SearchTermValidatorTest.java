package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class SearchTermValidatorTest
{
	@Test
	public void testIsValid()
	{
		SearchTermValidator validator = new SearchTermValidator();
		ReflectionTestUtils.setField(validator, "minLength", 3);

		assertTrue(validator.isValid("Wald", null));
		assertTrue(validator.isValid("Wälder", null));
		assertTrue(validator.isValid("Straßengebäude", null));
		assertTrue(validator.isValid("Hoch- oder Tief?!", null));
		assertTrue(validator.isValid("Stuttgart21", null));
		assertFalse(validator.isValid("a", null));
		assertFalse(validator.isValid("Stutt%", null));
		assertFalse(validator.isValid("      ", null));
	}
}
