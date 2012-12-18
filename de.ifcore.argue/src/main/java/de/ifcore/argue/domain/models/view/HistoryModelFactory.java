package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.EventReport;

public interface HistoryModelFactory<T extends EventReport>
{
	public EventModel getEventModel(T eventReport, String entityName, MessageSource messageSource);
}
