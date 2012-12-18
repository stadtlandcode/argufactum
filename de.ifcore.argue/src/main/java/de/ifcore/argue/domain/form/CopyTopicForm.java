package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.services.TopicService;

public class CopyTopicForm extends AbstractIdAjaxForm<TopicService, Topic>
{
	@Override
	public Topic accept(TopicService service, Author author)
	{
		return service.copy(id, author);
	}
}
