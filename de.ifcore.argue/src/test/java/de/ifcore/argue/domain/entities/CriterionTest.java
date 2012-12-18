package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockCriterion;
import static de.ifcore.argue.utils.EntityTestUtils.mockDefinitionSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.enumerations.CriterionDataType;

public class CriterionTest
{
	@Test
	public void testEdit()
	{
		Criterion criterion = mockCriterion();
		String label = "123456";
		ComparisonValueDefinitionSet definitionSet1 = mockDefinitionSet(CriterionDataType.NUMBER);
		ComparisonValueDefinitionSet definitionSet2 = mockDefinitionSet(CriterionDataType.RATING);

		boolean executed = criterion.edit(label, definitionSet1);
		assertEquals(label, criterion.getLabel());
		assertEquals(definitionSet1, criterion.getDefinitionSet());
		assertTrue(executed);

		assertFalse(criterion.edit(label, definitionSet1));
		assertFalse(criterion.edit(label, null));
		assertFalse(criterion.edit(null, definitionSet1));
		assertFalse(criterion.edit(" ", null));
		assertFalse(criterion.edit(null, null));
		assertNotNull(criterion.getDefinitionSet());
		assertNotNull(criterion.getLabel());

		assertTrue(criterion.edit("654321", null));
		assertNotNull(criterion.getDefinitionSet());
		assertTrue(criterion.edit(null, definitionSet2));
		assertNotNull(criterion.getLabel());
		assertTrue(criterion.edit("7654321", definitionSet2));
	}

	@Test
	public void testCompareTo()
	{
		ComparisonTopic comparisonTopic = mockComparisonTopic();
		Author author = mockAuthor();
		Date creationDate = new Date();
		Date creationDate2 = new Date(1000);
		ComparisonValueDefinitionSet definitionSet = mockDefinitionSet();

		Criterion criterion1 = new Criterion(comparisonTopic, "123", definitionSet, author);
		Criterion criterion2 = new Criterion(comparisonTopic, "123", definitionSet, author);
		Criterion criterion3 = new Criterion(comparisonTopic, "123", definitionSet, mockAuthor());
		Criterion criterion4 = new Criterion(comparisonTopic, "123", definitionSet, author);
		Criterion criterion5 = new Criterion(mockComparisonTopic(), "123", definitionSet, author);
		ReflectionTestUtils.setField(criterion1, "createdAt", creationDate);
		ReflectionTestUtils.setField(criterion2, "createdAt", creationDate);
		ReflectionTestUtils.setField(criterion3, "createdAt", creationDate);
		ReflectionTestUtils.setField(criterion3, "id", Long.valueOf(1));
		ReflectionTestUtils.setField(criterion4, "createdAt", creationDate2);
		ReflectionTestUtils.setField(criterion5, "createdAt", creationDate);

		assertEquals(0, criterion1.compareTo(criterion1));
		assertEquals(0, criterion1.compareTo(criterion2));
		assertTrue(criterion1.compareTo(criterion3) < 0);
		assertTrue(criterion1.compareTo(criterion4) > 0);
		assertEquals(0, criterion1.compareTo(criterion5));
	}

}
