package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockFact;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.FactEvidence;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class FactDaoTest extends IntegrationTest
{
	@Test
	public void testSave()
	{
		Argument argument = persistArgument(mockArgument());
		Fact fact = new Fact(argument, FactType.CONFIRMATIVE, "12345", mockAuthor());
		factDao.save(fact);
		flushAndClear();

		Fact persistedFact = factDao.get(fact.getId());
		assertEquals(fact, persistedFact);
	}

	@Test
	public void testGetEvidences()
	{
		Fact fact = persistFact(mockFact());
		flushAndClear();
		assertEquals(1, factDao.getEvidences(fact.getId()).size());

		fact = factDao.get(fact.getId());
		fact.editEvidence("12345678", mockAuthor());
		factDao.saveEvidence(fact.getEvidence());
		factDao.update(fact);
		flushAndClear();

		List<FactEvidence> evidences = factDao.getEvidences(fact.getId());
		assertEquals(2, evidences.size());
	}
}
