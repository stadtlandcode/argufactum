package de.ifcore.argue.domain.entities;

import static de.ifcore.argue.utils.EntityTestUtils.mockDefinitionSet;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Test;

public class ComparisonValueDefinitionTest
{
	@Test
	public void testCompareTo()
	{
		ComparisonValueDefinitionSet colorSet = mockDefinitionSet();
		ComparisonValueDefinition dc1 = new ComparisonValueDefinition(colorSet, "rot", (byte)100);
		ComparisonValueDefinition dc2 = new ComparisonValueDefinition(colorSet, "weiss", (byte)50);
		ComparisonValueDefinition dc3 = new ComparisonValueDefinition(colorSet, "pink", (byte)100);
		TreeSet<ComparisonValueDefinition> treeSet = new TreeSet<>(Arrays.asList(dc2, dc3, dc1));
		assertEquals(dc3, treeSet.first());
		assertEquals(dc2, treeSet.last());

		ComparisonValueDefinitionSet ratingSet = mockDefinitionSet();
		ComparisonValueDefinition dr1 = new ComparisonValueDefinition(ratingSet, "sehr gut", (byte)100);
		ComparisonValueDefinition dr2 = new ComparisonValueDefinition(ratingSet, "mittel", (byte)50);
		treeSet = new TreeSet<>(Arrays.asList(dr2, dr1));
		assertEquals(dr1, treeSet.first());
		assertEquals(dr2, treeSet.last());
	}
}
