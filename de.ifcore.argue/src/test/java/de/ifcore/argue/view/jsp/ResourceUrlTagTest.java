package de.ifcore.argue.view.jsp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContext;

public class ResourceUrlTagTest
{
	@Test
	public void testGetMd5Value() throws JspException
	{
		WebApplicationContext webApplicationContext = mock(WebApplicationContext.class);
		ServletContext servletContext = mock(ServletContext.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(RequestContext.WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
		RequestContext requestContext = new RequestContext(request);
		ReflectionTestUtils.setField(requestContext, "webApplicationContext", webApplicationContext);
		when(webApplicationContext.getServletContext()).thenReturn(servletContext);
		when(servletContext.getRealPath("/test.min.js")).thenReturn("src/test/resources/test.min.js");

		ResourceUrlTag urlTag = new ResourceUrlTag();
		ReflectionTestUtils.setField(urlTag, "requestContext", requestContext);
		assertEquals("/test_7d6997a9b7.min.js", urlTag.getMd5Value("/test.min.js", 10));
	}
}
