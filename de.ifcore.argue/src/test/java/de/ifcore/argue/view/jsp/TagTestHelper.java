package de.ifcore.argue.view.jsp;

import static org.mockito.Mockito.mock;

import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class TagTestHelper
{
	private final PageContext pageContext;

	public TagTestHelper(TagSupport tag)
	{
		this.pageContext = new MockPageContext();
		tag.setPageContext(pageContext);
	}

	public PageContext getPageContext()
	{
		return pageContext;
	}

	public RequestContext mockRequestContext()
	{
		RequestContext requestContext = mock(RequestContext.class);
		pageContext.setAttribute(RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE, requestContext);
		return requestContext;
	}

	public String getResponseContent()
	{
		try
		{
			MockHttpServletResponse response = (MockHttpServletResponse)pageContext.getResponse();
			String content = response.getContentAsString();
			response.setCommitted(false);
			response.reset();
			return content;
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	public String getEndTagResponse(TagSupport tag) throws JspException
	{
		tag.doEndTag();
		return getResponseContent();
	}
}
