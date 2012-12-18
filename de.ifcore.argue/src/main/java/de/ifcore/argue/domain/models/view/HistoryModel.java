package de.ifcore.argue.domain.models.view;

import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.EventReport;

public class HistoryModel extends AbstractHistoryModel<EventReport> implements ViewModel
{
	private static final long serialVersionUID = 7509036395349663160L;
	private static final HistoryModelFactory<EventReport> factory = new HistoryModelFactoryImpl();

	public HistoryModel(Long entityId, String entityName, Set<EventReport> reports, MessageSource messageSource)
	{
		super(entityId, entityName, reports, messageSource, factory);
	}

	private static class HistoryModelFactoryImpl implements HistoryModelFactory<EventReport>
	{
		@Override
		public EventModel getEventModel(EventReport eventReport, String entityName, MessageSource messageSource)
		{
			return new EventModel(eventReport, entityName, messageSource);
		}
	}
}
