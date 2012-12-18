package de.ifcore.argue.services;

import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.EntityCruDao;
import de.ifcore.argue.dao.EntityDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.RelevanceVote;
import de.ifcore.argue.domain.entities.TopicEntityWithRelevance;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.form.AbstractRelevanceVoteForm;

public abstract class AbstractRelevanceVoteService<E extends TopicEntityWithRelevance<R>, D extends EntityCruDao<E, Long>, R extends RelevanceVote>
		extends AbstractDeletableTopicEntityService<E, D> implements RelevanceVoteService<R>
{
	private final EntityDao<R, Long> relevanceVoteDao;

	public AbstractRelevanceVoteService(D entityDao, TopicAccessRightService accessRightService,
			EntityDao<R, Long> relevanceVoteDao)
	{
		super(entityDao, accessRightService);
		this.relevanceVoteDao = relevanceVoteDao;
	}

	@Override
	@Transactional
	public <T extends AbstractRelevanceVoteForm<R>> R persistRelevanceVote(T form, Author author)
	{
		E topicEntity = entityDao.get(form.getId());
		accessRightService.checkAccess(topicEntity, author.getRegisteredUser(), EntityPermission.VOTE);

		R relevanceVote = topicEntity.getRelevanceVoteOfUser(author.getUserId());
		if (relevanceVote == null)
		{
			if (form.getRelevance() == null)
			{
				return null;
			}
			else
			{
				relevanceVote = getNewRelevanceVote(topicEntity, form.getRelevance(), author);
				relevanceVoteDao.save(relevanceVote);
			}
		}
		else
		{
			if (form.getRelevance() == null)
			{
				topicEntity.deleteRelevanceVote(relevanceVote);
				relevanceVoteDao.delete(relevanceVote);
				relevanceVote = null;
			}
			else
			{
				relevanceVote.edit(form.getRelevance());
				relevanceVoteDao.update(relevanceVote);
			}
		}
		return relevanceVote;
	}

	protected abstract R getNewRelevanceVote(E topicEntity, Relevance relevance, Author author);
}
