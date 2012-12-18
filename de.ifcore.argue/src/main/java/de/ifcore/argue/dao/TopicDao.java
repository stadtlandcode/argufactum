package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicTerm;
import de.ifcore.argue.domain.entities.TopicVote;

public interface TopicDao extends EntityDao<Topic, Long>
{
	public List<Topic> getLatestPublicTopics(int maxResults);

	public List<Topic> getSelectedTopics();

	public List<Topic> getAssociatedTopics(Long userId);

	public void saveTerm(TopicTerm topicTerm);

	public List<TopicTerm> getTerms(Long topicId);

	public TopicAccessRight getAccessRight(Long id);

	public void saveAccessRight(TopicAccessRight accessRight);

	public void deleteAccessRight(TopicAccessRight accessRight);

	public void saveVote(TopicVote vote);

	public void updateVote(TopicVote vote);

	public void deleteVote(TopicVote vote);
}
