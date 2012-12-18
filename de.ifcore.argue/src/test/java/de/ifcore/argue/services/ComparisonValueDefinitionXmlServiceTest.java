package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.domain.entities.ComparisonValueDefinition;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.enumerations.ComparisonValueInputMethod;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.enumerations.SortOrder;

public class ComparisonValueDefinitionXmlServiceTest
{
	@Test
	public void testParseMainNodes() throws Exception
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new File("src/test/resources/initialData/comparisonValueDefinitions.xml"));
		NodeList definitionSetNodes = document.getElementsByTagName("definitionSet");
		ComparisonValueDefinitionXmlService service = new ComparisonValueDefinitionXmlService(
				mock(ComparisonValueDefinitionDao.class), null, null);

		Set<ComparisonValueDefinitionSet> definitionSets = service.parseMainNodes(definitionSetNodes);
		assertEquals(3, definitionSets.size());

		ComparisonValueDefinitionSet definitionSet1 = getDefinitionSet("0-5 Sterne", definitionSets);
		assertNotNull(definitionSet1);
		assertEquals(CriterionDataType.RATING, definitionSet1.getDataType());
		assertEquals(Integer.valueOf(90), definitionSet1.getSortKey());
		assertEquals(ComparisonValueInputMethod.STARS, definitionSet1.getInputMethod());
		assertTrue(definitionSet1.isGlobal());
		assertEquals(6, definitionSet1.getDefinitions().size());

		ComparisonValueDefinition definition1 = getDefinition("1", definitionSet1.getDefinitions());
		assertNotNull(definition1);
		assertEquals(Byte.valueOf((byte)20), definition1.getPercent());

		ComparisonValueDefinitionSet definitionSet2 = getDefinitionSet("Freitext", definitionSets);
		assertNotNull(definitionSet2);
		assertTrue(definitionSet2.getDefinitions().isEmpty());

		ComparisonValueDefinitionSet definitionSet3 = getDefinitionSet("Standard", definitionSets);
		assertNotNull(definitionSet3);
		assertEquals(new BigDecimal(0), definitionSet3.getNumberDefinition().getMin());
		assertNull(definitionSet3.getNumberDefinition().getMax());
		assertEquals(SortOrder.DESC, definitionSet3.getNumberDefinition().getSortOrder());
	}

	private ComparisonValueDefinition getDefinition(String string, Set<ComparisonValueDefinition> definitions)
	{
		for (ComparisonValueDefinition definition : definitions)
		{
			if (string.equals(definition.getString()))
				return definition;
		}
		return null;
	}

	private ComparisonValueDefinitionSet getDefinitionSet(String name, Set<ComparisonValueDefinitionSet> definitionSets)
	{
		for (ComparisonValueDefinitionSet definitionSet : definitionSets)
		{
			if (name.equals(definitionSet.getName()))
				return definitionSet;
		}
		return null;
	}
}
