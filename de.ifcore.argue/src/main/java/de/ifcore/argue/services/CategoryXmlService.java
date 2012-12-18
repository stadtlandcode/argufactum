package de.ifcore.argue.services;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.domain.entities.Category;

public class CategoryXmlService extends AbstractXmlImportService<Category>
{
	private final CategoryDao categoryDao;

	@Inject
	public CategoryXmlService(CategoryDao categoryDao, PlatformTransactionManager transactionManager, String startupFile)
	{
		super(transactionManager, startupFile, null, "category");
		if (categoryDao == null)
			throw new IllegalArgumentException();
		this.categoryDao = categoryDao;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkPreconditions()
	{
		return categoryDao.getAll().isEmpty();
	}

	@Override
	protected Category parseMainNode(Element element)
	{
		String name = element.getChildNodes().item(0).getNodeValue();
		return new Category(name);
	}

	@Override
	@Transactional
	protected void saveObjects(Set<Category> categories)
	{
		for (Category category : categories)
		{
			categoryDao.save(category);
		}
	}

}
