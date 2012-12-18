package de.ifcore.argue.domain.models.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.report.EventReport;

public class EventModel extends LogModel implements ViewModel
{
	private static final long serialVersionUID = 6710806465644987227L;

	private final String description;
	private final boolean restorable;

	public EventModel(EventReport eventReport, String entity, MessageSource messageSource)
	{
		this(eventReport, entity, Arrays.asList(eventReport.getText()), messageSource);
	}

	protected EventModel(EventReport eventReport, String entity, List<String> descriptionArguments,
			MessageSource messageSource)
	{
		super(eventReport, messageSource);
		List<String> escapedDescriptionArguments = new ArrayList<>();
		for (String argument : descriptionArguments)
		{
			if (argument == null)
				argument = "";
			escapedDescriptionArguments.add(StringEscapeUtils.escapeHtml4(argument));
		}

		this.description = messageSource.getMessage(entity + ".event." + eventReport.getEvent().name(),
				escapedDescriptionArguments.toArray(), LocaleContextHolder.getLocale());
		this.restorable = eventReport.getText() != null;
	}

	public String getDescription()
	{
		return description;
	}

	public boolean isRestorable()
	{
		return restorable;
	}
}
