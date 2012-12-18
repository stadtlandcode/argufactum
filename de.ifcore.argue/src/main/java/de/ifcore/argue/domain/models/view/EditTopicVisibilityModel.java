package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.report.TopicReport;

public class EditTopicVisibilityModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 1463551633043100612L;

	private final TopicVisibility visibility;

	public EditTopicVisibilityModel(TopicReport topic)
	{
		super(topic, topic.getId());
		this.visibility = topic.getVisibility();
	}

	public TopicVisibility getVisibility()
	{
		return visibility;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "topic.editVisibility";
	}

}
