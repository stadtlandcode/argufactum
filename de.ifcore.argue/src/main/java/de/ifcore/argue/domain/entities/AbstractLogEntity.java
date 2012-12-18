package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.comparators.LogReportComparator;
import de.ifcore.argue.domain.enumerations.LogEvent;
import de.ifcore.argue.domain.report.EventReport;

@MappedSuperclass
public abstract class AbstractLogEntity extends AbstractAuthoredEntity implements IdEntity, EventReport
{
	private static final long serialVersionUID = -8972515641825163586L;

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private LogEvent event;

	AbstractLogEntity()
	{
	}

	public AbstractLogEntity(LogEvent event, Author author)
	{
		super(author);
		if (event == null)
			throw new NullPointerException();
		this.event = event;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public LogEvent getEvent()
	{
		return event;
	}

	@Override
	public String getText()
	{
		return null;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Log [event=").append(getEvent()).append("]");
		return builder.toString();
	}

	protected boolean equals(AbstractLogEntity rhs)
	{
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.append(getEvent(), rhs.getEvent()).isEquals();
	}

	protected int hashCode(int n1, int n2)
	{
		return new HashCodeBuilder(n1, n2).append(getCreatedAt()).append(getAuthor()).append(getEvent()).toHashCode();
	}

	protected int compareToLog(AbstractLogEntity o)
	{
		if (this.equals(o))
			return 0;
		int result = LogReportComparator.getInstance().compare(this, o);
		if (result == 0)
			result = getEvent().compareTo(o.getEvent());
		if (result == 0)
			result = getId().compareTo(o.getId());
		return result;
	}
}
