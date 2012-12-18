package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.report.OptionReport;

@Entity
public final class Option extends AbstractAuthoredEntity implements OptionReport, Comparable<Option>, IdEntity
{
	private static final long serialVersionUID = -8102804134262990770L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false, name = "topic_id")
	private ComparisonTopic topic;

	@Column(nullable = false)
	private String label;

	@OneToMany(mappedBy = "option")
	private Set<ComparisonValue> values;

	private Boolean deleted;

	Option()
	{
	}

	public Option(ComparisonTopic topic, String label, Author author)
	{
		super(author);
		if (topic == null)
			throw new IllegalArgumentException();
		this.topic = topic;
		setLabel(label);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public ComparisonTopic getTopic()
	{
		return topic;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	private void setLabel(String label)
	{
		if (StringUtils.isBlank(label))
			throw new IllegalArgumentException();
		this.label = label;
	}

	public boolean edit(String label, Author author)
	{
		if (this.label.equals(label))
		{
			return false;
		}
		else
		{
			setLabel(label);
			return true;
		}
	}

	public Set<ComparisonValue> getValues()
	{
		return values == null ? Collections.<ComparisonValue> emptySet() : Collections.unmodifiableSet(values);
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
		if (!(obj instanceof Option))
			return false;

		final Option rhs = (Option)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(41, 43).append(getCreatedAt()).append(getAuthor()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Option [label=").append(label).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Option o)
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
		return getTopic().getId();
	}
}
