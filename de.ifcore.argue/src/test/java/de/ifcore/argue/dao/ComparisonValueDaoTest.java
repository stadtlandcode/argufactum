package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockCriterion;
import static de.ifcore.argue.utils.EntityTestUtils.mockOption;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ComparisonValueDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		ComparisonTopic topic = mockComparisonTopic();
		Criterion criterion = persistCriterion(mockCriterion(topic));
		Option option = persistOption(mockOption(topic));

		ComparisonValue value = new ComparisonValue(criterion, option, "1", mockAuthor());
		comparisonValueDao.save(value);
		flushAndClear();

		ComparisonValue persistedValue = comparisonValueDao.get(criterion.getId(), option.getId());
		assertEquals(value, persistedValue);
	}
}
