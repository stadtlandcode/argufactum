package de.ifcore.argue.controller.topic;

import org.springframework.ui.Model;

import de.ifcore.argue.domain.entities.Topic;

public interface TopicViewHelper
{
	public void addAttributes(Topic topic, Model model);

	public String getViewName();

	public String getAjaxViewName();

	public boolean isResponsible(Topic topic);
}
