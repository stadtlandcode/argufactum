package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopicAccessRight;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.SelectedTopic;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicTerm;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class TopicDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		RegisteredUser user = persistUser(mockUser());
		Topic topic = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, mockAuthor(user),
				TopicVisibility.PRIVATE, null);
		topicDao.save(topic);
		flushAndClear();

		Topic persistedTopic = topicDao.get(topic.getId());
		assertEquals(topic, persistedTopic);
		assertEquals(topic.getText(), persistedTopic.getText());
		assertEquals(1, topic.getAccessRights().size());
	}

	@Test
	public void testGetLatestTopics() throws InterruptedException
	{
		RegisteredUser user = persistUser(mockUser());
		Author author = mockAuthor(user);
		Topic topic1 = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, author, TopicVisibility.PUBLIC, null);
		Thread.sleep(100);
		Topic topic2 = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, author, TopicVisibility.PUBLIC, null);
		Thread.sleep(100);
		Topic topic3 = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, author, TopicVisibility.PUBLIC, null);
		Thread.sleep(100);

		topicDao.save(topic1);
		topicDao.save(topic2);
		topicDao.save(topic3);
		flushAndClear();

		List<Topic> latestTopics = topicDao.getLatestPublicTopics(2);
		assertEquals(2, latestTopics.size());
		assertEquals(topic3, latestTopics.get(0));
		assertEquals(topic2, latestTopics.get(1));
	}

	@Test
	public void testSaveAccessRight()
	{
		Topic topic = persistTopic(mockTopic());
		RegisteredUser user = persistUser(mockUser());
		TopicAccessRight accessRight = new TopicAccessRight(topic, user, EntityPermission.WRITE);
		topicDao.saveAccessRight(accessRight);
		flushAndClear();

		assertNotNull(accessRight.getId());
		TopicAccessRight persistedAccessRight = topicDao.getAccessRight(accessRight.getId());
		assertNotNull(persistedAccessRight);
		assertEquals(topic, accessRight.getTopic());
		assertEquals(user, accessRight.getRegisteredUser());
		assertEquals(accessRight.getPermission(), persistedAccessRight.getPermission());
	}

	@Test
	public void testDeleteAccessRight()
	{
		TopicAccessRight accessRight = persistTopicAccessRight(mockTopicAccessRight(mockTopic()));
		flushAndClear();

		topicDao.deleteAccessRight(accessRight);
		flushAndClear();
		assertNull(topicDao.getAccessRight(accessRight.getId()));
	}

	@Test
	public void testGetTerms()
	{
		Topic topic = persistTopic(mockTopic());
		flushAndClear();
		topic = topicDao.get(topic.getId());
		topic.edit("12345", null, null, mockAuthor());
		topicDao.saveTerm(topic.getTerm());
		topicDao.save(topic);
		flushAndClear();

		List<TopicTerm> theses = topicDao.getTerms(topic.getId());
		assertEquals(2, theses.size());
	}

	@Test
	public void testVote()
	{
		Topic topic = persistTopic(mockTopic());
		RegisteredUser user = persistUser(mockUser());
		flushAndClear();

		topic = topicDao.get(topic.getId());
		topicDao.saveVote(new TopicVote(true, topic, mockAuthor(user)));
		flushAndClear();
		topic = topicDao.get(topic.getId());
		assertEquals(1, topic.getVotes().size());

		TopicVote topicVote = topic.getVotes().iterator().next();
		topicVote.setVote(false);
		topicDao.updateVote(topicVote);
		flushAndClear();
		topic = topicDao.get(topic.getId());
		topicVote = topic.getVotes().iterator().next();
		assertEquals(1, topic.getVotes().size());
		assertFalse(topicVote.isUpVote());

		topicDao.deleteVote(topicVote);
		flushAndClear();
		topic = topicDao.get(topic.getId());
		assertEquals(0, topic.getVotes().size());
	}

	@Test
	public void testGetSelectedTopics()
	{
		selectedTopicDao.save(new SelectedTopic(Byte.valueOf((byte)1), persistTopic(mockTopic())));
		selectedTopicDao.save(new SelectedTopic(Byte.valueOf((byte)2), persistTopic(mockTopic())));
		flushAndClear();

		assertEquals(2, topicDao.getSelectedTopics().size());
	}

	@Test
	public void testGetAssociatedTopics()
	{
		RegisteredUser user1 = persistUser(mockUser());
		RegisteredUser user2 = persistUser(mockUser());
		Topic topicOfUser1 = persistTopic(mockTopic(mockAuthor(user1)));
		Topic topicOfUser2 = persistTopic(mockTopic(mockAuthor(user2)));
		persistTopic(mockTopic());
		flushAndClear();

		assertEquals(1, topicDao.getAssociatedTopics(user1.getId()).size());

		TopicAccessRight topicAccessRight = new TopicAccessRight(topicOfUser2, user1, EntityPermission.READ);
		topicDao.saveAccessRight(topicAccessRight);
		topicDao.update(topicOfUser2);
		flushAndClear();

		List<Topic> associatedTopics = topicDao.getAssociatedTopics(user1.getId());
		assertEquals(2, associatedTopics.size());
		assertTrue(associatedTopics.contains(topicOfUser1));
		assertTrue(associatedTopics.contains(topicOfUser2));
	}
}
