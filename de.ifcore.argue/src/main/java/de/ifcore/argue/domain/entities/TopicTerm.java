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
import de.ifcore.argue.domain.report.TopicEventReport;

@Entity
public final class TopicTerm extends AbstractAuthoredEntity implements IdEntity, TopicEventReport
{
	private static final long serialVersionUID = -2132533757692469055L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false)
	private String text;

	@Column(nullable = false, updatable = false)
	private String definition;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Topic topic;

	@JoinColumn(updatable = false, nullable = false)
	private Boolean first;

	TopicTerm()
	{
	}

	public TopicTerm(String text, String definition, Topic topic, Author author)
	{
		super(author);
		if (StringUtils.isBlank(text))
			throw new IllegalArgumentException();
		this.text = StringUtils.normalizeSpace(text);
		this.definition = StringUtils.isBlank(definition) ? "" : StringUtils.normalizeSpace(definition);
		this.first = topic.getTerm() == null ? Boolean.TRUE : Boolean.FALSE;
		this.topic = topic;
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

	@Override
	public String getDefinition()
	{
		return definition;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public boolean isFirst()
	{
		return first.booleanValue();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof TopicTerm))
			return false;

		final TopicTerm rhs = (TopicTerm)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getText(), rhs.getText())
				.append(getDefinition(), rhs.getDefinition()).append(getAuthor(), rhs.getAuthor()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(5, 11).append(getCreatedAt()).append(getAuthor()).append(getText())
				.append(getDefinition()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TopicTerm [text=").append(text).append("]");
		return builder.toString();
	}

	@Override
	public LogEvent getEvent()
	{
		return LogEvent.UPDATE;
	}
}
