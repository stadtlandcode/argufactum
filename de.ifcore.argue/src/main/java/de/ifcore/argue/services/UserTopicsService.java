package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.models.view.TopicModel;

public interface UserTopicsService
{
	public Set<TopicModel> getAll(Long userId);
}
