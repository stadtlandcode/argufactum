package de.ifcore.argue.domain.comparators;

import java.util.Comparator;

import de.ifcore.argue.domain.report.LogReport;

public class LogReportComparator implements Comparator<LogReport>
{
	private final static LogReportComparator instance = new LogReportComparator();

	public static LogReportComparator getInstance()
	{
		return instance;
	}

	private LogReportComparator()
	{
	}

	@Override
	public int compare(LogReport o1, LogReport o2)
	{
		return o2.getDateTime().compareTo(o1.getDateTime());
	}
}
