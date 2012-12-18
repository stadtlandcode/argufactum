package de.ifcore.argue.domain.models;

import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.RegisteredUser;

public class UserRequestImplTest
{
	@Test
	public void testUserRequestImpl()
	{
		HttpServletRequest request = mock(HttpServletRequest.class);
		String ip = "127.0.0.1";
		String ip2 = "8.8.8.8";
		RegisteredUser user = mockUser();
		when(request.getRemoteAddr()).thenReturn(ip);
		when(request.getHeader("X-Real-IP")).thenReturn(ip2);

		MockUserSession filledUserSession = new MockUserSession(user);
		UserRequest userRequest = new UserRequestImpl(request, filledUserSession, false);
		assertEquals(new Author(user, user.getUsername(), ip), userRequest.getAuthor());

		MockUserSession emptyUserSession = new MockUserSession(null);
		userRequest = new UserRequestImpl(request, emptyUserSession, false);
		assertEquals(new Author(null, ip, ip), userRequest.getAuthor());

		userRequest = new UserRequestImpl(request, emptyUserSession, true);
		assertEquals(new Author(null, ip2, ip2), userRequest.getAuthor());
	}
}
