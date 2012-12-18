package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.DeletionReport;

public class DeletedEntityModel extends LogModel implements ViewModel
{
	private static final long serialVersionUID = -1029195836153190925L;

	private final Long id;
	private final String text;

	public DeletedEntityModel(DeletionReport deletionReport, MessageSource messageSource)
	{
		super(deletionReport.getDeletionLog(), messageSource);
		this.id = deletionReport.getId();
		this.text = deletionReport.getText();
	}

	public Long getId()
	{
		return id;
	}

	public String getText()
	{
		return text;
	}
}
