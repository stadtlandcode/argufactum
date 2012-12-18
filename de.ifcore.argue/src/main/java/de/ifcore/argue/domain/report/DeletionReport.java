package de.ifcore.argue.domain.report;

public interface DeletionReport extends EntityReport
{
	public String getText();

	public LogReport getDeletionLog();
}
