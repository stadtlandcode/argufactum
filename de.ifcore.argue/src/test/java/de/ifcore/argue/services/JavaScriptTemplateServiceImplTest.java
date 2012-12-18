package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import de.ifcore.argue.domain.models.view.CreateArgumentModel;

public class JavaScriptTemplateServiceImplTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTemplateMap()
	{
		JavaScriptTemplateService service = new JavaScriptTemplateServiceImpl();
		Map<String, Object> templateMap = service.getTemplateMap(CreateArgumentModel.class);
		assertTrue(templateMap.containsKey("textId"));
		assertFalse(templateMap.containsKey("class"));
		assertTrue(templateMap.containsKey("modificationLog"));
		assertTrue(templateMap.get("modificationLog") instanceof Map);
		assertEquals("{{= modificationLog.userId}}",
				((Map<String, Object>)templateMap.get("modificationLog")).get("userId"));
	}
}
