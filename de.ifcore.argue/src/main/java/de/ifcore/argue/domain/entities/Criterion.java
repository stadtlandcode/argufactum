package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.report.ComparisonValueReport;
import de.ifcore.argue.domain.report.CriterionReport;

@Entity
public final class Criterion extends AbstractAuthoredEntity implements CriterionReport, Comparable<Criterion>, IdEntity
{
	private static final long serialVersionUID = -636505756589956148L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false, name = "topic_id")
	private ComparisonTopic topic;

	@Column(nullable = false)
	private String label;

	@ManyToOne
	@JoinColumn(nullable = false)
	private ComparisonValueDefinitionSet definitionSet;

	@ManyToMany
	private Set<ComparisonValueDefinitionSet> definitions;

	@OneToMany(mappedBy = "dedicatedEntity")
	private Set<CriterionImportance> importance;

	@OneToMany(mappedBy = "criterion")
	private Set<ComparisonValue> values;

	private Boolean deleted;

	Criterion()
	{
	}

	public Criterion(ComparisonTopic topic, String label, ComparisonValueDefinitionSet definitionSet, Author author)
	{
		super(author);
		if (topic == null || label == null || definitionSet == null)
			throw new IllegalArgumentException();
		this.topic = topic;
		this.label = label;
		this.definitionSet = definitionSet;
	}

	/**
	 * @param label
	 *            if null or blank, current label remains
	 * @param definitionSet
	 *            if null, current definitionSet remains
	 * @return false when edit is ignored (nothing changed)
	 */
	public boolean edit(String label, ComparisonValueDefinitionSet definitionSet)
	{
		boolean editLabel = !StringUtils.isBlank(label) && !label.equals(this.label);
		boolean editDefinitionSet = definitionSet != null && !definitionSet.equals(this.definitionSet);
		if (!editLabel && !editDefinitionSet)
		{
			return false;
		}
		else
		{
			if (editLabel)
				this.label = label;
			if (editDefinitionSet)
				this.definitionSet = definitionSet;
			return true;
		}
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public ComparisonValueDefinitionSet getDefinitionSet()
	{
		return definitionSet;
	}

	@Override
	public Byte getImportance(RegisteredUser user)
	{
		CriterionImportance importanceEntity = getImportanceEntity(user);
		if (importanceEntity == null)
			return 50;
		else
			return importanceEntity.getImportance();
	}

	public CriterionImportance setImportance(Byte importance, Author author)
	{
		CriterionImportance importanceEntity = getImportanceEntity(author.getRegisteredUser());
		if (importanceEntity != null)
		{
			importanceEntity.editImportance(importance, author);
		}
		else
		{
			importanceEntity = new CriterionImportance(this, importance, author);
			if (this.importance == null)
				this.importance = new HashSet<>();
			this.importance.add(importanceEntity);
		}
		return importanceEntity;
	}

	private CriterionImportance getImportanceEntity(RegisteredUser user)
	{
		if (user != null)
		{
			for (CriterionImportance importanceEntity : getImportanceEntities())
			{
				if (user.equals(importanceEntity.getAuthor().getRegisteredUser()))
					return importanceEntity;
			}
		}

		return null;
	}

	public Set<CriterionImportance> getImportanceEntities()
	{
		return importance == null ? Collections.<CriterionImportance> emptySet() : Collections
				.unmodifiableSet(importance);
	}

	@Override
	public Set<ComparisonValueReport> getValues()
	{
		return values == null ? Collections.<ComparisonValueReport> emptySet() : Collections
				.<ComparisonValueReport> unmodifiableSet(values);
	}

	public Set<ComparisonValueDefinitionSet> getDefinitions()
	{
		return definitions == null ? Collections.<ComparisonValueDefinitionSet> emptySet() : Collections
				.unmodifiableSet(definitions);
	}

	public ComparisonTopic getTopic()
	{
		return topic;
	}

	public boolean isDeleted()
	{
		return deleted.booleanValue();
	}

	public void delete()
	{
		deleted = Boolean.TRUE;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Criterion))
			return false;

		final Criterion rhs = (Criterion)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(29, 37).append(getCreatedAt()).append(getAuthor()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Criterion [label=").append(label).append(", definitionSet=").append(definitionSet).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Criterion o)
	{
		if (this.equals(o))
			return 0;

		int result = getDateTime().compareTo(o.getDateTime());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	@Override
	public Long getTopicId()
	{
		return topic.getId();
	}
}
