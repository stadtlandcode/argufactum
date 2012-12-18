package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.CriterionImportance;
import de.ifcore.argue.services.CriterionService;

public class CriterionImportanceForm extends AbstractAjaxForm<CriterionService, CriterionImportance>
{
	@NotNull
	private Long criterionId;

	@NotNull
	private Byte importance;

	private Long importanceId;

	public Long getCriterionId()
	{
		return criterionId;
	}

	public void setCriterionId(Long criterionId)
	{
		this.criterionId = criterionId;
	}

	public Byte getImportance()
	{
		return importance;
	}

	public void setImportance(Byte importance)
	{
		this.importance = importance;
	}

	public Long getImportanceId()
	{
		return importanceId;
	}

	public void setImportanceId(Long importanceId)
	{
		this.importanceId = importanceId;
	}

	@Override
	public CriterionImportance accept(CriterionService service, Author author)
	{
		return service.setImportance(this, author);
	}
}
