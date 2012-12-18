package de.ifcore.argue.domain.models.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.EventReport;

public class AbstractHistoryModel<T extends EventReport> implements Serializable
{
	private static final long serialVersionUID = -1031387446501198340L;

	private final Long id;
	private final Set<EventModel> events;

	protected AbstractHistoryModel(Long entityId, String entityName, Set<T> reports, MessageSource messageSource,
			HistoryModelFactory<T> factory)
	{
		this.id = entityId;
		Set<EventModel> events = new LinkedHashSet<>();
		for (T eventReport : reports)
			events.add(factory.getEventModel(eventReport, entityName, messageSource));
		this.events = Collections.unmodifiableSet(events);
	}

	public Long getId()
	{
		return id;
	}

	public Set<EventModel> getEvents()
	{
		return events;
	}
}
