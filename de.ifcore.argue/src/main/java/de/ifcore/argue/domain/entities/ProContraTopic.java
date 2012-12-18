package de.ifcore.argue.domain.entities;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.report.DiscussionTypeReport;
import de.ifcore.argue.utils.EntityUtils;

@Entity
public final class ProContraTopic implements TopicEntity, CopiableNestedEntity<ProContraTopic, Topic>,
		DiscussionTypeReport
{
	private static final long serialVersionUID = 950096378649721504L;

	@Id
	private Long id;

	@OneToOne(mappedBy = "proContraTopic", optional = false, cascade = CascadeType.PERSIST)
	@MapsId
	private Topic topic;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TopicThesesMessageCodeAppendix thesesMessageCodeAppendix;

	@OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Argument> arguments;

	ProContraTopic()
	{
	}

	public ProContraTopic(Topic topic)
	{
		if (topic == null)
			throw new IllegalArgumentException();
		this.topic = topic;
		this.thesesMessageCodeAppendix = TopicThesesMessageCodeAppendix.PRO_CONTRA;
		topic.setProContraTopic(this);
	}

	@Override
	public ProContraTopic copyTo(Topic destinationTopic, int timeDiff)
	{
		ProContraTopic proContraTopic = new ProContraTopic(destinationTopic);
		proContraTopic.setThesesMessageCodeAppendix(getThesesMessageCodeAppendix());
		EntityUtils.copyNested(getArguments(), proContraTopic);
		return proContraTopic;
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

	public TopicThesesMessageCodeAppendix getThesesMessageCodeAppendix()
	{
		return thesesMessageCodeAppendix;
	}

	public void setThesesMessageCodeAppendix(TopicThesesMessageCodeAppendix thesesMessageCodeAppendix)
	{
		if (thesesMessageCodeAppendix == null)
			throw new IllegalArgumentException();
		this.thesesMessageCodeAppendix = thesesMessageCodeAppendix;
	}

	public String getProMessageCode()
	{
		return "TopicThesis.PRO." + getThesesMessageCodeAppendix();
	}

	public String getContraMessageCode()
	{
		return "TopicThesis.CONTRA." + getThesesMessageCodeAppendix();
	}

	public Set<Argument> getArguments()
	{
		return EntityUtils.getVisibleEntities(getAllArguments());
	}

	public Set<Argument> getDeletedArguments(TopicThesis topicThesis)
	{
		if (topicThesis == null)
			throw new NullPointerException();
		Set<Argument> arguments = new LinkedHashSet<>();
		for (Argument argument : getAllArguments())
		{
			if (argument.isDeleted() && topicThesis.equals(argument.getTopicThesis()))
				arguments.add(argument);
		}
		return Collections.unmodifiableSet(arguments);
	}

	private Set<Argument> getAllArguments()
	{
		return EntityUtils.nullSafeSet(arguments);
	}

	public void addArgument(Argument argument)
	{
		if (argument.getTopic() != this)
			throw new IllegalArgumentException();
		if (arguments == null)
			arguments = new TreeSet<>();
		arguments.add(argument);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ProContraTopic))
			return false;

		final ProContraTopic rhs = (ProContraTopic)obj;
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
		builder.append("ProContraTopic [id=").append(id).append(", thesesMessageCodeAppendix=")
				.append(thesesMessageCodeAppendix).append("]");
		return builder.toString();
	}

	@Override
	public Long getTopicId()
	{
		return id;
	}

	@Override
	public String getNameOfMainEntities()
	{
		return "arguments";
	}

	@Override
	public Integer getNumberOfMainEntities()
	{
		return Integer.valueOf(getArguments().size());
	}

	@Override
	public String getNameOfSecondaryEntities()
	{
		return "facts";
	}

	@Override
	public Integer getNumberOfSecondaryEntities()
	{
		int facts = 0;
		for (Argument argument : getArguments())
		{
			facts += argument.getNumberOfFacts(FactType.CONFIRMATIVE).intValue();
			facts += argument.getNumberOfFacts(FactType.DEBILITATIVE).intValue();
		}
		return Integer.valueOf(facts);
	}
}
