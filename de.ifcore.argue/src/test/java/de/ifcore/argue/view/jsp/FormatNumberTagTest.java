package de.ifcore.argue.view.jsp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.jsp.JspException;

import org.junit.Test;
import org.springframework.web.servlet.support.RequestContext;

public class FormatNumberTagTest
{
	@Test
	public void testDoEndTag() throws JspException
	{
		FormatNumberTag tag = new FormatNumberTag();
		TagTestHelper tagTestHelper = new TagTestHelper(tag);
		RequestContext requestContext = tagTestHelper.mockRequestContext();
		tag.doStartTag();

		tag.setValue(Integer.valueOf(1));
		assertEquals("1", tagTestHelper.getEndTagResponse(tag));

		tag.setValue(Integer.valueOf(12012));
		assertEquals("12.012", tagTestHelper.getEndTagResponse(tag));

		tag.setMessageCode("facts");
		when(requestContext.getMessage(anyString(), any(Object[].class))).thenReturn("x");
		tag.doEndTag();
		verify(requestContext).getMessage("facts", new Object[] { "12.012" });

		tag.setValue(Integer.valueOf("1"));
		tag.doEndTag();
		verify(requestContext).getMessage("facts", new Object[] { "1" });

		tag.setHandleSingular(true);
		tag.doEndTag();
		verify(requestContext).getMessage("facts.1", new Object[] { "1" });

		tag.setValue(Integer.valueOf(0));
		tag.doEndTag();
		verify(requestContext).getMessage("facts", new Object[] { "0" });
	}
}
