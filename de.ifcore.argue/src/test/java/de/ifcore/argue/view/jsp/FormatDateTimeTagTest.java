package de.ifcore.argue.view.jsp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.jsp.JspException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.springframework.web.servlet.support.RequestContext;

public class FormatDateTimeTagTest
{
	@Test
	public void testDoEndTag() throws JspException
	{
		FormatDateTimeTag tag = new FormatDateTimeTag();
		TagTestHelper tagTestHelper = new TagTestHelper(tag);
		RequestContext requestContext = tagTestHelper.mockRequestContext();
		DateTime dateTime = new DateTime(2011, 10, 26, 16, 41, 23, DateTimeZone.UTC);
		tag.doStartTag();

		tag.setValue(dateTime);
		assertEquals("2011-10-26T16:41:23Z", tagTestHelper.getEndTagResponse(tag));

		tag.setPatternMessageCode("datetime.full");
		when(requestContext.getMessage("datetime.full")).thenReturn("dd. MMMM YYYY HH:mm");
		assertEquals("26. Oktober 2011 16:41", tagTestHelper.getEndTagResponse(tag));
	}
}
