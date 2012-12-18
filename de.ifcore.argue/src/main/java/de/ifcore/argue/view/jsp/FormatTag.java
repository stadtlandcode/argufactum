package de.ifcore.argue.view.jsp;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;

public class FormatTag extends TagSupport
{
	private static final long serialVersionUID = -1396094324145492064L;
	private static final TypeDescriptor stringType = TypeDescriptor.valueOf(String.class);
	private Object object;
	private String propertyName;

	@Override
	public int doEndTag() throws JspException
	{
		if (object == null)
			throw new JspException("object is required");

		try
		{
			if (propertyName == null)
			{
				this.pageContext.getOut().write(getConversionService(pageContext).convert(object, String.class));
			}
			else
			{
				PropertyDescriptor propertyDescriptor = BeanUtils
						.getPropertyDescriptor(object.getClass(), propertyName);
				Property property = new Property(propertyDescriptor.getPropertyType(),
						propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod());
				String formattedValue = (String)getConversionService(pageContext).convert(
						propertyDescriptor.getReadMethod().invoke(object), new TypeDescriptor(property), stringType);
				this.pageContext.getOut().write(formattedValue);
			}
		}
		catch (IOException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	private ConversionService getConversionService(PageContext pageContext)
	{
		return (ConversionService)pageContext.getRequest().getAttribute(ConversionService.class.getName());
	}

	public void setObject(Object object)
	{
		this.object = object;
	}

	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}
}
