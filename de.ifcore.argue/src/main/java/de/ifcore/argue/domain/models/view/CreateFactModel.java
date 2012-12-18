package de.ifcore.argue.domain.models.view;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.report.FactReport;

public class CreateFactModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 3729124169554753874L;

	private final Long argumentId;
	private final Long textId;
	private final String text;
	private final FactType type;
	private final LogModel creationLog;
	private final LogModel modificationLog;
	private final RelevanceModel relevance;
	private final Set<ReferenceModel> references;
	private final Integer numberOfReferences;
	private final AttributionModel attribution;

	public CreateFactModel(FactReport fact, MessageSource messageSource)
	{
		super(fact, fact.getTopicId());
		this.argumentId = fact.getArgumentId();
		this.textId = fact.getEvidenceId();
		this.text = fact.getText();
		this.type = fact.getType();
		this.creationLog = LogModel.instanceOf(fact.getCreationLog(), messageSource);
		this.modificationLog = LogModel.instanceOf(fact.getModificationLog(), messageSource);
		this.relevance = RelevanceModel.instanceOf(fact, "fact." + fact.getType().name(), messageSource);
		this.references = getReferences(fact, messageSource);
		this.numberOfReferences = Integer.valueOf(fact.getReferences().size());
		this.attribution = new AttributionModel(fact);
	}

	private Set<ReferenceModel> getReferences(FactReport fact, MessageSource messageSource)
	{
		Set<ReferenceModel> references = new LinkedHashSet<>();
		for (Reference reference : fact.getReferences())
		{
			references.add(new ReferenceModel(reference, messageSource));
		}
		return references;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "fact.create";
	}

	public String getText()
	{
		return text;
	}

	public FactType getType()
	{
		return type;
	}

	public Long getParentId()
	{
		return getArgumentId();
	}

	public Long getArgumentId()
	{
		return argumentId;
	}

	public LogModel getCreationLog()
	{
		return creationLog;
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	public Set<ReferenceModel> getReferences()
	{
		return references;
	}

	public Integer getNumberOfReferences()
	{
		return numberOfReferences;
	}

	public Long getTextId()
	{
		return textId;
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
		builder.append("CreateFactModel [text=").append(text).append(", type=").append(type).append("]");
		return builder.toString();
	}
}
