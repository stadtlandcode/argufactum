package de.ifcore.argue.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.enumerations.SortOrder;

@Embeddable
public final class ComparisonValueNumberDefinition implements Serializable
{
	private static final long serialVersionUID = 5446366720648573045L;

	@Column(name = "max_number")
	private BigDecimal max;

	@Column(name = "min_number")
	private BigDecimal min;

	private SortOrder sortOrder;

	ComparisonValueNumberDefinition()
	{
	}

	/**
	 * @param min
	 * @param max
	 * @param sortOrder
	 *            ASC = less is better, DESC = more is better
	 */
	ComparisonValueNumberDefinition(BigDecimal min, BigDecimal max, SortOrder sortOrder)
	{
		if (sortOrder == null)
			throw new IllegalArgumentException();
		this.min = min;
		this.max = max;
		this.sortOrder = sortOrder;
	}

	public BigDecimal getMax()
	{
		return max;
	}

	public BigDecimal getMin()
	{
		return min;
	}

	public SortOrder getSortOrder()
	{
		return sortOrder;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ComparisonValueNumberDefinition))
			return false;

		final ComparisonValueNumberDefinition rhs = (ComparisonValueNumberDefinition)obj;
		return new EqualsBuilder().append(getMin(), rhs.getMin()).append(getMax(), rhs.getMax())
				.append(getSortOrder(), rhs.getSortOrder()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(61, 83).append(getMin()).append(getMax()).append(getSortOrder()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValueNumberDefinition [max=").append(max).append(", min=").append(min)
				.append(", sortOrder=").append(sortOrder).append("]");
		return builder.toString();
	}
}
