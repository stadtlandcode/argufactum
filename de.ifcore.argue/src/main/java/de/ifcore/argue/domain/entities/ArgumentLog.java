package de.ifcore.argue.domain.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.ifcore.argue.domain.enumerations.LogEvent;

@Entity
public class ArgumentLog extends AbstractLogEntity implements Comparable<ArgumentLog>
{
	private static final long serialVersionUID = -8626948477052251478L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Argument argument;

	ArgumentLog()
	{
	}

	public ArgumentLog(Argument argument, Author author, LogEvent event)
	{
		super(event, author);
		if (argument == null)
			throw new NullPointerException();
		this.argument = argument;
		argument.addLog(this);
	}

	public Argument getArgument()
	{
		return argument;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ArgumentLog))
			return false;

		final ArgumentLog rhs = (ArgumentLog)obj;
		return super.equals(rhs);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode(3, 37);
	}

	@Override
	public int compareTo(ArgumentLog o)
	{
		return super.compareToLog(o);
	}
}
