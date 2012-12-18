package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockCriterion;
import static de.ifcore.argue.utils.EntityTestUtils.mockDefinitionSet;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.CriterionImportance;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class CriterionDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		ComparisonTopic topic = persistComparisonTopic(mockComparisonTopic());
		ComparisonValueDefinitionSet definitionSet = persistDefinitionSet(mockDefinitionSet());
		Criterion criterion = new Criterion(topic, "12345", definitionSet, mockAuthor());
		criterionDao.save(criterion);
		flushAndClear();

		Criterion persistedCriterion = criterionDao.get(criterion.getId());
		assertEquals(criterion, persistedCriterion);
	}

	@Test
	public void testUpdate()
	{
		Criterion criterion = persistCriterion(mockCriterion());
		ComparisonValueDefinitionSet definitionSet = persistDefinitionSet(mockDefinitionSet());
		flushAndClear();

		criterion.edit("123546", definitionSet);
		criterionDao.update(criterion);
		flushAndClear();

		Criterion persistedCriterion = criterionDao.get(criterion.getId());
		assertEquals(criterion.getLabel(), persistedCriterion.getLabel());
		assertEquals(criterion.getDefinitionSet(), persistedCriterion.getDefinitionSet());
		assertEquals(criterion, persistedCriterion);
	}

	@Test
	public void testSaveImportance()
	{
		Criterion criterion = persistCriterion(mockCriterion());
		CriterionImportance importance = new CriterionImportance(criterion, (byte)50,
				mockAuthor(persistUser(mockUser())));
		criterionDao.saveImportance(importance);
		flushAndClear();

		CriterionImportance persistedImportance = criterionDao.getImportance(importance.getId());
		assertEquals(importance, persistedImportance);
	}
}
