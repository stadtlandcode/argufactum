package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.ReferenceReport;

public class ReferenceModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = 4021835935413688128L;

	private final Long factId;
	private final String text;
	private final String url;
	private final LogModel creationLog;

	public ReferenceModel(ReferenceReport reference, MessageSource messageSource)
	{
		super(reference, reference.getTopicId());
		this.factId = reference.getFactId();
		this.text = reference.getText();
		this.url = reference.getUrl();
		this.creationLog = LogModel.instanceOf(reference.getCreationLog(), messageSource);
	}

	public String getText()
	{
		return text;
	}

	public String getUrl()
	{
		return url;
	}

	public LogModel getCreationLog()
	{
		return creationLog;
	}

	public Long getFactId()
	{
		return factId;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ReferenceModel [text=").append(text).append(", url=").append(url).append(", creationLog=")
				.append(creationLog).append("]");
		return builder.toString();
	}

	@Override
	public String getBroadcastSubject()
	{
		return "reference.create";
	}
}
