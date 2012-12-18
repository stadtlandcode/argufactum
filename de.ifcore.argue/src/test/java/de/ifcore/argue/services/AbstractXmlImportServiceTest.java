package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.w3c.dom.Element;

import de.ifcore.argue.domain.entities.Topic;

public class AbstractXmlImportServiceTest
{
	@Test
	public void testGetStartupFiles() throws Exception
	{
		MockXmlImportService service = new MockXmlImportService();
		Set<File> startupFiles = service.getStartupFiles("initialData/categories.xml", "initialData");
		assertEquals(4, startupFiles.size());

		Set<String> fileNames = new HashSet<>();
		for (File file : startupFiles)
			fileNames.add(file.getName());

		assertTrue(fileNames.contains("categories.xml"));
		assertTrue(fileNames.contains("proContraTopics.xml"));
		assertTrue(fileNames.contains("users.xml"));
		assertTrue(fileNames.contains("categories.xml"));
	}

	private static class MockXmlImportService extends AbstractXmlImportService<Topic>
	{
		public MockXmlImportService()
		{
			super(null, null, null, null);
		}

		@Override
		public void importFile(File xmlFilename)
		{
		}

		@Override
		protected void saveObjects(Set<Topic> parsedObjects)
		{
		}

		@Override
		protected Topic parseMainNode(Element element)
		{
			return null;
		}

		@Override
		public boolean checkPreconditions()
		{
			return true;
		}
	}
}
