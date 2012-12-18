package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockFact;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.form.CreateReferenceForm;

public class ReferenceServiceImplTest
{

	@Test
	public void testCreate()
	{
		CreateReferenceForm form = new CreateReferenceForm();
		form.setFactId(1l);
		form.setText("http://www.argufactum.de");

		Author author = mockAuthor();
		FactDao factDao = mock(FactDao.class);
		Fact fact = mockFact();
		when(factDao.get(1l)).thenReturn(fact);
		ReferenceDao referenceDao = mock(ReferenceDao.class);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);

		ReferenceService service = new ReferenceServiceImpl(factDao, referenceDao, accessRightService);
		Reference reference = service.create(form, author);
		assertEquals(factDao.get(1l), reference.getFact());
		assertEquals(form.getText(), reference.getUrl());
		assertEquals("argufactum.de", reference.getText());
		verify(referenceDao).save(reference);
	}

}
