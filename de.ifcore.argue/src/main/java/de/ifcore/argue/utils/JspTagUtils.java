package de.ifcore.argue.utils;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class JspTagUtils
{
	public static void write(PageContext pageContext, String out) throws JspException
	{
		try
		{
			pageContext.getOut().write(out);
		}
		catch (IOException e)
		{
			throw new JspException(e);
		}
	}
}
