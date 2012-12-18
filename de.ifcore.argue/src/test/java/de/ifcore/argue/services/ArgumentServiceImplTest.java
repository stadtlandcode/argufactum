package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockProContraTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.ArgumentRelevanceVoteDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentRelevanceVote;
import de.ifcore.argue.domain.entities.ArgumentThesis;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.LogEvent;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.form.ArgumentRelevanceVoteForm;
import de.ifcore.argue.domain.form.CreateArgumentForm;
import de.ifcore.argue.domain.form.EditArgumentForm;
import de.ifcore.argue.domain.report.EventReport;

public class ArgumentServiceImplTest
{
	@Test
	public void testCreate()
	{
		ProContraTopic topic = mockProContraTopic();
		CreateArgumentForm form = new CreateArgumentForm();
		form.setThesis("12345");
		form.setTopicThesis(TopicThesis.PRO);
		form.setTopicId(2l);
		Author author = mockAuthor();

		ProContraTopicDao proContraTopicDao = mock(ProContraTopicDao.class);
		when(proContraTopicDao.get(form.getTopicId())).thenReturn(topic);
		ArgumentDao argumentDao = mock(ArgumentDao.class);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);
		ArgumentService service = new ArgumentServiceImpl(argumentDao, proContraTopicDao, accessRightService, null);
		Argument argument = service.create(form, author);

		assertEquals(topic, argument.getTopic());
		assertEquals(form.getThesis(), argument.getArgumentThesis().getText());
		assertEquals(form.getTopicThesis(), argument.getTopicThesis());
		assertEquals(author, argument.getArgumentThesis().getAuthor());
		verify(argumentDao).save(argument);
	}

	@Test
	public void testEdit()
	{
		EditArgumentForm form = new EditArgumentForm();
		form.setText("12345");
		form.setId(1l);
		Argument argument = mockArgument();
		Author author = mockAuthor();

		ArgumentDao argumentDao = mock(ArgumentDao.class);
		when(argumentDao.get(form.getId())).thenReturn(argument);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);
		ArgumentService service = new ArgumentServiceImpl(argumentDao, null, accessRightService, null);
		service.edit(form, author);

		assertEquals(form.getText(), argument.getArgumentThesis().getText());
		assertEquals(author, argument.getArgumentThesis().getAuthor());
		verify(argumentDao, times(1)).saveThesis(argument.getArgumentThesis());
		verify(argumentDao, times(1)).update(argument);

		service.edit(form, author);
		verify(argumentDao, times(1)).saveThesis(argument.getArgumentThesis());
		verify(argumentDao, times(1)).update(argument);
	}

	@Test
	public void testPersistRelevanceVote()
	{
		Argument argument = mockArgument();
		ArgumentRelevanceVoteForm form = new ArgumentRelevanceVoteForm();
		form.setId(1l);
		form.setRelevance(Relevance.LOW);
		RegisteredUser user = mockUser();
		ReflectionTestUtils.setField(user, "id", Long.valueOf(10));
		Author author = mockAuthor(user);

		ArgumentDao argumentDao = mock(ArgumentDao.class);
		when(argumentDao.get(form.getId())).thenReturn(argument);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);
		ArgumentRelevanceVoteDao relevanceVoteDao = mock(ArgumentRelevanceVoteDao.class);
		ArgumentService argumentService = new ArgumentServiceImpl(argumentDao, null, accessRightService,
				relevanceVoteDao);

		ArgumentRelevanceVote relevanceVote = argumentService.persistRelevanceVote(form, author);
		assertFalse(argument.getRelevanceVotes().isEmpty());
		assertEquals(1, argument.getRelevanceVotes().size());
		assertEquals(author, relevanceVote.getAuthor());
		assertEquals(form.getRelevance(), relevanceVote.getRelevance());
		verify(relevanceVoteDao).save(relevanceVote);

		form.setRelevance(Relevance.HIGH);
		relevanceVote = argumentService.persistRelevanceVote(form, author);
		assertEquals(1, argument.getRelevanceVotes().size());
		assertEquals(form.getRelevance(), relevanceVote.getRelevance());
		verify(relevanceVoteDao).update(relevanceVote);

		form.setRelevance(null);
		assertNull(argumentService.persistRelevanceVote(form, author));
		assertTrue(argument.getRelevanceVotes().isEmpty());
		verify(relevanceVoteDao).delete(relevanceVote);

		argumentService.persistRelevanceVote(form, author);
	}

	@Test
	public void testGetEventReports()
	{
		Argument argument = mockArgument();
		ReflectionTestUtils.setField(argument, "id", Long.valueOf(1l));
		ArgumentThesis firstThesis = argument.getArgumentThesis();
		Author author = mockAuthor();
		argument.editThesis("12345", author);
		ArgumentThesis secondThesis = argument.getArgumentThesis();
		ReflectionTestUtils.setField(firstThesis, "createdAt", new Date(System.currentTimeMillis() - 20000));
		ReflectionTestUtils.setField(argument.getArgumentThesis(), "createdAt", new Date(
				System.currentTimeMillis() - 10000));
		argument.delete(author);

		ArgumentDao argumentDao = mock(ArgumentDao.class);
		when(argumentDao.get(argument.getId())).thenReturn(argument);
		when(argumentDao.getTheses(argument.getId())).thenReturn(Arrays.asList(firstThesis, secondThesis));
		ArgumentService argumentService = new ArgumentServiceImpl(argumentDao, null,
				mock(TopicAccessRightService.class), null);

		Set<EventReport> eventReports = argumentService.getEventReports(argument.getId(), author);
		assertEquals(3, eventReports.size());

		Iterator<EventReport> iterator = eventReports.iterator();
		EventReport eventReport = iterator.next();
		assertEquals(LogEvent.DELETE, eventReport.getEvent());
		assertNull(eventReport.getText());

		EventReport eventReport2 = iterator.next();
		assertEquals(LogEvent.UPDATE, eventReport2.getEvent());
		assertEquals("12345", eventReport2.getText());
	}
}
