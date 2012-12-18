package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.UrlTag;

import de.ifcore.argue.controller.ArgumentController;
import de.ifcore.argue.domain.entities.Argument;

public class ArgumentUrlTag extends UrlTag
{
	private static final long serialVersionUID = 639000922503780579L;
	private Argument argument;

	@Override
	public int doStartTagInternal() throws JspException
	{
		setValue(ArgumentController.getUrl(argument));
		return super.doStartTagInternal();
	}

	public void setArgument(Argument argument)
	{
		this.argument = argument;
	}
}
