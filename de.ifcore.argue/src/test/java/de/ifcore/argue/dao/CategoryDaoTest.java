package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockCategory;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryDaoTest extends IntegrationTest
{
	@Test
	public void testGetAllCategories()
	{
		assertEquals(0, categoryDao.getAll().size());
		persistCategory(mockCategory());
		persistCategory(mockCategory());
		persistCategory(mockCategory());
		flushAndClear();
		assertEquals(3, categoryDao.getAll().size());
	}

	@Test
	public void testFindByName()
	{
		Category category = persistCategory(mockCategory());
		flushAndClear();

		Category persistedCategory = categoryDao.findByName(category.getName());
		assertEquals(category, persistedCategory);
	}

	@Test
	public void testDelete()
	{
		Category emptyCategory = persistCategory(mockCategory());
		Category categoryWithTopics = persistCategory(mockCategory());
		persistTopic(new Topic("abc", null, DiscussionType.PRO_CONTRA, mockAuthor(mockUser()), TopicVisibility.PUBLIC,
				categoryWithTopics));
		flushAndClear();

		emptyCategory = categoryDao.get(emptyCategory.getId());
		categoryDao.delete(emptyCategory);
		flushAndClear();
		assertNull(categoryDao.get(emptyCategory.getId()));

		categoryWithTopics = categoryDao.get(categoryWithTopics.getId());
		try
		{
			categoryDao.delete(categoryWithTopics);
			fail();
		}
		catch (UnsupportedOperationException e)
		{
		}
	}

	@Test
	public void testUpdate()
	{
		Category category = persistCategory(mockCategory());
		flushAndClear();

		category.setName("Haushalt");
		categoryDao.update(category);
		flushAndClear();

		Category persistedCategory = categoryDao.get(category.getId());
		assertEquals(category, persistedCategory);
	}

	@Test
	public void testSave()
	{
		Category category = mockCategory();
		categoryDao.save(category);
		flushAndClear();

		Category persistedCategory = categoryDao.get(category.getId());
		assertEquals(category, persistedCategory);
	}

}
