package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class UsernameValidatorTest
{
	@Test
	public void testIsValid()
	{
		UsernameValidator validator = new UsernameValidator();
		ReflectionTestUtils.setField(validator, "minLength", 3);
		ReflectionTestUtils.setField(validator, "maxLength", 64);

		assertTrue(validator.isValid("alf", null));
		assertTrue(validator.isValid("Müller", null));
		assertTrue(validator.isValid("Alfred Müller", null));
		assertTrue(validator.isValid("user1111171545121", null));
		assertTrue(validator.isValid("user.1.2", null));
		assertTrue(validator.isValid("user-1.2", null));
		assertFalse(validator.isValid("user_1.2", null));
		assertFalse(validator.isValid("test ", null));
		assertFalse(validator.isValid(" test", null));
		assertFalse(validator.isValid("user1234-", null));
		assertFalse(validator.isValid("<script", null));
		assertFalse(validator.isValid("A", null));
		assertFalse(validator.isValid("electronic@mail.de", null));
		assertFalse(validator.isValid("abc\"sd", null));
		assertFalse(validator
				.isValid(
						"seeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeehrlangerusername",
						null));
	}
}
