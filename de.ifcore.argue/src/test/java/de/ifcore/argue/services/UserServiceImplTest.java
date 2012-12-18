package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.ifcore.argue.dao.UserDao;

public class UserServiceImplTest
{
	@Test
	public void testGuessUsernameByEmail()
	{
		UserDao userDao = mock(UserDao.class);
		Set<String> takenUsernames = new HashSet<>(Arrays.asList("max", "max mustermann", "max1", "max3"));
		when(userDao.findUsernamesStartingWith("Max")).thenReturn(takenUsernames);
		when(userDao.findUsernamesStartingWith(anyString())).thenReturn(Collections.<String> emptySet());

		UsernameService usernameService = new UsernameServiceImpl(userDao);
		assertEquals("Max2", usernameService.guessByEmail("Max2@mustermann.de"));
		assertEquals("lisa", usernameService.guessByEmail("lisa@musterfrau.de"));
		assertEquals("astrange.name", usernameService.guessByEmail("a_strange.name@strange.de"));
		assertEquals("astrangename", usernameService.guessByEmail(".a_strange.name@strange.de"));
		assertEquals("user-a", usernameService.guessByEmail("a@strange.de"));
	}
}
