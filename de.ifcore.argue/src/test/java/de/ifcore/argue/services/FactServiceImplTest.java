package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.form.CreateFactForm;

public class FactServiceImplTest
{
	@Test
	public void testCreate()
	{
		CreateFactForm form = new CreateFactForm();
		form.setArgumentId(1l);
		form.setReference("12345");
		form.setText("54321");
		form.setFactType(FactType.CONFIRMATIVE);

		Author author = mockAuthor();
		Argument argument = mockArgument();
		ArgumentDao argumentDao = mock(ArgumentDao.class);
		when(argumentDao.get(1l)).thenReturn(argument);
		ReferenceDao referenceDao = mock(ReferenceDao.class);
		FactDao factDao = mock(FactDao.class);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);

		FactService service = new FactServiceImpl(factDao, argumentDao, referenceDao, accessRightService, null);
		Fact fact = service.create(form, author);
		assertEquals(form.getText(), fact.getEvidence().getText());
		assertEquals(author, fact.getAuthor());
		assertEquals(1, fact.getReferences().size());
		assertEquals(form.getReference(), fact.getReferences().iterator().next().getText());
		assertNull(fact.getReferences().iterator().next().getUrl());
		assertEquals(form.getFactType(), fact.getFactType());
		verify(factDao).save(fact);
		verify(referenceDao).save(fact.getReferences().iterator().next());

		// without reference
		form = new CreateFactForm();
		form.setArgumentId(1l);
		form.setText("54321");
		form.setFactType(FactType.CONFIRMATIVE);

		fact = service.create(form, author);
		assertNotNull(fact.getReferences());
		assertTrue(fact.getReferences().isEmpty());
	}
}
