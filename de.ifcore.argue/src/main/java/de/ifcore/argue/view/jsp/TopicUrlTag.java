package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.UrlTag;

import de.ifcore.argue.controller.topic.TopicController;
import de.ifcore.argue.domain.entities.TopicUrl;

public class TopicUrlTag extends UrlTag
{
	private static final long serialVersionUID = 5709430577478470980L;
	private TopicUrl topic;

	@Override
	public int doStartTagInternal() throws JspException
	{
		setValue(TopicController.getUrl(topic));
		return super.doStartTagInternal();
	}

	public void setTopic(TopicUrl topic)
	{
		this.topic = topic;
	}
}
