package de.ifcore.argue.services;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;
import de.ifcore.argue.domain.report.CriterionReport;

@Service
public class ComparisonValueDefinitionServiceImpl implements ComparisonValueDefinitionService
{
	private final ComparisonValueDefinitionDao definitionDao;

	@Inject
	public ComparisonValueDefinitionServiceImpl(ComparisonValueDefinitionDao definitionDao)
	{
		this.definitionDao = definitionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public ComparisonValueDefinitionSet getDefinitionSet(Long id)
	{
		return definitionDao.getSet(id);
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<ComparisonValueDefinitionSetReport> getGlobalSets()
	{
		return new TreeSet<ComparisonValueDefinitionSetReport>(definitionDao.getGlobalSets());
	}

	@Override
	@Transactional(readOnly = true)
	public SortedSet<ComparisonValueDefinitionSetReport> getAdditionalSets(CriterionReport criterion)
	{
		return new TreeSet<>();
	}
}
