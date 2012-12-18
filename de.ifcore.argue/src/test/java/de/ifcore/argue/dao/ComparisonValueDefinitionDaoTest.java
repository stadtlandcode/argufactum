package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockDefinitionSet;
import static de.ifcore.argue.utils.EntityTestUtils.mockGlobalDefinitionSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.ComparisonValueDefinition;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.enumerations.SortOrder;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ComparisonValueDefinitionDaoTest extends IntegrationTest
{
	@Test
	public void testSaveSet()
	{
		ComparisonValueDefinitionSet definitionSet = new ComparisonValueDefinitionSet(CriterionDataType.RATING,
				"von sehr leise bis sehr laut", mockAuthor());
		ComparisonValueDefinition definition1 = new ComparisonValueDefinition(definitionSet, "sehr leise", (byte)100);
		ComparisonValueDefinition definition2 = new ComparisonValueDefinition(definitionSet, "leise", (byte)75);
		comparisonValueDefinitionDao.saveSet(definitionSet);
		flushAndClear();

		ComparisonValueDefinitionSet persistedDefinitionSet = comparisonValueDefinitionDao
				.getSet(definitionSet.getId());
		assertEquals(definitionSet, persistedDefinitionSet);
		assertEquals(2, persistedDefinitionSet.getDefinitions().size());
		assertTrue(persistedDefinitionSet.getDefinitions().contains(definition1));
		assertTrue(persistedDefinitionSet.getDefinitions().contains(definition2));
	}

	@Test
	public void testSaveNumberSet()
	{
		ComparisonValueDefinitionSet definitionSet = new ComparisonValueDefinitionSet(CriterionDataType.NUMBER,
				"Standard", mockAuthor());
		definitionSet.setNumberDefinition(new BigDecimal(0), new BigDecimal(100), SortOrder.ASC);
		comparisonValueDefinitionDao.saveSet(definitionSet);
		flushAndClear();

		ComparisonValueDefinitionSet persistedDefinitionSet = comparisonValueDefinitionDao
				.getSet(definitionSet.getId());
		assertEquals(definitionSet, persistedDefinitionSet);
	}

	@Test
	public void testGetGlobalSets()
	{
		persistDefinitionSet(mockGlobalDefinitionSet());
		persistDefinitionSet(mockGlobalDefinitionSet());
		persistDefinitionSet(mockDefinitionSet());
		flushAndClear();

		List<ComparisonValueDefinitionSet> definitionSets = comparisonValueDefinitionDao.getGlobalSets();
		assertEquals(2, definitionSets.size());
		for (ComparisonValueDefinitionSet definitionSet : definitionSets)
			assertTrue(definitionSet.isGlobal());
	}
}
