package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.services.TopicAccessRightService;

public class TopicVisibilityForm extends AbstractIdAjaxForm<TopicAccessRightService, Topic>
{
	private TopicVisibility visibility;

	@Override
	public Topic accept(TopicAccessRightService service, Author author)
	{
		return service.setVisibility(this, author);
	}

	public TopicVisibility getVisibility()
	{
		return visibility == null ? TopicVisibility.PRIVATE : visibility;
	}

	public void setVisibility(TopicVisibility visibility)
	{
		this.visibility = visibility;
	}
}
