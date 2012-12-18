package de.ifcore.argue.controller.topic;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import de.ifcore.argue.domain.entities.Topic;

@Component
public class TopicViewHelperFactory
{
	private final Set<TopicViewHelper> helpers;

	@Inject
	private TopicViewHelperFactory(Set<TopicViewHelper> helpers)
	{
		this.helpers = helpers;
	}

	public TopicViewHelper getHelper(Topic topic)
	{
		for (TopicViewHelper helper : helpers)
		{
			if (helper.isResponsible(topic))
				return helper;
		}
		return null;
	}
}
