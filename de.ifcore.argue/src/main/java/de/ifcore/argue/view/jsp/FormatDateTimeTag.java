package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.joda.time.DateTime;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import de.ifcore.argue.utils.FormatUtils;
import de.ifcore.argue.utils.JspTagUtils;

public class FormatDateTimeTag extends RequestContextAwareTag
{
	private static final long serialVersionUID = -3736190353389891203L;

	private DateTime value;
	private String patternMessageCode;

	@Override
	public int doEndTag() throws JspException
	{
		String pattern = patternMessageCode == null ? null : getRequestContext().getMessage(patternMessageCode);
		JspTagUtils.write(pageContext, FormatUtils.formatDateTime(value, pattern));
		return super.doEndTag();
	}

	@Override
	protected int doStartTagInternal() throws Exception
	{
		return SKIP_BODY;
	}

	public void setValue(DateTime value)
	{
		this.value = value;
	}

	public void setPatternMessageCode(String patternMessageCode)
	{
		this.patternMessageCode = patternMessageCode;
	}
}
