package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.domain.enumerations.TopicThesis;

public class ProContraXmlServiceTest
{
	@Test
	public void testParseMainNodes() throws Exception
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new File("src/test/resources/initialData/proContraTopics.xml"));
		NodeList userNodes = document.getElementsByTagName("proContraTopic");
		RegisteredUser userEdel = RegisteredUser.register("edel", "edel@argufactum.de", "123456", null);
		RegisteredUser userFelix = RegisteredUser.register("felix", "felix@argufactum.de", "123456", null);
		UserDao userDao = mock(UserDao.class);
		when(userDao.findByUsername("edel")).thenReturn(userEdel);
		when(userDao.findByUsername("felix")).thenReturn(userFelix);
		CategoryDao categoryDao = mock(CategoryDao.class);
		when(categoryDao.findByName("Politik")).thenReturn(new Category("Politik"));
		ProContraXmlService service = new ProContraXmlService(mock(TopicDao.class), mock(ProContraTopicDao.class),
				userDao, categoryDao, null, null);

		Set<ProContraTopic> topics = service.parseMainNodes(userNodes);
		assertEquals(2, topics.size());

		ProContraTopic proContra = getTopic("Allgemeiner gesetzlicher Mindestlohn", topics);
		assertNotNull(proContra);
		assertEquals("8,50 Euro pro Stunde", proContra.getTopic().getTerm().getDefinition());
		assertEquals(TopicThesesMessageCodeAppendix.PRO_CONTRA, proContra.getThesesMessageCodeAppendix());
		assertEquals(userEdel.getUsername(), proContra.getTopic().getAuthorName());
		assertEquals(userEdel, proContra.getTopic().getAuthor().getRegisteredUser());
		assertNotNull(proContra.getTopic().getCategory());
		assertEquals("Politik", proContra.getTopic().getCategory().getName());
		assertEquals(3, proContra.getArguments().size());

		Argument argument = new TreeSet<Argument>(proContra.getArguments()).iterator().next();
		assertEquals("Verhindert (weiteres) Lohndumping", argument.getText());
		assertEquals(TopicThesis.PRO, argument.getTopicThesis());
		assertEquals(2, argument.getFacts().size());

		Fact fact = new TreeSet<Fact>(argument.getFacts()).iterator().next();
		assertEquals(
				"Niedrige Löhne bei Wettbewerbern bewirken Lohndumping in der gesamten Branche, um Konkurrenzfähig zu bleiben",
				fact.getText());
		assertEquals(FactType.CONFIRMATIVE, fact.getFactType());
		assertEquals(2, fact.getReferences().size());

		Reference reference = getReference("mindestlohn.de", fact.getReferences());
		assertNotNull(reference);
		assertEquals("http://www.mindestlohn.de", reference.getUrl());
	}

	private Reference getReference(String text, Set<Reference> references)
	{
		for (Reference reference : references)
		{
			if (reference.getText().equals(text))
				return reference;
		}
		return null;
	}

	private ProContraTopic getTopic(String term, Set<ProContraTopic> topics)
	{
		for (ProContraTopic proContraTopic : topics)
		{
			if (proContraTopic.getTopic().getText().equals(term))
				return proContraTopic;
		}
		return null;
	}
}
