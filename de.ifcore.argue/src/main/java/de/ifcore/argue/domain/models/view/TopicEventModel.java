package de.ifcore.argue.domain.models.view;

import java.util.Arrays;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.TopicEventReport;

public class TopicEventModel extends EventModel
{
	private static final long serialVersionUID = -5300445991323105692L;

	public TopicEventModel(TopicEventReport eventReport, String entity, MessageSource messageSource)
	{
		super(eventReport, entity, Arrays.asList(eventReport.getText(), eventReport.getDefinition()), messageSource);
	}
}
