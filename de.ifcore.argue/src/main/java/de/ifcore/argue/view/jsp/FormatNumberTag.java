package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.Printer;
import org.springframework.format.number.NumberFormatter;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import de.ifcore.argue.utils.JspTagUtils;

public class FormatNumberTag extends RequestContextAwareTag
{
	private static final long serialVersionUID = 4962837554382320577L;
	private static final Printer<Number> numberFormatter = new NumberFormatter();
	private static final Integer one = Integer.valueOf(1);

	private Integer value;
	private String messageCode;
	private boolean handleSingular;

	@Override
	public int doEndTag() throws JspException
	{
		if (value == null || (handleSingular && messageCode == null))
			throw new IllegalArgumentException();

		String formattedNumber = numberFormatter.print(value, LocaleContextHolder.getLocale());
		String out;
		if (this.messageCode != null)
		{
			String messageCode = this.messageCode;
			if (handleSingular && value.compareTo(one) == 0)
				messageCode = messageCode + ".1";
			out = getRequestContext().getMessage(messageCode, new Object[] { formattedNumber });
		}
		else
			out = formattedNumber;

		JspTagUtils.write(pageContext, out);
		return super.doEndTag();
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}

	public void setMessageCode(String messageCode)
	{
		this.messageCode = messageCode;
	}

	public void setHandleSingular(boolean handleSingular)
	{
		this.handleSingular = handleSingular;
	}

	@Override
	protected int doStartTagInternal() throws Exception
	{
		return SKIP_BODY;
	}
}
