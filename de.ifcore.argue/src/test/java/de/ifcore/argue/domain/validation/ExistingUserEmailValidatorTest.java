package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.utils.EntityTestUtils;

public class ExistingUserEmailValidatorTest
{
	@Test
	public void testIsValid()
	{
		UserDao userDao = mock(UserDao.class);
		when(userDao.findByEmail("info@argufactum.de")).thenReturn(EntityTestUtils.mockUser());
		ExistingUserEmailValidator validator = new ExistingUserEmailValidator(userDao);

		assertTrue(validator.isValid(null, null));
		assertTrue(validator.isValid("info@argufactum.de", null));
		assertFalse(validator.isValid("info2@argufactum.de", null));
	}
}
