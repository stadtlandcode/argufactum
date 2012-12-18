package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.utils.EntityTestUtils;

public class UniqueUsernameValidatorTest
{
	@Test
	public void testIsValid()
	{
		UserDao userDao = mock(UserDao.class);
		when(userDao.findByUsername("alwine")).thenReturn(EntityTestUtils.mockUser());
		UniqueUsernameValidator validator = new UniqueUsernameValidator(userDao);
		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid("alwine2", null));
		assertFalse(validator.isValid("alwine", null));
	}
}
