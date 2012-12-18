package de.ifcore.argue.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.report.ComparisonValueDefinitionReport;

@Entity
public class ComparisonValueDefinition implements Serializable, Comparable<ComparisonValueDefinition>,
		ComparisonValueDefinitionReport
{
	private static final long serialVersionUID = -6904994662760615526L;

	@EmbeddedId
	private ComparisonValueDefinitionPk pk;

	@ManyToOne
	@MapsId("definitionSetId")
	@JoinColumn(updatable = false, nullable = false)
	private ComparisonValueDefinitionSet definitionSet;

	@Embeddable
	public static class ComparisonValueDefinitionPk implements Serializable
	{
		private static final long serialVersionUID = 4023865611514546339L;

		@Column(nullable = false, updatable = false)
		private String string;
		private Long definitionSetId;

		public String getString()
		{
			return string;
		}

		public Long getDefinitionSetId()
		{
			return definitionSetId;
		}

		ComparisonValueDefinitionPk()
		{
		}

		public ComparisonValueDefinitionPk(String string, Long definitionSetId)
		{
			if (string == null)
				throw new IllegalArgumentException();
			this.string = string;
			this.definitionSetId = definitionSetId;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this)
				return true;
			if (!(obj instanceof ComparisonValueDefinitionPk))
				return false;

			final ComparisonValueDefinitionPk rhs = (ComparisonValueDefinitionPk)obj;
			return new EqualsBuilder().append(string, rhs.getString())
					.append(definitionSetId, rhs.getDefinitionSetId()).isEquals();
		}

		@Override
		public int hashCode()
		{
			return new HashCodeBuilder(5, 53).append(string).append(definitionSetId).toHashCode();
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("ComparisonValueDefinitionPk [string=").append(string).append(", definitionSetId=")
					.append(definitionSetId).append("]");
			return builder.toString();
		}
	}

	@Column(nullable = false)
	private Byte percent;

	ComparisonValueDefinition()
	{
	}

	public ComparisonValueDefinition(ComparisonValueDefinitionSet definitionSet, String value, Byte percent)
	{
		if (definitionSet == null || percent == null || percent.intValue() < 0 || percent.intValue() > 100)
			throw new IllegalArgumentException();
		this.definitionSet = definitionSet;
		this.percent = percent;
		this.pk = new ComparisonValueDefinitionPk(value, definitionSet.getId());
		definitionSet.addDefinition(this);
	}

	public ComparisonValueDefinitionSet getDefinitionSet()
	{
		return definitionSet;
	}

	@Override
	public Byte getPercent()
	{
		return percent;
	}

	@Override
	public String getString()
	{
		return pk != null ? pk.string : null;
	}

	ComparisonValueDefinitionPk getPk()
	{
		return pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ComparisonValueDefinition))
			return false;

		final ComparisonValueDefinition rhs = (ComparisonValueDefinition)obj;
		return new EqualsBuilder().append(getPk(), rhs.getPk()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(3, 53).append(getPk()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValueDefinition [pk=").append(pk).append(", percent=").append(percent).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ComparisonValueDefinition o)
	{
		if (this.equals(o))
			return 0;

		int result = o.getPercent().compareTo(getPercent());
		if (result == 0)
			result = String.CASE_INSENSITIVE_ORDER.compare(getString(), o.getString());
		if (result == 0)
			result = getDefinitionSet().compareTo(o.getDefinitionSet());
		return result;
	}
}
