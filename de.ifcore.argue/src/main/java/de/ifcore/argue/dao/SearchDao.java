package de.ifcore.argue.dao;

import java.util.List;

import de.ifcore.argue.domain.entities.Topic;

public interface SearchDao
{
	public List<Topic> searchTopics(String searchTerm);
}
