package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ComparisonTopicDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		Topic topic = new Topic("testTopic", null, DiscussionType.COMPARISON, mockAuthor(persistUser(mockUser())),
				TopicVisibility.PRIVATE, null);
		ComparisonTopic comparisonTopic = new ComparisonTopic(topic);
		comparisonTopicDao.save(comparisonTopic);
		flushAndClear();

		ComparisonTopic persistedComparisonTopic = comparisonTopicDao.get(comparisonTopic.getId());
		assertEquals(comparisonTopic, persistedComparisonTopic);
		assertEquals(topic, persistedComparisonTopic.getTopic());
	}
}
