package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;

public interface TopicViewCountService
{
	public void count(Topic topic, Author author);

	public void updateTopics();
}
