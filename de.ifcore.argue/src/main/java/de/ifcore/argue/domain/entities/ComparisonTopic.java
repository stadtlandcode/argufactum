package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import de.ifcore.argue.domain.report.DiscussionTypeReport;

@Entity
public final class ComparisonTopic implements IdEntity, DiscussionTypeReport
{
	private static final long serialVersionUID = -1362251052941377414L;

	@Id
	private Long id;

	@OneToOne(mappedBy = "comparisonTopic", optional = false, cascade = CascadeType.PERSIST)
	@MapsId
	private Topic topic;

	@OneToMany(mappedBy = "topic")
	@Sort(type = SortType.NATURAL)
	private SortedSet<Criterion> criteria;

	@OneToMany(mappedBy = "topic")
	@Sort(type = SortType.NATURAL)
	private SortedSet<Option> options;

	ComparisonTopic()
	{
	}

	public ComparisonTopic(Topic topic)
	{
		if (topic == null)
			throw new IllegalArgumentException();
		this.topic = topic;
		topic.setComparisonTopic(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public Set<Criterion> getCriteria()
	{
		return criteria == null ? Collections.<Criterion> emptySet() : Collections.unmodifiableSet(criteria);
	}

	public Set<Option> getOptions()
	{
		return options == null ? Collections.<Option> emptySet() : Collections.unmodifiableSet(options);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ComparisonTopic))
			return false;

		final ComparisonTopic rhs = (ComparisonTopic)obj;
		return new EqualsBuilder().append(getTopic(), rhs.getTopic()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(31, 43).append(getTopic()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonTopic [id=").append(id).append("]");
		return builder.toString();
	}

	@Override
	public String getNameOfMainEntities()
	{
		return "criteria";
	}

	@Override
	public Integer getNumberOfMainEntities()
	{
		return Integer.valueOf(getCriteria().size());
	}

	@Override
	public String getNameOfSecondaryEntities()
	{
		return "options";
	}

	@Override
	public Integer getNumberOfSecondaryEntities()
	{
		return Integer.valueOf(getOptions().size());
	}
}
