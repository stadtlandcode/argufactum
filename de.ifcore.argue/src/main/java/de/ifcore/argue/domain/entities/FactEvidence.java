package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.enumerations.LogEvent;
import de.ifcore.argue.domain.report.EventReport;

@Entity
public final class FactEvidence extends AbstractAuthoredEntity implements IdEntity, EventReport
{
	private static final long serialVersionUID = -7904491300036337924L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false)
	private String text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Fact fact;

	@JoinColumn(updatable = false, nullable = false)
	private Boolean first;

	FactEvidence()
	{
	}

	public FactEvidence(String text, Fact fact, Author author)
	{
		super(author);
		if (StringUtils.isBlank(text))
			throw new IllegalArgumentException();
		this.text = StringUtils.normalizeSpace(text);
		this.fact = fact;
		this.first = fact.getEvidence() == null ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getText()
	{
		return text;
	}

	public Fact getFact()
	{
		return fact;
	}

	public boolean isFirst()
	{
		return Boolean.TRUE.equals(first);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof FactEvidence))
			return false;

		final FactEvidence rhs = (FactEvidence)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getText(), rhs.getText())
				.append(getAuthor(), rhs.getAuthor()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(41, 43).append(getCreatedAt()).append(getAuthor()).append(getText()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("FactTerm [text=").append(text).append("]");
		return builder.toString();
	}

	@Override
	public LogEvent getEvent()
	{
		return LogEvent.UPDATE;
	}
}
