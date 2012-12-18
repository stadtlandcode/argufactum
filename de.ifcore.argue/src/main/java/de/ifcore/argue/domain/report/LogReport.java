package de.ifcore.argue.domain.report;

import org.joda.time.DateTime;

public interface LogReport
{
	public String getAuthorName();

	public Long getUserId();

	public DateTime getDateTime();
}
