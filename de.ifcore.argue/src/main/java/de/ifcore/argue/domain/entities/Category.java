package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.utils.UrlUtils;

@Entity
public class Category implements IdEntity, Comparable<Category>, CategoryUrl
{
	private static final long serialVersionUID = 4710556635314134865L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, unique = true)
	private String nameForUrl;

	@OneToMany(mappedBy = "category")
	private Set<Topic> topics;

	Category()
	{
	}

	public Category(String name)
	{
		if (name == null)
			throw new IllegalArgumentException();
		this.name = name;
		this.nameForUrl = UrlUtils.slugify(name);
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

	public void setNameForUrl(String nameForUrl)
	{
		this.nameForUrl = nameForUrl;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Set<Topic> getTopics()
	{
		return topics == null ? Collections.<Topic> emptySet() : Collections.unmodifiableSet(topics);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Category))
			return false;

		final Category rhs = (Category)obj;
		return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(43, 53).append(getName()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Category [id=").append(id).append(", name=").append(name).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Category o)
	{
		if (this.equals(o))
			return 0;

		int result = getName().compareTo(o.getName());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	public Integer getNumberOfTopics()
	{
		return Integer.valueOf(topics.size());
	}
}
