package de.ifcore.argue.domain.entities;

import de.ifcore.argue.domain.report.EntityReport;

public interface TopicEntity extends IdEntity, EntityReport
{
	public Long getTopicId();
}
