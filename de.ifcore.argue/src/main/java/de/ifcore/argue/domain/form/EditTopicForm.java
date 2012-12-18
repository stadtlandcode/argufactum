package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.services.TopicService;

public final class EditTopicForm extends AbstractAjaxForm<TopicService, Topic>
{
	@NotNull
	private Long id;

	@Size(max = 125)
	@NotBlank
	private String term;

	@Size(max = 255)
	private String definition;

	private Long categoryId;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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
		return service.edit(this, author);
	}
}
