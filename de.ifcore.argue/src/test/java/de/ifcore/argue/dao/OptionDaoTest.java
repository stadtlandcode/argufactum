package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class OptionDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		ComparisonTopic topic = persistComparisonTopic(mockComparisonTopic());
		Option option = new Option(topic, "12345", mockAuthor());
		optionDao.save(option);
		flushAndClear();

		Option persistedOption = optionDao.get(option.getId());
		assertEquals(option, persistedOption);
	}
}
