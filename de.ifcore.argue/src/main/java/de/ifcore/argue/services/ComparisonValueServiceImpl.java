package de.ifcore.argue.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ComparisonValueDao;
import de.ifcore.argue.dao.CriterionDao;
import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.ComparisonValueForm;

@Service
public class ComparisonValueServiceImpl implements ComparisonValueService
{
	private final ComparisonValueDao valueDao;
	private final CriterionDao criterionDao;
	private final OptionDao optionDao;

	@Inject
	public ComparisonValueServiceImpl(ComparisonValueDao valueDao, CriterionDao criterionDao, OptionDao optionDao)
	{
		this.valueDao = valueDao;
		this.criterionDao = criterionDao;
		this.optionDao = optionDao;
	}

	@Override
	@Transactional
	public ComparisonValue persist(ComparisonValueForm form, Author author)
	{
		ComparisonValue value = valueDao.get(form.getCriterionId(), form.getOptionId());
		if (value == null)
		{
			Criterion criterion = criterionDao.get(form.getCriterionId());
			Option option = optionDao.get(form.getOptionId());
			value = new ComparisonValue(criterion, option, form.getString(), author);
			valueDao.save(value);
		}
		else
		{
			value.setString(form.getString());
			valueDao.update(value);
		}
		return value;
	}
}
