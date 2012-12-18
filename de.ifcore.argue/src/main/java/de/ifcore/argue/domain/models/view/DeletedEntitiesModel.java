package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.comparators.DeletionReportComparator;
import de.ifcore.argue.domain.report.DeletionReport;

public class DeletedEntitiesModel implements ViewModel
{
	private static final long serialVersionUID = -5207941968651305671L;

	private final Set<DeletedEntityModel> entities;

	public DeletedEntitiesModel(Set<? extends DeletionReport> deletionReports, MessageSource messageSource)
	{
		Set<DeletionReport> sortedDeletionReports = new TreeSet<>(DeletionReportComparator.getInstance());
		sortedDeletionReports.addAll(deletionReports);

		Set<DeletedEntityModel> entities = new LinkedHashSet<>();
		for (DeletionReport deletionReport : sortedDeletionReports)
			entities.add(new DeletedEntityModel(deletionReport, messageSource));
		this.entities = Collections.unmodifiableSet(entities);
	}

	public Set<DeletedEntityModel> getEntities()
	{
		return entities;
	}
}
