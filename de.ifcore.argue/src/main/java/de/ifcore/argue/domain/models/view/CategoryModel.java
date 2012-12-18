package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.CategoryUrl;

public class CategoryModel implements ViewModel, CategoryUrl
{
	private static final long serialVersionUID = -2127810638368981929L;

	private final Long id;
	private final String name;
	private final String nameForUrl;

	public static Set<CategoryModel> setOf(Set<Category> categories)
	{
		Set<CategoryModel> categoryModels = new LinkedHashSet<>();
		for (Category category : categories)
			categoryModels.add(new CategoryModel(category));
		return Collections.unmodifiableSet(categoryModels);
	}

	public CategoryModel(Category category)
	{
		this.id = category.getId();
		this.name = category.getName();
		this.nameForUrl = category.getNameForUrl();
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String getNameForUrl()
	{
		return nameForUrl;
	}
}
