package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicEntity;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class TopicAccessRightServiceImplTest
{
	@Test
	public void testHasAccess()
	{
		RegisteredUser user1 = mockUser();
		ReflectionTestUtils.setField(user1, "id", Long.valueOf(1l));
		RegisteredUser user2 = mockUser();
		ReflectionTestUtils.setField(user2, "id", Long.valueOf(2l));
		Topic topic = mockTopic(mockAuthor(user2));
		ReflectionTestUtils.setField(topic, "id", Long.valueOf(1l));

		TopicDao topicDao = mock(TopicDao.class);
		when(topicDao.get(topic.getId())).thenReturn(topic);
		TopicAccessRightServiceImpl accessRightService = new TopicAccessRightServiceImpl(topicDao, null);

		assertTrue(accessRightService.hasAccess((TopicEntity)null, null, EntityPermission.READ));
		topic.setVisibility(TopicVisibility.PUBLIC);
		assertTrue(accessRightService.hasAccess(topic, null, EntityPermission.READ));
		assertTrue(accessRightService.hasAccess(topic, user1, EntityPermission.READ));
		topic.setVisibility(TopicVisibility.PRIVATE);
		assertFalse(accessRightService.hasAccess(topic, null, EntityPermission.READ));
		assertFalse(accessRightService.hasAccess(topic, user1, EntityPermission.READ));

		new TopicAccessRight(topic, user1, EntityPermission.WRITE);
		assertTrue(accessRightService.hasAccess(topic, user1, EntityPermission.READ));
		assertTrue(accessRightService.hasAccess(topic, user1, EntityPermission.WRITE));
		assertTrue(accessRightService.hasAccess(topic, user1, EntityPermission.WRITE));
		assertTrue(accessRightService.hasAccess(topic, user2, EntityPermission.WRITE));
	}
}
