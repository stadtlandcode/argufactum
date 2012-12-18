package de.ifcore.argue.domain.entities;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.domain.enumerations.CriterionDataType;

public class ComparisonValueDefinitionSetTest
{
	@Test
	public void testCompareTo()
	{
		// without sortkey
		ComparisonValueDefinitionSet set1 = new ComparisonValueDefinitionSet(CriterionDataType.NUMBER, "XYZ", null);
		ComparisonValueDefinitionSet set2 = new ComparisonValueDefinitionSet(CriterionDataType.NUMBER, "ABC", null);
		long now = System.currentTimeMillis();
		ReflectionTestUtils.setField(set1, "createdAt", new Date(now - 1000));
		ReflectionTestUtils.setField(set2, "createdAt", new Date(now));

		TreeSet<ComparisonValueDefinitionSet> treeSet = new TreeSet<>(Arrays.asList(set2, set1));
		assertEquals(set1, treeSet.first());
		assertEquals(set2, treeSet.last());

		// with sortkey
		ComparisonValueDefinitionSet set10 = ComparisonValueDefinitionSet.maintainedSet(CriterionDataType.BOOLEAN,
				null, "DEF", 100, true);
		ComparisonValueDefinitionSet set11 = ComparisonValueDefinitionSet.maintainedSet(CriterionDataType.BOOLEAN,
				null, "EFG", 130, true);
		ComparisonValueDefinitionSet set12 = ComparisonValueDefinitionSet.maintainedSet(CriterionDataType.BOOLEAN,
				null, "FGH", 130, true);
		ReflectionTestUtils.setField(set12, "createdAt", new Date(now - 10000000));

		treeSet = new TreeSet<>(Arrays.asList(set12, set10, set11));
		assertEquals(set12, treeSet.first());
		assertEquals(set10, treeSet.last());
	}
}
