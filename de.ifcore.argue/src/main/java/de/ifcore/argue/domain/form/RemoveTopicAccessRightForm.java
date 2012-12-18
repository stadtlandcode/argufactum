package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.services.TopicAccessRightService;

public class RemoveTopicAccessRightForm extends AbstractIdAjaxForm<TopicAccessRightService, TopicAccessRight>
{
	@Override
	public TopicAccessRight accept(TopicAccessRightService service, Author author)
	{
		return service.removeAccessRight(this, author);
	}
}
