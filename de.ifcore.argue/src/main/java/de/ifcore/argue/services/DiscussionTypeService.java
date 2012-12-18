package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;

public interface DiscussionTypeService<T> extends Service
{
	public T create(Topic topic);

	public void saveWithDescendants(Topic dest);

	public boolean canHandle(DiscussionType discussionType);
}
