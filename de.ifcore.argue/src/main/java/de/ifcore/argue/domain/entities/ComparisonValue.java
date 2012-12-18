package de.ifcore.argue.domain.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import de.ifcore.argue.domain.report.ComparisonValueReport;

@Entity
public final class ComparisonValue extends AbstractAuthoredEntity implements ComparisonValueReport
{
	private static final long serialVersionUID = 9082267833153698999L;

	@EmbeddedId
	private ComparisonValuePk pk;

	@ManyToOne
	@MapsId("criterionId")
	@JoinColumn(updatable = false, nullable = false)
	private Criterion criterion;

	@ManyToOne
	@MapsId("optionId")
	@JoinColumn(updatable = false, nullable = false)
	private Option option;

	@Column(nullable = false)
	private String string;

	private Date updatedAt;

	@Embeddable
	public static class ComparisonValuePk implements Serializable
	{
		private static final long serialVersionUID = -389142774921793329L;

		private Long criterionId;
		private Long optionId;

		public Long getCriterionId()
		{
			return criterionId;
		}

		public Long getOptionId()
		{
			return optionId;
		}

		ComparisonValuePk()
		{
		}

		public ComparisonValuePk(Long criterionId, Long optionId)
		{
			this.criterionId = criterionId;
			this.optionId = optionId;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this)
				return true;
			if (!(obj instanceof ComparisonValuePk))
				return false;

			final ComparisonValuePk rhs = (ComparisonValuePk)obj;
			return new EqualsBuilder().append(optionId, rhs.getOptionId()).append(criterionId, rhs.getCriterionId())
					.isEquals();
		}

		@Override
		public int hashCode()
		{
			return new HashCodeBuilder(5, 53).append(optionId).append(criterionId).toHashCode();
		}
	}

	ComparisonValue()
	{
	}

	public ComparisonValue(Criterion criterion, Option option, String string, Author author)
	{
		super(author);
		if (criterion == null || option == null || string == null)
			throw new IllegalArgumentException();

		this.pk = new ComparisonValuePk(criterion.getId(), option.getId());
		this.criterion = criterion;
		this.option = option;
		this.string = string;
	}

	public Criterion getCriterion()
	{
		return criterion;
	}

	public Option getOption()
	{
		return option;
	}

	ComparisonValuePk getPk()
	{
		return pk;
	}

	@Override
	public String getString()
	{
		return string;
	}

	public void setString(String string)
	{
		this.string = string;
		this.updatedAt = new Date();
	}

	public DateTime getUpdatedAt()
	{
		return updatedAt == null ? null : new DateTime(updatedAt);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ComparisonValue))
			return false;

		final ComparisonValue rhs = (ComparisonValue)obj;
		return new EqualsBuilder().append(getPk(), rhs.getPk()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(7, 53).append(getPk()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValue [string=").append(string).append(", updatedAt=").append(updatedAt).append("]");
		return builder.toString();
	}

	@Override
	public Long getCriterionId()
	{
		return getCriterion().getId();
	}

	@Override
	public Long getOptionId()
	{
		return getOption().getId();
	}
}
