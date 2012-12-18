package de.ifcore.argue.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.dao.CriterionDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.CriterionImportance;
import de.ifcore.argue.domain.form.CreateCriterionForm;
import de.ifcore.argue.domain.form.CriterionImportanceForm;
import de.ifcore.argue.domain.form.DeleteCriterionForm;
import de.ifcore.argue.domain.form.EditCriterionForm;

@Service
public class CriterionServiceImpl implements CriterionService
{
	private final CriterionDao criterionDao;
	private final ComparisonTopicDao comparisonTopicDao;
	private final ComparisonValueDefinitionDao definitionDao;

	@Inject
	public CriterionServiceImpl(CriterionDao criterionDao, ComparisonTopicDao comparisonTopicDao,
			ComparisonValueDefinitionDao definitionDao)
	{
		this.criterionDao = criterionDao;
		this.comparisonTopicDao = comparisonTopicDao;
		this.definitionDao = definitionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Criterion get(Long id)
	{
		return criterionDao.get(id);
	}

	@Override
	@Transactional
	public Criterion create(CreateCriterionForm form, Author author)
	{
		ComparisonTopic topic = comparisonTopicDao.get(form.getTopicId());
		ComparisonValueDefinitionSet definitionSet = definitionDao.getSet(form.getDefinitionSetId());
		Criterion criterion = new Criterion(topic, form.getLabel(), definitionSet, author);
		criterionDao.save(criterion);
		return criterion;
	}

	@Override
	@Transactional
	public Criterion delete(DeleteCriterionForm form, Author author)
	{
		Criterion criterion = criterionDao.get(form.getId());
		criterion.delete();
		criterionDao.update(criterion);
		return criterion;
	}

	@Override
	@Transactional
	public Criterion edit(EditCriterionForm form, Author author)
	{
		Criterion criterion = criterionDao.get(form.getId());
		ComparisonValueDefinitionSet definitionSet = definitionDao.getSet(form.getDefinitionSetId());
		if (criterion.edit(form.getLabel(), definitionSet))
			criterionDao.update(criterion);
		return criterion;
	}

	@Override
	@Transactional
	public CriterionImportance setImportance(CriterionImportanceForm form, Author author)
	{
		CriterionImportance importanceEntity = null;
		boolean executeUpdate = false;
		if (form.getImportanceId() != null)
		{
			importanceEntity = criterionDao.getImportance(form.getImportanceId());
			if (importanceEntity == null)
				throw new IllegalArgumentException();
			executeUpdate = importanceEntity.editImportance(form.getImportance(), author);
		}
		else
		{
			Criterion criterion = criterionDao.get(form.getCriterionId());
			Byte importanceBefore = criterion.getImportance(author.getRegisteredUser());
			importanceEntity = criterion.setImportance(form.getImportance(), author);
			executeUpdate = !importanceEntity.getImportance().equals(importanceBefore);
		}

		if (importanceEntity != null)
		{
			if (importanceEntity.getId() == null)
				criterionDao.saveImportance(importanceEntity);
			else if (executeUpdate)
				criterionDao.updateImportance(importanceEntity);
		}
		return importanceEntity;
	}

}
