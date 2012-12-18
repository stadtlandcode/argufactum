package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.UrlTag;

import de.ifcore.argue.controller.CategoryController;
import de.ifcore.argue.domain.entities.CategoryUrl;

public class CategoryUrlTag extends UrlTag
{
	private static final long serialVersionUID = 5709430577478470980L;
	private CategoryUrl category;

	@Override
	public int doStartTagInternal() throws JspException
	{
		setValue(CategoryController.getUrl(category));
		return super.doStartTagInternal();
	}

	public void setCategory(CategoryUrl category)
	{
		this.category = category;
	}
}
