package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.TopicReport;

public class EditTopicModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 5012614504767214322L;
	private final Long termId;
	private final String text;
	private final String definition;
	private final CategoryModel category;
	private final LogModel modificationLog;

	public EditTopicModel(TopicReport topic, MessageSource messageSource)
	{
		super(topic, topic.getId());
		this.termId = topic.getTermId();
		this.text = topic.getText();
		this.definition = topic.getDefinition();
		this.category = topic.getCategory() == null ? null : new CategoryModel(topic.getCategory());
		this.modificationLog = LogModel.instanceOf(topic.getModificationLog(), messageSource);
	}

	public Long getTermId()
	{
		return termId;
	}

	public String getText()
	{
		return text;
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	public CategoryModel getCategory()
	{
		return category;
	}

	public String getDefinition()
	{
		return definition;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "topic.editTerm";
	}
}
