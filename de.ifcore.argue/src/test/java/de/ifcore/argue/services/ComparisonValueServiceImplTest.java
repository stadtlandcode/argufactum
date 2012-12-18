package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockCriterion;
import static de.ifcore.argue.utils.EntityTestUtils.mockOption;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.ComparisonValueDao;
import de.ifcore.argue.dao.CriterionDao;
import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.ComparisonValueForm;

public class ComparisonValueServiceImplTest
{
	@Test
	public void testPersist()
	{
		Author author = mockAuthor();
		ComparisonTopic comparisonTopic = mockComparisonTopic();
		Criterion criterion = mockCriterion(comparisonTopic);
		Option option = mockOption(comparisonTopic);
		ComparisonValueForm form = new ComparisonValueForm();
		form.setOptionId(1l);
		form.setCriterionId(2l);
		form.setString("10");

		ComparisonValueDao valueDao = mock(ComparisonValueDao.class);
		CriterionDao criterionDao = mock(CriterionDao.class);
		when(criterionDao.get(form.getCriterionId())).thenReturn(criterion);
		OptionDao optionDao = mock(OptionDao.class);
		when(optionDao.get(form.getOptionId())).thenReturn(option);
		ComparisonValueService valueService = new ComparisonValueServiceImpl(valueDao, criterionDao, optionDao);

		ComparisonValue value = valueService.persist(form, author);
		assertNotNull(value.getCriterion());
		assertNotNull(value.getOption());
		assertEquals(form.getString(), value.getString());
		assertEquals(author, value.getAuthor());
		assertNotNull(value.getCreatedAt());
		assertNull(value.getUpdatedAt());
		verify(valueDao).save(value);

		when(valueDao.get(form.getCriterionId(), form.getOptionId())).thenReturn(value);
		form.setString("11");
		ComparisonValue value2 = valueService.persist(form, author);
		assertEquals(form.getString(), value2.getString());
		assertNotNull(value2.getUpdatedAt());
		verify(valueDao).update(value2);
	}
}
