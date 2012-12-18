package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.services.UserService;

public class UniqueUserEmailValidatorTest
{
	@Test
	public void testIsValid()
	{
		UserService userService = mock(UserService.class);
		when(userService.isEmailTaken("info@argufactum.de")).thenReturn(Boolean.TRUE);
		UniqueUserEmailValidator validator = new UniqueUserEmailValidator(userService);
		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid("info2@argufactum.de", null));
		assertFalse(validator.isValid("info@argufactum.de", null));
	}
}
