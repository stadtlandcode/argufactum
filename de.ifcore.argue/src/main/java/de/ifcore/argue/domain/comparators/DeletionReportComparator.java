package de.ifcore.argue.domain.comparators;

import java.util.Comparator;

import de.ifcore.argue.domain.report.DeletionReport;

public class DeletionReportComparator implements Comparator<DeletionReport>
{
	private final static DeletionReportComparator instance = new DeletionReportComparator();

	public static DeletionReportComparator getInstance()
	{
		return instance;
	}

	private DeletionReportComparator()
	{
	}

	@Override
	public int compare(DeletionReport o1, DeletionReport o2)
	{
		return LogReportComparator.getInstance().compare(o1.getDeletionLog(), o2.getDeletionLog());
	}
}
