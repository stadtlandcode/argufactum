package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class TopicTest
{
	@Test
	public void testCreateTopic()
	{
		Author author = mockAuthor(mockUser());
		DiscussionType discussionType = DiscussionType.PRO_CONTRA;
		String term = "12345";
		Topic topic = new Topic(term, null, discussionType, author, TopicVisibility.PRIVATE, null);

		assertEquals(author, topic.getAuthor());
		assertEquals(discussionType, topic.getDiscussionType());
		assertEquals(term, topic.getTerm().getText());
		assertNotNull(topic.getCreatedAt());
		assertNull(topic.getId());
	}

	@Test
	public void testIsPermissionSufficient()
	{
		assertFalse(Topic.isPermissionSufficient(EntityPermission.READ, null, null));
		assertFalse(Topic.isPermissionSufficient(EntityPermission.WRITE, null, null));
		assertFalse(Topic.isPermissionSufficient(EntityPermission.VOTE, null, null));

		assertTrue(Topic.isPermissionSufficient(EntityPermission.READ, EntityPermission.READ, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.READ, EntityPermission.VOTE, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.READ, EntityPermission.WRITE, null));

		assertFalse(Topic.isPermissionSufficient(EntityPermission.WRITE, EntityPermission.READ, null));
		assertFalse(Topic.isPermissionSufficient(EntityPermission.WRITE, EntityPermission.VOTE, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.WRITE, EntityPermission.WRITE, null));

		assertFalse(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.READ, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.READ, mockUser()));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.VOTE, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.VOTE, mockUser()));
		assertFalse(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.WRITE, null));
		assertTrue(Topic.isPermissionSufficient(EntityPermission.VOTE, EntityPermission.WRITE, mockUser()));
	}

	@Test
	public void testGetUpVotesInPercent()
	{
		Topic topic = mockTopic();
		assertNull(topic.getUpVotesInPercent());

		new TopicVote(false, topic, mockAuthor(mockUser()));
		assertEquals(Byte.valueOf((byte)0), topic.getUpVotesInPercent());

		new TopicVote(true, topic, mockAuthor(mockUser()));
		new TopicVote(true, topic, mockAuthor(mockUser()));
		assertEquals(Byte.valueOf((byte)67), topic.getUpVotesInPercent());
	}
}
