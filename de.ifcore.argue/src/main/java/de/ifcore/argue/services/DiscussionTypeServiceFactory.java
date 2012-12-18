package de.ifcore.argue.services;

import de.ifcore.argue.domain.enumerations.DiscussionType;

public interface DiscussionTypeServiceFactory
{
	public DiscussionTypeService<?> getService(DiscussionType discussionType);
}
