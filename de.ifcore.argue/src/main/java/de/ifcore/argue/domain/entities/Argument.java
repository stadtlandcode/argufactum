package de.ifcore.argue.domain.entities;

import java.util.Arrays;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.LogEvent;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.report.ArgumentReport;
import de.ifcore.argue.domain.report.LogReport;
import de.ifcore.argue.utils.EntityUtils;

@Entity
public final class Argument extends AbstractTopicEntityWithRelevance<ArgumentRelevanceVote> implements ArgumentReport,
		Comparable<Argument>, CopiableNestedEntity<Argument, ProContraTopic>
{
	private static final long serialVersionUID = 738311189654472188L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false, name = "topic_id")
	private ProContraTopic topic;

	@Enumerated(EnumType.STRING)
	@Column(updatable = false, nullable = false)
	private TopicThesis topicThesis;

	@OneToOne
	@JoinColumn
	private ArgumentThesis thesis;

	@OneToMany(mappedBy = "argument", cascade = CascadeType.PERSIST)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Fact> facts;

	@OneToMany(mappedBy = "argument", cascade = CascadeType.ALL)
	@Sort(type = SortType.NATURAL)
	private SortedSet<ArgumentLog> logs;

	private EmbeddableAuthorAttribution authorAttribution;

	Argument()
	{
	}

	public Argument(ProContraTopic topic, TopicThesis topicThesis, String thesis, Author author)
	{
		this(topic, topicThesis, thesis, author, 0, null);
	}

	public Argument(ProContraTopic topic, TopicThesis topicThesis, String thesis, Author author, int timeDiff,
			Set<String> attributionList)
	{
		super(author, timeDiff);
		if (topic == null || topicThesis == null)
			throw new IllegalArgumentException();

		this.topicThesis = topicThesis;
		this.topic = topic;
		this.thesis = new ArgumentThesis(thesis, author, this);
		this.authorAttribution = new EmbeddableAuthorAttribution(getAuthorName(), attributionList);
		topic.addArgument(this);
	}

	public ProContraTopic getTopic()
	{
		return topic;
	}

	@Override
	public TopicThesis getTopicThesis()
	{
		return topicThesis;
	}

	public ArgumentThesis getArgumentThesis()
	{
		return thesis;
	}

	public void setArgumentThesis(ArgumentThesis thesis)
	{
		if (thesis != null && thesis.getArgument() != this)
			throw new IllegalArgumentException();
		if (thesis != null && this.thesis != null)
		{
			getAuthorAttribution()
					.addToAuthorList(thesis.getAuthorName(), this.thesis.getAuthorName(), getAuthorName());
		}
		this.thesis = thesis;
	}

	@Override
	public Set<Fact> getFacts()
	{
		return EntityUtils.getVisibleEntities(getAllFacts());
	}

	public Set<Fact> getDeletedFacts(FactType factType)
	{
		if (factType == null)
			throw new NullPointerException();
		Set<Fact> facts = new LinkedHashSet<>();
		for (Fact fact : getAllFacts())
		{
			if (fact.isDeleted() && factType.equals(fact.getFactType()))
				facts.add(fact);
		}
		return Collections.unmodifiableSet(facts);
	}

	public Set<Fact> getAllFacts()
	{
		return EntityUtils.nullSafeSet(facts);
	}

	void addFact(Fact fact)
	{
		if (fact == null || fact.getArgument() != this)
			throw new IllegalArgumentException();
		if (facts == null)
			facts = new TreeSet<>();
		facts.add(fact);
	}

	public Set<ArgumentLog> getLogs()
	{
		return EntityUtils.nullSafeSet(logs);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Argument))
			return false;

		final Argument rhs = (Argument)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.append(getTopicThesis(), rhs.getTopicThesis()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(29, 37).append(getCreatedAt()).append(getAuthor()).append(getTopicThesis())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Argument [topicThesis=").append(topicThesis).append(", text=").append(getText())
				.append(", createdAt=").append(getCreatedAt()).append("]");
		return builder.toString();
	}

	@Override
	public Long getTopicId()
	{
		return topic.getId();
	}

	@Override
	public String getText()
	{
		return thesis.getText();
	}

	@Override
	public Integer getNumberOfFacts(FactType factType)
	{
		if (factType == null)
			throw new NullPointerException();

		int count = 0;
		for (Fact fact : getFacts())
		{
			if (factType.equals(fact.getFactType()))
				count++;
		}
		return Integer.valueOf(count);
	}

	@Override
	public LogReport getModificationLog()
	{
		return thesis.isFirst() ? null : thesis;
	}

	@Override
	public LogReport getDeletionLog()
	{
		return isDeleted() ? logs.first() : null;
	}

	@Override
	public int compareTo(Argument o)
	{
		if (this.equals(o))
			return 0;

		int result = o.getNumericRelevance().compareTo(getNumericRelevance());
		if (result == 0)
			result = Integer.valueOf(o.getRelevanceVotes().size()).compareTo(
					Integer.valueOf(getRelevanceVotes().size()));
		if (result == 0)
			result = getCreationLog().getDateTime().compareTo(o.getCreationLog().getDateTime());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	@Override
	public Long getThesisId()
	{
		return thesis.getId();
	}

	@Override
	public LogReport getCreationLog()
	{
		return this;
	}

	void addLog(ArgumentLog log)
	{
		if (log == null || log.getArgument() != this)
			throw new IllegalArgumentException();
		if (logs == null)
			logs = new TreeSet<>();
		logs.add(log);
	}

	@Override
	protected void log(LogEvent logEvent, Author author)
	{
		new ArgumentLog(this, author, logEvent);
	}

	public TopicTerm getTopicTerm()
	{
		return getTopic().getTopic().getTerm();
	}

	/**
	 * @param text
	 * @param author
	 * @return false when edit is ignored (when text equals current thesis)
	 */
	public boolean editThesis(String text, Author author)
	{
		if (getText().equals(text))
		{
			return false;
		}
		else
		{
			setArgumentThesis(new ArgumentThesis(text, author, this));
			return true;
		}
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Argument copyTo(ProContraTopic destionationParent, int timeDiff)
	{
		Argument argument = new Argument(destionationParent, getTopicThesis(), getText(), destionationParent.getTopic()
				.getAuthor(), timeDiff, getAuthorList());
		EntityUtils.copyNested(getFacts(), argument);
		return argument;
	}

	public EmbeddableAuthorAttribution getAuthorAttribution()
	{
		if (authorAttribution == null)
		{
			authorAttribution = new EmbeddableAuthorAttribution();
		}
		return authorAttribution;
	}

	@Override
	public boolean offerAttributionList()
	{
		return getAuthorAttribution().offerAttributionList(getAuthorList(),
				Arrays.asList(thesis.getAuthorName(), getAuthorName()));
	}

	@Override
	public Set<String> getAuthorList()
	{
		return getAuthorAttribution().getListOfAllAuthors(thesis.getAuthorName(), getAuthorName());
	}
}
