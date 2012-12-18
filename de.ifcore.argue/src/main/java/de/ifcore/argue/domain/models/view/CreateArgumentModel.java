package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.report.ArgumentReport;

public class CreateArgumentModel extends AbstractArgumentModel implements ViewModel
{
	private static final long serialVersionUID = -7771854042298386813L;
	private final TopicThesis topicThesis;
	private final Integer numberOfConfirmativeFacts;
	private final Integer numberOfDebilitativeFacts;
	private final LogModel creationLog;
	private final LogModel modificationLog;
	private final RelevanceModel relevance;
	private final String url;
	private final AttributionModel attribution;

	public CreateArgumentModel(ArgumentReport argument, MessageSource messageSource, String url)
	{
		super(argument);
		this.topicThesis = argument.getTopicThesis();
		this.numberOfConfirmativeFacts = argument.getNumberOfFacts(FactType.CONFIRMATIVE);
		this.numberOfDebilitativeFacts = argument.getNumberOfFacts(FactType.DEBILITATIVE);
		this.creationLog = LogModel.instanceOf(argument.getCreationLog(), messageSource);
		this.modificationLog = LogModel.instanceOf(argument.getModificationLog(), messageSource);
		this.relevance = RelevanceModel.instanceOf(argument, "argument", messageSource);
		this.url = url;
		this.attribution = new AttributionModel(argument);
	}

	@Override
	public String getBroadcastSubject()
	{
		return "argument.create";
	}

	public TopicThesis getTopicThesis()
	{
		return topicThesis;
	}

	public TopicThesis getType()
	{
		return topicThesis;
	}

	public Integer getNumberOfConfirmativeFacts()
	{
		return numberOfConfirmativeFacts;
	}

	public Integer getNumberOfDebilitativeFacts()
	{
		return numberOfDebilitativeFacts;
	}

	public LogModel getCreationLog()
	{
		return creationLog;
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	public String getUrl()
	{
		return url;
	}

	public RelevanceModel getRelevance()
	{
		return relevance;
	}

	public AttributionModel getAttribution()
	{
		return attribution;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CreateArgumentModel [id=").append(getId()).append(", text=").append(getText())
				.append(", textId=").append(getTextId()).append(", topicThesis=").append(topicThesis)
				.append(", creationLog=").append(creationLog).append(", modificationLog=").append(modificationLog)
				.append(", url=").append(url).append("]");
		return builder.toString();
	}
}
