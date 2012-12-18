package de.ifcore.argue.domain.models.view;

import java.math.BigDecimal;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.report.RelevanceReport;

public class RelevanceModel implements ViewModel
{
	private static final long serialVersionUID = 1305334669146124167L;

	private final String label;
	private final Relevance value;
	private final BigDecimal numericValue;

	public static RelevanceModel instanceOf(RelevanceReport relevanceReport, String entityName,
			MessageSource messageSource)
	{
		return new RelevanceModel(relevanceReport, entityName, messageSource);
	}

	private RelevanceModel(RelevanceReport relevanceReport, String entityName, MessageSource messageSource)
	{
		if (entityName == null)
			entityName = "";
		entityName = entityName + ".";

		if (relevanceReport.getRelevance() != null)
		{
			this.label = messageSource.getMessage("Relevance." + entityName + relevanceReport.getRelevance(),
					new String[] { relevanceReport.getNumericRelevance().toString() }, LocaleContextHolder.getLocale());
		}
		else
		{
			this.label = messageSource.getMessage("Relevance.undefined", null, LocaleContextHolder.getLocale());
		}
		this.value = relevanceReport.getRelevance();
		this.numericValue = relevanceReport.getNumericRelevance();
	}

	public String getLabel()
	{
		return label;
	}

	public Relevance getValue()
	{
		return value;
	}

	public BigDecimal getNumericValue()
	{
		return numericValue;
	}
}
