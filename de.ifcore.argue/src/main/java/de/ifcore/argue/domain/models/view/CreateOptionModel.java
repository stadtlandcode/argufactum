package de.ifcore.argue.domain.models.view;

import de.ifcore.argue.domain.report.OptionReport;

public class CreateOptionModel extends EditOptionModel
{
	private static final long serialVersionUID = 1117067331527990924L;

	private final Long processId;

	public CreateOptionModel(OptionReport option, Long processId)
	{
		super(option);
		this.processId = processId;
	}

	public Long getProcessId()
	{
		return processId;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "createOption";
	}
}
