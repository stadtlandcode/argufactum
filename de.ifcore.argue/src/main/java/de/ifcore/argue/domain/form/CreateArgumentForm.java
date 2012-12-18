package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.services.ArgumentService;

public final class CreateArgumentForm extends AbstractAjaxForm<ArgumentService, Argument>
{
	@NotNull
	private Long topicId;

	@Size(max = 255)
	@NotBlank
	private String thesis;

	@NotNull
	private TopicThesis topicThesis;

	public String getThesis()
	{
		return thesis;
	}

	public void setThesis(String thesis)
	{
		this.thesis = thesis;
	}

	public TopicThesis getTopicThesis()
	{
		return topicThesis;
	}

	public void setTopicThesis(TopicThesis topicThesis)
	{
		this.topicThesis = topicThesis;
	}

	public Long getTopicId()
	{
		return topicId;
	}

	public void setTopicId(Long topicId)
	{
		this.topicId = topicId;
	}

	@Override
	public Argument accept(ArgumentService service, Author author)
	{
		return service.create(this, author);
	}
}
