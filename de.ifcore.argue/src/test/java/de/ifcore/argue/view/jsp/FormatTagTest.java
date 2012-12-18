package de.ifcore.argue.view.jsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.servlet.jsp.JspException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.annotation.NumberFormat;

public class FormatTagTest
{
	@Test
	public void testObject() throws JspException
	{
		FormatTag formatTag = new FormatTag();
		TagTestHelper tagTestHelper = new TagTestHelper(formatTag);
		ConversionService conversionService = setupConversionService(tagTestHelper);
		MockFormatObject formatObject = new MockFormatObject(BigDecimal.TEN);
		when(conversionService.convert(formatObject.getSum(), String.class)).thenReturn("10");

		formatTag.setObject(formatObject.getSum());
		formatTag.doEndTag();
		assertEquals("10", tagTestHelper.getResponseContent());
	}

	@Test
	public void testObjectWithProperty() throws JspException
	{
		FormatTag formatTag = new FormatTag();
		TagTestHelper tagTestHelper = new TagTestHelper(formatTag);
		ConversionService conversionService = setupConversionService(tagTestHelper);
		MockFormatObject formatObject = new MockFormatObject(BigDecimal.TEN);
		when(conversionService.convert(any(), any(TypeDescriptor.class), any(TypeDescriptor.class))).thenReturn("10");

		formatTag.setObject(formatObject);
		formatTag.setPropertyName("sum");
		formatTag.doEndTag();
		assertEquals("10", tagTestHelper.getResponseContent());
		ArgumentCaptor<TypeDescriptor> typeDescriptor = ArgumentCaptor.forClass(TypeDescriptor.class);
		verify(conversionService).convert(eq(formatObject.getSum()), typeDescriptor.capture(),
				eq(TypeDescriptor.valueOf(String.class)));
		assertNotNull(typeDescriptor.getValue().getAnnotation(NumberFormat.class));
	}

	private ConversionService setupConversionService(TagTestHelper tagTestHelper)
	{
		ConversionService conversionService = mock(ConversionService.class);
		tagTestHelper.getPageContext().getRequest().setAttribute(ConversionService.class.getName(), conversionService);
		return conversionService;
	}

	@SuppressWarnings("unused")
	private static class MockFormatObject
	{
		private BigDecimal sum;

		public MockFormatObject(BigDecimal sum)
		{
			this.sum = sum;
		}

		@NumberFormat
		public BigDecimal getSum()
		{
			return sum;
		}

		public void setSum(BigDecimal sum)
		{
			this.sum = sum;
		}
	}
}
