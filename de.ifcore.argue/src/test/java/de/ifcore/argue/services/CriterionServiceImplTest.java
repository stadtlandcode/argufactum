package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockComparisonTopic;
import static de.ifcore.argue.utils.EntityTestUtils.mockCriterion;
import static de.ifcore.argue.utils.EntityTestUtils.mockDefinitionSet;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

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

public class CriterionServiceImplTest
{
	@Test
	public void testCreate()
	{
		Author author = mockAuthor();
		ComparisonTopic comparisonTopic = mockComparisonTopic();
		ComparisonValueDefinitionSet definitionSet = mockDefinitionSet();
		CreateCriterionForm form = new CreateCriterionForm();
		form.setLabel("test");
		form.setDefinitionSetId(2l);
		form.setTopicId(1l);

		CriterionDao criterionDao = mock(CriterionDao.class);
		ComparisonTopicDao comparisonTopicDao = mock(ComparisonTopicDao.class);
		ComparisonValueDefinitionDao definitionDao = mock(ComparisonValueDefinitionDao.class);
		when(comparisonTopicDao.get(form.getTopicId())).thenReturn(comparisonTopic);
		when(definitionDao.getSet(form.getDefinitionSetId())).thenReturn(definitionSet);
		CriterionService criterionService = new CriterionServiceImpl(criterionDao, comparisonTopicDao, definitionDao);

		Criterion criterion = criterionService.create(form, author);
		assertEquals(form.getLabel(), criterion.getLabel());
		assertEquals(definitionSet, criterion.getDefinitionSet());
		assertEquals(comparisonTopic, criterion.getTopic());
		assertEquals(author, criterion.getAuthor());
		assertNotNull(criterion.getCreatedAt());
		verify(criterionDao).save(criterion);
	}

	@Test
	public void testDelete()
	{
		Criterion criterion = mockCriterion();
		Author author = mockAuthor();
		DeleteCriterionForm form = new DeleteCriterionForm();
		form.setId(1l);

		CriterionDao criterionDao = mock(CriterionDao.class);
		when(criterionDao.get(form.getId())).thenReturn(criterion);
		CriterionService criterionService = new CriterionServiceImpl(criterionDao, null, null);

		criterionService.delete(form, author);
		assertTrue(criterion.isDeleted());
		verify(criterionDao).update(criterion);
	}

	@Test
	public void testEdit()
	{
		Author author = mockAuthor();
		Criterion mockCriterion = mockCriterion();
		ComparisonValueDefinitionSet definitionSet = mockDefinitionSet();
		EditCriterionForm form = new EditCriterionForm();
		form.setLabel("test123");
		form.setDefinitionSetId(2l);
		form.setId(1l);

		CriterionDao criterionDao = mock(CriterionDao.class);
		ComparisonValueDefinitionDao definitionDao = mock(ComparisonValueDefinitionDao.class);
		when(criterionDao.get(form.getId())).thenReturn(mockCriterion);
		when(definitionDao.getSet(form.getDefinitionSetId())).thenReturn(definitionSet);
		CriterionService criterionService = new CriterionServiceImpl(criterionDao, null, definitionDao);

		Criterion criterion = criterionService.edit(form, author);
		assertEquals(form.getLabel(), criterion.getLabel());
		assertEquals(definitionSet, criterion.getDefinitionSet());
		assertNotSame(author, criterion.getAuthor());
		verify(criterionDao).update(criterion);
	}

	@Test
	public void testSetImportance()
	{
		Author author = mockAuthor(mockUser());
		Criterion criterion = mockCriterion();
		CriterionImportanceForm form = new CriterionImportanceForm();
		form.setCriterionId(1l);
		form.setImportance((byte)30);

		CriterionDao criterionDao = mock(CriterionDao.class);
		when(criterionDao.get(form.getCriterionId())).thenReturn(criterion);
		CriterionService criterionService = new CriterionServiceImpl(criterionDao, null, null);

		CriterionImportance importanceEntity = criterionService.setImportance(form, author);
		assertEquals(form.getImportance(), importanceEntity.getImportance());
		assertEquals(author, importanceEntity.getAuthor());
		assertEquals(criterion, importanceEntity.getDedicatedEntity());
		assertEquals(importanceEntity.getImportance(), criterion.getImportance(author.getRegisteredUser()));
		verify(criterionDao, times(1)).saveImportance(importanceEntity);
		ReflectionTestUtils.setField(importanceEntity, "id", 2l);

		criterionService.setImportance(form, author);
		verify(criterionDao, times(1)).saveImportance(importanceEntity);

		form.setImportance((byte)25);
		criterionService.setImportance(form, author);
		assertEquals(form.getImportance(), importanceEntity.getImportance());
		verify(criterionDao).updateImportance(importanceEntity);

		form.setImportance((byte)20);
		form.setImportanceId(importanceEntity.getId());
		when(criterionDao.getImportance(form.getImportanceId())).thenReturn(importanceEntity);
		criterionService.setImportance(form, author);
		assertEquals(form.getImportance(), importanceEntity.getImportance());
		verify(criterionDao).getImportance(form.getImportanceId());
		verify(criterionDao, times(2)).updateImportance(importanceEntity);
	}
}
