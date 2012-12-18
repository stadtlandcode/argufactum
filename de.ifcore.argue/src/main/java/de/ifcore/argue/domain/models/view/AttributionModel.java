package de.ifcore.argue.domain.models.view;

import org.apache.commons.lang3.StringUtils;

import de.ifcore.argue.domain.entities.AuthorAttribution;

public class AttributionModel implements ViewModel
{
	private static final long serialVersionUID = -6564001665689649183L;

	private final boolean offerAttributionList;
	private final String authors;

	public AttributionModel(AuthorAttribution authorAttribution)
	{
		this.offerAttributionList = authorAttribution.offerAttributionList();
		this.authors = (this.offerAttributionList) ? StringUtils.join(authorAttribution.getAuthorList(), ", ") : "";
	}

	public boolean isOfferAttributionList()
	{
		return offerAttributionList;
	}

	public String getAuthors()
	{
		return authors;
	}

	@Override
	public String toString()
	{
		return "AttributionModel [authors=" + authors + "]";
	}
}
