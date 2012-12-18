package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockFact;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReferenceDaoTest extends IntegrationTest
{
	@Autowired
	private ReferenceDao referenceDao;

	@Test
	public void testSave()
	{
		Fact fact = persistFact(mockFact());
		Reference reference = Reference.instanceOf("http://www.argufactum.de", fact, null);
		referenceDao.save(reference);
		flushAndClear();

		Reference persistedReference = referenceDao.get(reference.getId());
		assertEquals(reference, persistedReference);
	}
}
