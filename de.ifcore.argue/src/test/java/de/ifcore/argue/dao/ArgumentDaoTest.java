package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockProContraTopic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ArgumentThesis;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArgumentDaoTest extends IntegrationTest
{
	@Test
	public void testSaveArgument()
	{
		ProContraTopic topic = persistProContraTopic(mockProContraTopic());

		Argument argument = new Argument(topic, TopicThesis.PRO, "12345", mockAuthor());
		ArgumentThesis thesis = new ArgumentThesis("test123", mockAuthor(), argument);
		argument.setArgumentThesis(thesis);
		argumentDao.save(argument);
		flushAndClear();

		Argument persistedArgument = argumentDao.get(argument.getId());
		assertEquals(argument, persistedArgument);
		assertEquals(thesis, persistedArgument.getArgumentThesis());
	}

	@Test
	public void testUpdateArgument()
	{
		Argument argument = persistArgument(mockArgument());
		flushAndClear();

		argument.editThesis("test123", mockAuthor());
		argumentDao.saveThesis(argument.getArgumentThesis());
		argumentDao.update(argument);
		flushAndClear();

		Argument persistedArgument = argumentDao.get(argument.getId());
		assertEquals(argument.getArgumentThesis(), persistedArgument.getArgumentThesis());
	}

	@Test
	public void testDeleteArgument()
	{
		Argument argument = persistArgument(mockArgument());
		flushAndClear();
		assertEquals(0, argument.getLogs().size());
		assertFalse(argument.isDeleted());
		argument.delete(mockAuthor());
		argumentDao.update(argument);
		flushAndClear();

		Argument persistedArgument = argumentDao.get(argument.getId());
		assertTrue(persistedArgument.isDeleted());
		assertEquals(1, persistedArgument.getLogs().size());
	}

	@Test
	public void testSaveThesis()
	{
		Argument argument = persistArgument(mockArgument());
		ArgumentThesis thesis = new ArgumentThesis("12345", mockAuthor(), argument);
		argumentDao.saveThesis(thesis);
		flushAndClear();

		ArgumentThesis persistedThesis = argumentDao.getThesis(thesis.getId());
		assertEquals(thesis.getArgument(), persistedThesis.getArgument());
		assertEquals(thesis.getAuthor(), persistedThesis.getAuthor());
		assertEquals(thesis.getCreatedAt(), persistedThesis.getCreatedAt());
		assertEquals(thesis.getText(), persistedThesis.getText());
	}

	@Test
	public void testGetTheses()
	{
		Argument argument = persistArgument(mockArgument());
		flushAndClear();
		argument.editThesis("12345", mockAuthor());
		argumentDao.saveThesis(argument.getArgumentThesis());
		argumentDao.update(argument);
		flushAndClear();

		List<ArgumentThesis> theses = argumentDao.getTheses(argument.getId());
		assertEquals(2, theses.size());
	}
}
