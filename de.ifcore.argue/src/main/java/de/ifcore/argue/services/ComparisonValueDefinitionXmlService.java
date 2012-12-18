package de.ifcore.argue.services;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.domain.entities.ComparisonValueDefinition;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.enumerations.ComparisonValueInputMethod;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.enumerations.SortOrder;

public class ComparisonValueDefinitionXmlService extends AbstractXmlImportService<ComparisonValueDefinitionSet>
{
	private final ComparisonValueDefinitionDao definitionDao;

	@Inject
	public ComparisonValueDefinitionXmlService(ComparisonValueDefinitionDao definitionDao,
			PlatformTransactionManager transactionManager, String startupFile)
	{
		super(transactionManager, startupFile, null, "definitionSet");
		if (definitionDao == null)
			throw new IllegalArgumentException();
		this.definitionDao = definitionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkPreconditions()
	{
		return definitionDao.getGlobalSets().isEmpty();
	}

	@Override
	@Transactional
	protected void saveObjects(Set<ComparisonValueDefinitionSet> parsedObjects)
	{
		for (ComparisonValueDefinitionSet definitionSet : parsedObjects)
			definitionDao.saveSet(definitionSet);
	}

	@Override
	protected ComparisonValueDefinitionSet parseMainNode(Element element)
	{
		String name = getTagValue("name", element);
		CriterionDataType dataType = CriterionDataType.valueOf(getTagValue("dataType", element));
		Integer sortKey = Integer.valueOf(getTagValue("sortKey", element));
		ComparisonValueInputMethod inputMethod = ComparisonValueInputMethod
				.valueOf(getTagValue("inputMethod", element));
		boolean global = Boolean.parseBoolean(getTagValue("global", element));

		ComparisonValueDefinitionSet definitionSet = ComparisonValueDefinitionSet.maintainedSet(dataType, inputMethod,
				name, sortKey, global);
		parseDefinitions(element.getElementsByTagName("definition"), definitionSet);
		parseNumberDefinition(element.getElementsByTagName("numberDefinition"), definitionSet);

		return definitionSet;
	}

	void parseNumberDefinition(NodeList numberDefinitionNodes, ComparisonValueDefinitionSet definitionSet)
	{
		if (numberDefinitionNodes != null && numberDefinitionNodes.getLength() > 0)
		{
			if (numberDefinitionNodes.getLength() > 1)
				throw new IllegalStateException();

			Element element = (Element)numberDefinitionNodes.item(0);
			String maxString = getTagValue("max", element);
			BigDecimal max = "max".equals(maxString) ? null : new BigDecimal(maxString);
			String minString = getTagValue("min", element);
			BigDecimal min = "min".equals(minString) ? null : new BigDecimal(minString);
			SortOrder sortOrder = SortOrder.valueOf(getTagValue("sortOrder", element));
			definitionSet.setNumberDefinition(min, max, sortOrder);
		}
	}

	void parseDefinitions(NodeList definitionNodes, ComparisonValueDefinitionSet definitionSet)
	{
		if (definitionNodes != null && definitionNodes.getLength() > 0)
		{
			for (int i = 0; i < definitionNodes.getLength(); i++)
			{
				Element element = (Element)definitionNodes.item(i);
				String value = getTagValue("string", element);
				Byte percent = Byte.valueOf(getTagValue("percent", element));

				new ComparisonValueDefinition(definitionSet, value, percent);
			}
		}
	}
}
