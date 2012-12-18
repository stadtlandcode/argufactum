package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProContraTopicDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		Topic topic = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, mockAuthor(persistUser(mockUser())),
				TopicVisibility.PRIVATE, null);
		ProContraTopic proContraTopic = new ProContraTopic(topic);
		proContraTopicDao.save(proContraTopic);
		flushAndClear();

		ProContraTopic persistedProContraTopic = proContraTopicDao.get(proContraTopic.getId());
		assertEquals(proContraTopic, persistedProContraTopic);
		assertEquals(topic, persistedProContraTopic.getTopic());
		assertNotNull(persistedProContraTopic.getTopic().getProContraTopic());
	}

	@Test
	public void testSaveWithDescendants()
	{
		Topic topic = new Topic("testTopic", null, DiscussionType.PRO_CONTRA, mockAuthor(persistUser(mockUser())),
				TopicVisibility.PRIVATE, null);
		ProContraTopic proContraTopic = new ProContraTopic(topic);
		Argument argument = new Argument(proContraTopic, TopicThesis.PRO, "12345", mockAuthor());
		Fact fact = new Fact(argument, FactType.CONFIRMATIVE, "12345", mockAuthor());
		Reference reference = Reference.instanceOf("12345", fact, mockAuthor());
		proContraTopicDao.saveWithDescendants(proContraTopic);
		flushAndClear();

		ProContraTopic persistedProContraTopic = proContraTopicDao.get(proContraTopic.getId());
		assertEquals(proContraTopic, persistedProContraTopic);
		assertEquals(topic, persistedProContraTopic.getTopic());

		assertEquals(1, persistedProContraTopic.getArguments().size());
		Argument persistedArgument = persistedProContraTopic.getArguments().iterator().next();
		assertEquals(argument, persistedArgument);

		assertEquals(1, persistedArgument.getFacts().size());
		Fact persistedFact = persistedArgument.getFacts().iterator().next();
		assertEquals(fact, persistedFact);

		assertEquals(1, persistedFact.getReferences().size());
		Reference persistedReference = persistedFact.getReferences().iterator().next();
		assertEquals(reference, persistedReference);
	}
}
