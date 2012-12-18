package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.EmailVerificationRequest;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.social.MockOAuthForeignAuthentication;
import de.ifcore.argue.social.MockOpenIdForeignAuthentication;
import de.ifcore.argue.utils.EntityTestUtils;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest extends IntegrationTest
{
	@Test
	public void testSaveUser()
	{
		RegisteredUser passwordUser = RegisteredUser.register("maxmu", "max@mustermann.de", "12345678", null);
		userDao.save(passwordUser);
		RegisteredUser openIdUser = RegisteredUser.register("maxmu2", "max2@mustermann.de",
				new MockOpenIdForeignAuthentication("12345678"), null);
		userDao.save(openIdUser);
		RegisteredUser oAuthUser = RegisteredUser.register("maxmu3", "max3@mustermann.de",
				new MockOAuthForeignAuthentication("google", "12345678"), null);
		userDao.save(oAuthUser);
		flushAndClear();

		RegisteredUser persistedPasswordUser = userDao.get(passwordUser.getId());
		assertEquals(passwordUser.getUsername(), persistedPasswordUser.getUsername());
		assertEquals(passwordUser.getCreatedAt(), persistedPasswordUser.getCreatedAt());
		assertEquals(passwordUser.getEmail(), persistedPasswordUser.getEmail());
		assertEquals(passwordUser.getPassword(), persistedPasswordUser.getPassword());
		assertTrue(persistedPasswordUser.getOpenIdIdentifiers().isEmpty());

		RegisteredUser persistedOpenIdUser = userDao.get(openIdUser.getId());
		assertEquals(1, persistedOpenIdUser.getOpenIdIdentifiers().size());
		assertTrue(persistedOpenIdUser.getOpenIdIdentifiers().contains(
				openIdUser.getOpenIdIdentifiers().iterator().next()));

		RegisteredUser persistedOAuthUser = userDao.get(oAuthUser.getId());
		assertEquals(1, persistedOAuthUser.getOAuthConnections().size());
		assertTrue(persistedOAuthUser.getOAuthConnections().contains(oAuthUser.getOAuthConnections().iterator().next()));
	}

	@Test
	public void testFindByUsername()
	{
		RegisteredUser user = persistUser(mockUser());
		flushAndClear();
		assertEquals(user, userDao.findByUsername(user.getUsername()));
		assertEquals(user, userDao.findByUsername(user.getUsername().toLowerCase()));
		assertEquals(user, userDao.findByUsername(user.getUsername().toUpperCase()));
	}

	@Test
	public void testFindByEmail()
	{
		RegisteredUser user = persistUser(mockUser());
		flushAndClear();
		assertEquals(user, userDao.findByEmail(user.getEmail()));
		assertEquals(user, userDao.findByEmail(user.getEmail().toUpperCase()));
	}

	@Test
	public void testFindByOpenIdIdentifier()
	{
		String openIdIdentifier = "123456";
		RegisteredUser user = persistUser(RegisteredUser.register("alf", "alf@space.de",
				new MockOpenIdForeignAuthentication(openIdIdentifier), null));
		flushAndClear();
		assertEquals(user, userDao.findByOpenIdIdentifier(openIdIdentifier));
	}

	@Test
	public void testEmailVerificationRequest()
	{
		RegisteredUser user = EntityTestUtils.mockUser();
		userDao.save(user);
		EmailVerificationRequest request = new EmailVerificationRequest(user);
		userDao.saveEmailVerificationRequest(request);
		flushAndClear();

		EmailVerificationRequest persistedRequest = userDao.getEmailVerificationRequest(request.getVerificationKey());
		assertEquals(request, persistedRequest);

		persistedRequest.verify();
		userDao.updateEmailVerificationRequest(persistedRequest);
		flushAndClear();
		EmailVerificationRequest updatedRequest = userDao.getEmailVerificationRequest(request.getVerificationKey());
		assertEquals(persistedRequest, updatedRequest);
	}

	@Test
	public void testFindUsernamesStartingWith()
	{
		String usernamePrefix = "Alf";
		userDao.save(RegisteredUser.register("ALF", "alf@space.de", "123456", null));
		userDao.save(RegisteredUser.register("alf1", "alf1@space.de", "123456", null));
		flushAndClear();

		Set<String> usernames = userDao.findUsernamesStartingWith(usernamePrefix);
		assertEquals(2, usernames.size());
		for (String username : usernames)
			assertTrue(username.toLowerCase().startsWith(usernamePrefix.toLowerCase()));

		Set<String> usernames2 = userDao.findUsernamesStartingWith("ralf");
		assertNotNull(usernames2);
		assertTrue(usernames2.isEmpty());
	}
}
