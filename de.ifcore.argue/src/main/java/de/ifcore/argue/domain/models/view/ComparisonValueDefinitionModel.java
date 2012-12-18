package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.ComparisonValueDefinitionReport;

public class ComparisonValueDefinitionModel implements ViewModel
{
	private static final long serialVersionUID = -5762784363797857440L;

	private final String string;
	private final Byte percent;

	public ComparisonValueDefinitionModel(ComparisonValueDefinitionReport definitionReport)
	{
		this.string = definitionReport.getString();
		this.percent = definitionReport.getPercent();
	}

	public String getString()
	{
		return string;
	}

	public Byte getPercent()
	{
		return percent;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValueDefinitionModel [string=").append(string).append(", percent=").append(percent)
				.append("]");
		return builder.toString();
	}

}
