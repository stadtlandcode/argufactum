package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockProContraTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.enumerations.TopicThesis;

public class ArgumentTest
{
	@Test
	public void testCreateArgument()
	{
		ProContraTopic topic = mockProContraTopic();
		Author author = mockAuthor();
		String thesis = "12345";
		TopicThesis topicThesis = TopicThesis.PRO;
		Argument argument = new Argument(topic, topicThesis, thesis, author);

		assertEquals(topic, argument.getTopic());
		assertEquals(topicThesis, argument.getTopicThesis());
		assertNotNull(argument.getText());
		assertEquals(author, argument.getArgumentThesis().getAuthor());
		assertNotNull(argument.getFacts());
		assertTrue(argument.getFacts().isEmpty());
		assertNotNull(argument.getCreatedAt());
		assertNull(argument.getId());
	}

	@Test
	public void testEdit()
	{
		String text = "123456";
		Author author = mockAuthor();
		Argument argument = mockArgument();

		boolean executed = argument.editThesis(text, author);
		assertEquals(text, argument.getText());
		assertEquals(author, argument.getArgumentThesis().getAuthor());
		assertTrue(executed);

		assertFalse(argument.editThesis(text, author));
		assertFalse(argument.editThesis(text, mockAuthor()));
	}

	@Test
	public void testCompareTo()
	{
		ProContraTopic topic = mockProContraTopic();
		Author author = mockAuthor(mockUser());
		Date creationDate = new Date();

		Argument argument = new Argument(topic, TopicThesis.PRO, "12345", author);
		ReflectionTestUtils.setField(argument, "createdAt", creationDate);
		Argument argument2 = new Argument(topic, TopicThesis.PRO, "12345", author);
		ReflectionTestUtils.setField(argument2, "createdAt", creationDate);
		Argument argument3 = new Argument(topic, TopicThesis.PRO, "12345", mockAuthor());
		ReflectionTestUtils.setField(argument3, "createdAt", creationDate);
		ReflectionTestUtils.setField(argument3, "id", Long.valueOf(1));

		assertEquals(0, argument.compareTo(argument));
		assertEquals(0, argument.compareTo(argument2));
		assertTrue(argument.compareTo(argument3) < 0);

		new ArgumentRelevanceVote(argument3, Relevance.LOW, author);
		assertTrue(argument.compareTo(argument3) > 0);
	}

	@Test
	public void testCopyTo()
	{
		Argument source = mockArgument();
		source.editThesis("123456", mockAuthor());
		new Fact(source, FactType.CONFIRMATIVE, "12345", mockAuthor(), 0, null);
		Fact firstFact = new Fact(source, FactType.CONFIRMATIVE, "123", mockAuthor(), 1000, null);
		firstFact.editEvidence("abc234", mockAuthor());

		ProContraTopic destTopic = mockProContraTopic();
		Argument dest = source.copyTo(destTopic, 0);
		assertEquals(destTopic, dest.getTopic());
		assertEquals(source.getText(), dest.getText());
		assertNotSame(source.getAuthor(), dest.getAuthor());
		assertNotSame(source.getArgumentThesis(), dest.getArgumentThesis());
		assertFalse(source.getArgumentThesis().isFirst());
		assertTrue(dest.getArgumentThesis().isFirst());
		assertEquals(2, dest.getFacts().size());

		Fact firstFactOnDest = dest.getFacts().iterator().next();
		assertEquals(firstFact.getText(), firstFactOnDest.getText());
		assertEquals(2, firstFactOnDest.getAuthorAttribution().getExternalAuthorList().size());
		firstFactOnDest.editEvidence("abc", mockAuthor());
		firstFactOnDest.editEvidence("abcdef", mockAuthor());

		Argument dest2 = dest.copyTo(destTopic, 10);
		Fact firstFactOnDest2 = dest2.getFacts().iterator().next();
		assertEquals(4, firstFactOnDest2.getAuthorAttribution().getExternalAuthorList().size()); // 3 authors by edit, 1 by creation
	}

	@Test
	public void testGetRelevance()
	{
		Argument argument = mockArgument();
		assertEquals(BigDecimal.ZERO, argument.getNumericRelevance());
		assertNull(argument.getRelevance());

		new ArgumentRelevanceVote(argument, Relevance.HIGH, mockAuthor(mockUser()));
		assertEquals(new BigDecimal("3.00"), argument.getNumericRelevance());
		assertEquals(Relevance.HIGH, argument.getRelevance());

		new ArgumentRelevanceVote(argument, Relevance.HIGH, mockAuthor(mockUser()));
		new ArgumentRelevanceVote(argument, Relevance.AVERAGE, mockAuthor(mockUser()));
		assertEquals(new BigDecimal("2.67"), argument.getNumericRelevance());
		assertEquals(Relevance.HIGH, argument.getRelevance());
	}

	@Test
	public void testOfferAttributionList()
	{
		Argument argument = mockArgument();
		assertFalse(argument.offerAttributionList());

		Author author2 = mockAuthor();
		argument.editThesis("abc", author2);
		assertFalse(argument.offerAttributionList());

		argument.editThesis("123", argument.getAuthor());
		assertTrue(argument.offerAttributionList());
	}
}
