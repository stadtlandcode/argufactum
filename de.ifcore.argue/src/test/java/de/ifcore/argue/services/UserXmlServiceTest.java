package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.RegisteredUser;

public class UserXmlServiceTest
{
	@Test
	public void testParseMainNodes() throws Exception
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new File("src/test/resources/initialData/users.xml"));
		NodeList userNodes = document.getElementsByTagName("user");
		RegisteredUser mockUser = RegisteredUser.register("abc", "def@def.de", "123", null);
		UserDao userDao = mock(UserDao.class);
		when(userDao.findByUsername("horst")).thenReturn(mockUser);
		when(userDao.findByEmail("manfred@argufactum.de")).thenReturn(mockUser);
		SecurityService securityService = mock(SecurityService.class);
		when(securityService.encodePassword(anyString())).thenReturn("abc");
		UserXmlService service = new UserXmlService(userDao, securityService, null, null);

		Set<RegisteredUser> users = service.parseMainNodes(userNodes);
		assertEquals(2, users.size());

		RegisteredUser user1 = getUser("felix", users);
		assertNotNull(user1);
		assertEquals("felix", user1.getUsername());
		assertEquals("felix@argufactum.de", user1.getEmail());
		assertTrue(user1.isAdmin());
		assertTrue(user1.isEmailVerified());
		assertNotNull(user1.getPassword());

		RegisteredUser user2 = getUser("edel", users);
		assertFalse(user2.isAdmin());
	}

	private RegisteredUser getUser(String username, Set<RegisteredUser> users)
	{
		for (RegisteredUser user : users)
		{
			if (username.equals(user.getUsername()))
				return user;
		}
		return null;
	}
}
