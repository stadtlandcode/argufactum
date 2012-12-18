package de.ifcore.argue.domain.models.view;

import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.TopicEventReport;

public class TopicHistoryModel extends AbstractHistoryModel<TopicEventReport> implements ViewModel
{
	private static final long serialVersionUID = 2111593637430783430L;
	private static final HistoryModelFactory<TopicEventReport> factory = new TopicHistoryModelFactory();

	public TopicHistoryModel(Long entityId, Set<TopicEventReport> reports, MessageSource messageSource)
	{
		super(entityId, "topic", reports, messageSource, factory);
	}

	private static class TopicHistoryModelFactory implements HistoryModelFactory<TopicEventReport>
	{
		@Override
		public EventModel getEventModel(TopicEventReport eventReport, String entityName, MessageSource messageSource)
		{
			return new TopicEventModel(eventReport, entityName, messageSource);
		}
	}
}
