package de.ifcore.argue.domain.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.form.RegistrationPasswordInput;
import de.ifcore.argue.domain.models.MockUserSession;
import de.ifcore.argue.domain.models.UserSession;

public class RegistrationPasswordVerificationValidatorTest
{
	@Test
	public void testIsValid()
	{
		UserSession userSession = new MockUserSession();
		RegistrationPasswordVerificationValidator validator = new RegistrationPasswordVerificationValidator(userSession);
		ReflectionTestUtils.setField(validator, "minLength", 6);
		ReflectionTestUtils.setField(validator, "maxLength", 128);
		RegistrationPasswordInput passwordInput = new RegistrationPasswordInput(UUID.randomUUID().toString());

		assertFalse(validator.isValid(passwordInput, null));
		passwordInput.setPassword("123");
		assertFalse(validator.isValid(passwordInput, null));
		passwordInput.setPassword("123456");
		assertTrue(validator.isValid(passwordInput, null));

		userSession.storePassword(passwordInput.getUuid(), "123456");
		assertTrue(validator.isValid(passwordInput, null));
		passwordInput.setPassword(null);
		assertTrue(validator.isValid(passwordInput, null));
		passwordInput.setPassword("123");
		assertFalse(validator.isValid(passwordInput, null));
	}
}
