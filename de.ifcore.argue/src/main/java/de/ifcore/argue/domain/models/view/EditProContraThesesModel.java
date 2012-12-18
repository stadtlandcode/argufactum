package de.ifcore.argue.domain.models.view;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;

public class EditProContraThesesModel extends AbstractTopicBroadcast implements ViewModel
{
	private static final long serialVersionUID = -557232056523969586L;

	private final String proThesis;
	private final String contraThesis;
	private final TopicThesesMessageCodeAppendix thesesMessageCodeAppendix;

	public EditProContraThesesModel(ProContraTopic proContraTopic, MessageSource messageSource)
	{
		super(proContraTopic.getId());
		Locale locale = LocaleContextHolder.getLocale();
		this.proThesis = messageSource.getMessage(proContraTopic.getProMessageCode(), null, locale);
		this.contraThesis = messageSource.getMessage(proContraTopic.getContraMessageCode(), null, locale);
		this.thesesMessageCodeAppendix = proContraTopic.getThesesMessageCodeAppendix();
	}

	public String getProThesis()
	{
		return proThesis;
	}

	public String getContraThesis()
	{
		return contraThesis;
	}

	public TopicThesesMessageCodeAppendix getThesesMessageCodeAppendix()
	{
		return thesesMessageCodeAppendix;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "proContraTheses.edit";
	}
}
