package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.dao.hibernate.HibernateSearchDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class SearchDaoTest extends IntegrationTest
{
	@Test
	public void testSearchTopics()
	{
		SearchDao searchDao = new HibernateSearchDao(sessionFactory);
		Topic topic1 = new Topic("Waldsterben", "", DiscussionType.PRO_CONTRA, mockAuthor(mockUser()),
				TopicVisibility.PUBLIC, null);
		Topic topic2 = new Topic("Fußweg an B599 in Waldürn", "", DiscussionType.PRO_CONTRA, mockAuthor(mockUser()),
				TopicVisibility.PUBLIC, null);
		Topic topic3 = new Topic("Walfang", "", DiscussionType.PRO_CONTRA, mockAuthor(mockUser()),
				TopicVisibility.PUBLIC, null);
		Topic topic4 = new Topic("Waldsterben", "", DiscussionType.PRO_CONTRA, mockAuthor(mockUser()),
				TopicVisibility.PRIVATE, null);
		persistTopic(topic1);
		persistTopic(topic2);
		persistTopic(topic3);
		persistTopic(topic4);
		flushAndClear();

		List<Topic> topics = searchDao.searchTopics("WALD");
		assertEquals(2, topics.size());
	}
}
