package de.ifcore.argue.domain.entities;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import de.ifcore.argue.domain.report.FactReport;
import de.ifcore.argue.domain.report.LogReport;
import de.ifcore.argue.utils.EntityUtils;

@Entity
public final class Fact extends AbstractTopicEntityWithRelevance<FactRelevanceVote> implements FactReport,
		Comparable<Fact>, CopiableNestedEntity<Fact, Argument>
{
	private static final long serialVersionUID = 2715057876636108180L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn
	private FactEvidence evidence;

	@Column(nullable = false, updatable = false)
	private FactType factType;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private Argument argument;

	@OneToMany(mappedBy = "fact", cascade = CascadeType.PERSIST)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Reference> references;

	@OneToMany(mappedBy = "fact", cascade = CascadeType.ALL)
	@Sort(type = SortType.NATURAL)
	private SortedSet<FactLog> logs;

	private EmbeddableAuthorAttribution authorAttribution;

	Fact()
	{
	}

	public Fact(Argument argument, FactType factType, String text, Author author)
	{
		this(argument, factType, text, author, 0, null);
	}

	public Fact(Argument argument, FactType factType, String text, Author author, int timeDiff,
			Set<String> attributionList)
	{
		super(author, timeDiff);
		if (argument == null || factType == null)
			throw new IllegalArgumentException();

		this.factType = factType;
		setEvidence(new FactEvidence(text, this, author));
		this.argument = argument;
		this.authorAttribution = new EmbeddableAuthorAttribution(getAuthorName(), attributionList);
		addToArgument();
	}

	@Override
	public Long getId()
	{
		return id;
	}

	protected void addToArgument()
	{
		this.argument.addFact(this);
	}

	public FactEvidence getEvidence()
	{
		return evidence;
	}

	public void setEvidence(FactEvidence evidence)
	{
		if (evidence != null && evidence.getFact() != this)
			throw new IllegalArgumentException();
		if (evidence != null && this.evidence != null)
		{
			getAuthorAttribution().addToAuthorList(evidence.getAuthorName(), this.evidence.getAuthorName(),
					getAuthorName());
		}
		this.evidence = evidence;
	}

	public Argument getArgument()
	{
		return argument;
	}

	@Override
	public Set<Reference> getReferences()
	{
		return EntityUtils.getVisibleEntities(getAllReferences());
	}

	public Set<Reference> getAllReferences()
	{
		return EntityUtils.nullSafeSet(references);
	}

	void addReference(Reference reference)
	{
		if (reference == null || reference.getFact() != this)
			throw new IllegalArgumentException();
		if (references == null)
			references = new TreeSet<>();
		references.add(reference);
	}

	public FactType getFactType()
	{
		return factType;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Fact))
			return false;

		final Fact rhs = (Fact)obj;
		return new EqualsBuilder().append(getFactType(), rhs.getFactType()).append(getCreatedAt(), rhs.getCreatedAt())
				.append(getAuthor(), rhs.getAuthor()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(5, 73).append(getFactType()).append(getCreatedAt()).append(getAuthor()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Fact [text=").append(getText()).append(", factType=").append(factType).append("]");
		return builder.toString();
	}

	@Override
	public String getText()
	{
		return getEvidence().getText();
	}

	@Override
	public FactType getType()
	{
		return getFactType();
	}

	@Override
	public LogReport getModificationLog()
	{
		return evidence.isFirst() ? null : evidence;
	}

	@Override
	public LogReport getCreationLog()
	{
		return this;
	}

	@Override
	public Long getArgumentId()
	{
		return argument == null ? null : argument.getId();
	}

	@Override
	public int compareTo(Fact o)
	{
		if (this.equals(o))
			return 0;

		int result = o.getNumericRelevance().compareTo(getNumericRelevance());
		if (result == 0)
			result = getCreationLog().getDateTime().compareTo(o.getCreationLog().getDateTime());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	@Override
	public Long getEvidenceId()
	{
		return evidence == null ? null : evidence.getId();
	}

	public boolean editEvidence(String text, Author author)
	{
		if (getText().equals(text))
		{
			return false;
		}
		else
		{
			setEvidence(new FactEvidence(text, this, author));
			return true;
		}
	}

	@Override
	public Long getTopicId()
	{
		return argument.getTopicId();
	}

	void addLog(FactLog log)
	{
		if (log == null || log.getFact() != this)
			throw new IllegalArgumentException();
		if (logs == null)
			logs = new TreeSet<>();
		logs.add(log);
	}

	public Set<FactLog> getLogs()
	{
		return EntityUtils.nullSafeSet(logs);
	}

	@Override
	protected void log(LogEvent logEvent, Author author)
	{
		new FactLog(this, author, logEvent);
	}

	@Override
	public LogReport getDeletionLog()
	{
		return isDeleted() ? logs.first() : null;
	}

	@Override
	public Fact copyTo(Argument destionationParent, int timeDiff)
	{
		Fact fact = new Fact(destionationParent, getFactType(), getText(), destionationParent.getAuthor(), timeDiff,
				getAuthorList());
		EntityUtils.copyNested(getReferences(), fact);
		return fact;
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
				Arrays.asList(evidence.getAuthorName(), getAuthorName()));
	}

	@Override
	public Set<String> getAuthorList()
	{
		return getAuthorAttribution().getListOfAllAuthors(evidence.getAuthorName(), getAuthorName());
	}
}
