package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentRelevanceVote;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.utils.EntityTestUtils;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArgumentRelevanceVoteDaoTest extends IntegrationTest
{
	@Autowired
	private ArgumentRelevanceVoteDao relevanceVoteDao;

	@Test
	public void testSave()
	{
		Argument argument = persistArgument(mockArgument());
		RegisteredUser user = persistUser(mockUser());

		ArgumentRelevanceVote vote = new ArgumentRelevanceVote(argument, Relevance.AVERAGE,
				EntityTestUtils.mockAuthor(user));
		relevanceVoteDao.save(vote);
		flushAndClear();

		Argument persistedArgument = argumentDao.get(argument.getId());
		assertEquals(Relevance.AVERAGE, persistedArgument.getRelevance());
		assertEquals(1, persistedArgument.getRelevanceVotes().size());

		ArgumentRelevanceVote persistedVote = persistedArgument.getRelevanceVotes().iterator().next();
		assertNotNull(persistedVote);
		assertEquals(vote.getDedicatedEntity(), persistedVote.getDedicatedEntity());
		assertEquals(vote.getAuthor(), persistedVote.getAuthor());
		assertEquals(vote.getCreatedAt(), persistedVote.getCreatedAt());
		assertEquals(vote.getRelevance(), persistedVote.getRelevance());
	}

	@Test
	public void testDelete()
	{
		Argument argument = persistArgument(mockArgument());
		RegisteredUser user = persistUser(mockUser());
		ArgumentRelevanceVote vote = new ArgumentRelevanceVote(argument, Relevance.AVERAGE,
				EntityTestUtils.mockAuthor(user));
		relevanceVoteDao.save(vote);
		flushAndClear();

		Argument persistedArgument = argumentDao.get(argument.getId());
		assertEquals(1, persistedArgument.getRelevanceVotes().size());
		relevanceVoteDao.delete(persistedArgument.getRelevanceVotes().iterator().next());
		flushAndClear();

		persistedArgument = argumentDao.get(argument.getId());
		assertEquals(0, persistedArgument.getRelevanceVotes().size());
	}
}
