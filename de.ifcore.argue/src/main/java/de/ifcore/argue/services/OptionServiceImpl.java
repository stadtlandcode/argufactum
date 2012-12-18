package de.ifcore.argue.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.CreateOptionForm;
import de.ifcore.argue.domain.form.DeleteOptionForm;
import de.ifcore.argue.domain.form.EditOptionForm;

@Service
public class OptionServiceImpl implements OptionService
{
	private final ComparisonTopicDao comparisonTopicDao;
	private final OptionDao optionDao;

	@Inject
	public OptionServiceImpl(ComparisonTopicDao comparisonTopicDao, OptionDao optionDao)
	{
		this.comparisonTopicDao = comparisonTopicDao;
		this.optionDao = optionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Option get(Long id)
	{
		return optionDao.get(id);
	}

	@Override
	@Transactional
	public Option create(CreateOptionForm form, Author author)
	{
		ComparisonTopic topic = comparisonTopicDao.get(form.getTopicId());
		Option option = new Option(topic, form.getLabel(), author);
		optionDao.save(option);
		return option;
	}

	@Override
	@Transactional
	public Option delete(DeleteOptionForm form, Author author)
	{
		Option option = optionDao.get(form.getId());
		option.delete();
		optionDao.update(option);
		return option;
	}

	@Override
	public Option edit(EditOptionForm form, Author author)
	{
		Option option = optionDao.get(form.getId());
		if (option.edit(form.getLabel(), author))
			optionDao.update(option);
		return option;
	}
}
