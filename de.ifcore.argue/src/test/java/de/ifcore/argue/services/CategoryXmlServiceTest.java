package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.domain.entities.Category;

public class CategoryXmlServiceTest
{
	@Test
	public void testParseMainNodes() throws Exception
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new File("src/test/resources/initialData/categories.xml"));
		NodeList categoryNodes = document.getElementsByTagName("category");
		CategoryXmlService service = new CategoryXmlService(mock(CategoryDao.class), null, null);

		Set<Category> categories = service.parseMainNodes(categoryNodes);
		assertEquals(3, categories.size());
		assertTrue(categories.contains(new Category("Politik")));
	}
}
