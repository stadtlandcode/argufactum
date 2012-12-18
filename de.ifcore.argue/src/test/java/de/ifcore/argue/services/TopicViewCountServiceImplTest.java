package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;

public class TopicViewCountServiceImplTest
{
	@Test
	public void testCount()
	{
		TopicViewCountServiceImpl service = new TopicViewCountServiceImpl(null, 2, 100);
		Topic topic1 = getTopicWithId(1);
		Author author1 = new Author(mockUser(), "127.0.0.1");
		Topic topic2 = getTopicWithId(2);
		Author author2 = new Author(mockUser(), "127.0.0.2");
		Topic topic3 = getTopicWithId(3);
		Author author3 = new Author(mockUser(), "127.0.0.3");

		service.count(mockTopic(), mockAuthor());
		assertTrue(service.getTopics().isEmpty());

		service.count(topic1, author1);
		assertEquals(1, service.getTopics().size());
		service.count(topic1, author1);
		assertEquals(1, service.getTopics().size());

		service.count(topic1, author2);
		service.count(topic1, author3);
		service.count(topic2, author3);
		service.count(topic3, author3);
		assertEquals(2, service.getTopics().size());
		assertTrue(service.getTopics().containsKey(Long.valueOf(2)));
		assertTrue(service.getTopics().containsKey(Long.valueOf(3)));
	}

	private Topic getTopicWithId(int id)
	{
		Topic topic = mockTopic();
		ReflectionTestUtils.setField(topic, "id", Long.valueOf(id));
		return topic;
	}
}
