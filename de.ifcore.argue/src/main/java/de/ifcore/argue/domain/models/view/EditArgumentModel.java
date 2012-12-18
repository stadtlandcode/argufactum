package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.report.ArgumentReport;

public class EditArgumentModel extends AbstractArgumentModel implements ViewModel
{
	private static final long serialVersionUID = 2607331994998273193L;

	private final LogModel modificationLog;

	public EditArgumentModel(ArgumentReport argument, MessageSource messageSource)
	{
		super(argument);
		this.modificationLog = LogModel.instanceOf(argument.getModificationLog(), messageSource);
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "argument.edit";
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EditArgumentModel [modificationLog=").append(modificationLog).append(", getId()=")
				.append(getId()).append(", getTextId()=").append(getTextId()).append(", getText()=").append(getText())
				.append("]");
		return builder.toString();
	}
}
