package de.ifcore.argue.view.jsp;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import de.ifcore.argue.domain.models.view.ComparisonValueModel;
import de.ifcore.argue.domain.models.view.CreateCriterionModel;
import de.ifcore.argue.utils.JspTagUtils;

public class ComparisonValueTag extends RequestContextAwareTag
{
	private static final long serialVersionUID = -5870910791674899055L;

	private Long optionId;
	private CreateCriterionModel criterion;

	@Override
	public int doEndTag() throws JspException
	{
		if (optionId != null && criterion != null)
		{
			for (ComparisonValueModel value : criterion.getValues())
			{
				if (optionId.equals(value.getOptionId()))
				{
					JspTagUtils.write(pageContext, value.getString());
					break;
				}
			}
		}
		return super.doEndTag();
	}

	public Long getOptionId()
	{
		return optionId;
	}

	public void setOptionId(Long optionId)
	{
		this.optionId = optionId;
	}

	public CreateCriterionModel getCriterion()
	{
		return criterion;
	}

	public void setCriterion(CreateCriterionModel criterion)
	{
		this.criterion = criterion;
	}

	@Override
	protected int doStartTagInternal() throws Exception
	{
		return SKIP_BODY;
	}
}
