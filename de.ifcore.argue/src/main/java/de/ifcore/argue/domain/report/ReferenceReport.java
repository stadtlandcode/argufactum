package de.ifcore.argue.domain.report;

public interface ReferenceReport extends TopicEntityReport
{
	public Long getFactId();

	public String getText();

	public String getUrl();

	public LogReport getCreationLog();
}
