package de.ifcore.argue.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateUsersConnectionRepositoryTest extends IntegrationTest
{
	@Test
	public void testFindUserIdsWithConnection()
	{
		RegisteredUser user = persistUser(RegisteredUser.register("eva", "eva@maria.de",
				new MockOAuthForeignAuthentication("google", "111"), null));
		flushAndClear();
		UsersConnectionRepository connectionRepository = new HibernateUsersConnectionRepository(sessionFactory);
		Connection<?> connection = mock(Connection.class);
		when(connection.getKey()).thenReturn(new ConnectionKey("google", "111"));

		List<String> userIdsWithConnection = connectionRepository.findUserIdsWithConnection(connection);
		assertNotNull(userIdsWithConnection);
		assertEquals(1, userIdsWithConnection.size());
		assertEquals(user.getId().toString(), userIdsWithConnection.iterator().next());

		when(connection.getKey()).thenReturn(new ConnectionKey("google", "222"));
		userIdsWithConnection = connectionRepository.findUserIdsWithConnection(connection);
		assertNotNull(userIdsWithConnection);
		assertTrue(userIdsWithConnection.isEmpty());
	}

	@Test
	public void testFindUserIdsConnectedTo()
	{
		RegisteredUser user1 = persistUser(RegisteredUser.register("eva1", "eva1@maria.de",
				new MockOAuthForeignAuthentication("google", "111"), null));
		persistUser(RegisteredUser.register("eva2", "eva2@maria.de",
				new MockOAuthForeignAuthentication("google", "222"), null));
		RegisteredUser user3 = persistUser(RegisteredUser.register("eva3", "eva3@maria.de",
				new MockOAuthForeignAuthentication("google", "333"), null));
		flushAndClear();

		UsersConnectionRepository connectionRepository = new HibernateUsersConnectionRepository(sessionFactory);
		Set<String> users = connectionRepository.findUserIdsConnectedTo("google",
				new HashSet<>(Arrays.asList("111", "333")));
		assertNotNull(users);
		assertEquals(2, users.size());
		assertTrue(users.contains(user1.getId().toString()));
		assertTrue(users.contains(user3.getId().toString()));
	}
}
