package de.ifcore.argue.domain.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.ifcore.argue.domain.enumerations.LogEvent;

@Entity
public class FactLog extends AbstractLogEntity implements Comparable<FactLog>
{
	private static final long serialVersionUID = -8626948477052251478L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private Fact fact;

	FactLog()
	{
	}

	public FactLog(Fact fact, Author author, LogEvent event)
	{
		super(event, author);
		if (fact == null)
			throw new NullPointerException();
		this.fact = fact;
		fact.addLog(this);
	}

	public Fact getFact()
	{
		return fact;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof FactLog))
			return false;

		final FactLog rhs = (FactLog)obj;
		return super.equals(rhs);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode(7, 37);
	}

	@Override
	public int compareTo(FactLog o)
	{
		return super.compareToLog(o);
	}
}
