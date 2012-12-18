package de.ifcore.argue.domain.report;

import de.ifcore.argue.domain.enumerations.LogEvent;

public interface EventReport extends LogReport
{
	public LogEvent getEvent();

	public String getText();
}
