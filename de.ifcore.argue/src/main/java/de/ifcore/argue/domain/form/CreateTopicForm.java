package de.ifcore.argue.domain.form;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.services.TopicService;

public final class CreateTopicForm extends AbstractForm<TopicService, Topic>
{
	@Size(max = 125)
	private String term;

	@Size(max = 255)
	private String definition;

	@NotNull
	private DiscussionType discussionType;

	private TopicVisibility visibility;

	private Long categoryId;

	public CreateTopicForm()
	{
		this.discussionType = DiscussionType.PRO_CONTRA;
	}

	public String getTerm()
	{
		return term;
	}

	public void setTerm(String term)
	{
		this.term = term;
	}

	public String getDefinition()
	{
		return definition;
	}

	public void setDefinition(String definition)
	{
		this.definition = definition;
	}

	public void setValidTerm(MessageSource messageSource, Locale locale)
	{
		if (StringUtils.isBlank(term))
			this.term = messageSource.getMessage("topic.firstTerm", null, locale);
	}

	public DiscussionType getDiscussionType()
	{
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType)
	{
		this.discussionType = discussionType;
	}

	public TopicVisibility getVisibility()
	{
		return visibility == null ? TopicVisibility.PRIVATE : visibility;
	}

	public void setVisibility(TopicVisibility visibility)
	{
		this.visibility = visibility;
	}

	public Long getCategoryId()
	{
		return Long.valueOf(0).equals(categoryId) ? null : categoryId;
	}

	public void setCategoryId(Long categoryId)
	{
		this.categoryId = categoryId;
	}

	@Override
	public Topic accept(TopicService service, Author author)
	{
		return service.create(this, author);
	}
}
