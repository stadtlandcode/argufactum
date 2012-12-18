package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public final class ArgumentThesis extends AbstractAuthoredEntity implements IdEntity, EventReport
{
	private static final long serialVersionUID = 7414341971508666893L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false)
	private String text;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	private Argument argument;

	@JoinColumn(updatable = false, nullable = false)
	private Boolean first;

	ArgumentThesis()
	{
	}

	public ArgumentThesis(String text, Author author, Argument argument)
	{
		super(author);
		if (StringUtils.isBlank(text) || argument == null)
			throw new IllegalArgumentException();
		this.text = StringUtils.normalizeSpace(text);
		this.first = argument.getArgumentThesis() == null ? Boolean.TRUE : Boolean.FALSE;
		this.argument = argument;
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

	public Argument getArgument()
	{
		return argument;
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
		if (!(obj instanceof ArgumentThesis))
			return false;

		final ArgumentThesis rhs = (ArgumentThesis)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.append(getText(), rhs.getText()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 23).append(getCreatedAt()).append(getAuthor()).append(getText()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ArgumentThesis [text=").append(text).append("]");
		return builder.toString();
	}

	@Override
	public LogEvent getEvent()
	{
		return LogEvent.UPDATE;
	}
}
