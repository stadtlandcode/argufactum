package de.ifcore.argue.domain.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public final class EmbeddableAuthorAttribution implements Serializable
{
	private static final long serialVersionUID = 8658669330844474980L;

	@Column(length = 65536)
	private String authors;

	@Column(updatable = false, length = 65536)
	private String externalAuthors;

	public EmbeddableAuthorAttribution()
	{
	}

	public EmbeddableAuthorAttribution(String authorOfEntity, Set<String> externalAuthorList)
	{
		if (externalAuthorList != null)
		{
			if (externalAuthorList.contains(authorOfEntity))
			{
				externalAuthorList = new LinkedHashSet<>(externalAuthorList);
				externalAuthorList.remove(authorOfEntity);
			}
			if (!externalAuthorList.isEmpty())
			{
				this.externalAuthors = StringUtils.join(externalAuthorList, ";");
			}
		}
	}

	void addToAuthorList(String newAuthor, String currentAuthor, String authorOfEntity)
	{
		Set<String> list = new LinkedHashSet<>(getAuthorList());
		if (!list.isEmpty())
		{
			list.add(newAuthor);
		}
		else if (!authorOfEntity.equals(currentAuthor) && !newAuthor.equals(currentAuthor))
		{
			list.add(authorOfEntity);
			list.add(currentAuthor);
			list.add(newAuthor);
		}

		if (!list.isEmpty())
		{
			this.authors = StringUtils.join(list, ";");
		}
	}

	public boolean offerAttributionList(Set<String> attributionList, List<String> displayedAuthors)
	{
		int match = 0;
		Set<String> uniqueDisplayedAuthors = new LinkedHashSet<>(displayedAuthors);
		for (String author : uniqueDisplayedAuthors)
		{
			if (attributionList.contains(author))
				match++;
		}
		return match < attributionList.size();
	}

	Set<String> getListOfAllAuthors(String currentAuthor, String authorOfEntity)
	{
		Set<String> set = null;
		if (StringUtils.isEmpty(authors))
			set = convertStringToSet(authorOfEntity + ";" + currentAuthor);
		else
			set = getAuthorList();

		set = new LinkedHashSet<>(set);
		set.addAll(getExternalAuthorList());

		return Collections.unmodifiableSet(set);
	}

	public Set<String> getAuthorList()
	{
		return convertStringToSet(authors);
	}

	public Set<String> getExternalAuthorList()
	{
		return convertStringToSet(externalAuthors);
	}

	public String getAuthors()
	{
		return authors;
	}

	public String getExternalAuthors()
	{
		return externalAuthors;
	}

	private Set<String> convertStringToSet(String stringList)
	{
		return stringList == null ? Collections.<String> emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(
				Arrays.asList(stringList.split(";"))));
	}

	@Override
	public String toString()
	{
		return "EmbeddableAuthorAttribution [authorList=" + authors + ", externalAuthorList=" + externalAuthors + "]";
	}
}
