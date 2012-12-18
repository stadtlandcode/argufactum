package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.services.ComparisonValueService;

public class ComparisonValueForm extends AbstractAjaxForm<ComparisonValueService, ComparisonValue>
{
	@NotNull
	private Long optionId;

	@NotNull
	private Long criterionId;

	@NotNull
	private String string;

	public Long getOptionId()
	{
		return optionId;
	}

	public void setOptionId(Long optionId)
	{
		this.optionId = optionId;
	}

	public Long getCriterionId()
	{
		return criterionId;
	}

	public void setCriterionId(Long criterionId)
	{
		this.criterionId = criterionId;
	}

	public String getString()
	{
		return string;
	}

	public void setString(String string)
	{
		this.string = string;
	}

	@Override
	public ComparisonValue accept(ComparisonValueService service, Author author)
	{
		return service.persist(this, author);
	}
}
