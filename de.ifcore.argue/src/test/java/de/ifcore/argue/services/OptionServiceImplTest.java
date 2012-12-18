package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockOption;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.CreateOptionForm;
import de.ifcore.argue.domain.form.DeleteOptionForm;
import de.ifcore.argue.domain.form.EditOptionForm;

public class OptionServiceImplTest
{
	@Test
	public void testCreate()
	{
		Author author = mockAuthor();
		ComparisonTopic topic = mockComparisonTopic();
		CreateOptionForm form = new CreateOptionForm();
		form.setLabel("test");
		form.setTopicId(1l);

		ComparisonTopicDao comparisonTopicDao = mock(ComparisonTopicDao.class);
		OptionDao optionDao = mock(OptionDao.class);
		when(comparisonTopicDao.get(form.getTopicId())).thenReturn(topic);
		OptionService optionService = new OptionServiceImpl(comparisonTopicDao, optionDao);
		Option option = optionService.create(form, author);

		assertEquals(form.getLabel(), option.getLabel());
		assertEquals(topic, option.getTopic());
		assertNotNull(option.getCreatedAt());
		assertEquals(author, option.getAuthor());
		verify(optionDao).save(option);
	}

	@Test
	public void testDelete()
	{
		Author author = mockAuthor();
		Option option = new Option(mockComparisonTopic(), "test", author);
		DeleteOptionForm form = new DeleteOptionForm();
		form.setId(1l);

		OptionDao optionDao = mock(OptionDao.class);
		when(optionDao.get(form.getId())).thenReturn(option);
		OptionService optionService = new OptionServiceImpl(null, optionDao);

		optionService.delete(form, author);
		assertTrue(option.isDeleted());
		verify(optionDao).update(option);
	}

	@Test
	public void testEdit()
	{
		Option option = mockOption();
		EditOptionForm form = new EditOptionForm();
		form.setLabel("123456");
		form.setId(1l);

		OptionDao optionDao = mock(OptionDao.class);
		when(optionDao.get(form.getId())).thenReturn(option);
		OptionService optionService = new OptionServiceImpl(null, optionDao);
		optionService.edit(form, mockAuthor());

		assertEquals(form.getLabel(), option.getLabel());
		verify(optionDao).get(form.getId());
		verify(optionDao, times(1)).update(option);

		optionService.edit(form, mockAuthor());
		verify(optionDao, times(1)).update(option);
	}
}
