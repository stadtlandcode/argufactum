package de.ifcore.argue.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "ci_uk", columnNames = { "registeredUser_id",
		"dedicatedEntity_id" }) })
public final class CriterionImportance extends AbstractVote<Criterion> implements IdEntity
{
	private static final long serialVersionUID = -4832091014157112210L;

	@Column(nullable = false)
	private Byte importance;

	private Date updatedAt;

	CriterionImportance()
	{
	}

	public CriterionImportance(Criterion criterion, Byte importance, Author author)
	{
		super(criterion, author);
		setImportance(importance);
	}

	public Byte getImportance()
	{
		return importance;
	}

	private void setImportance(Byte importance)
	{
		if (importance == null || importance.compareTo((byte)0) < 0)
			throw new IllegalArgumentException();
		this.importance = importance;
	}

	/**
	 * @param importance
	 * @param author
	 * @return false if edit is ignored (nothing changed)
	 */
	public boolean editImportance(Byte importance, Author author)
	{
		if (author == null || author.getRegisteredUser() == null
				|| !getAuthor().getRegisteredUser().equals(author.getRegisteredUser()))
			throw new IllegalArgumentException();

		if (this.importance.equals(importance))
		{
			return false;
		}
		else
		{
			setImportance(importance);
			this.updatedAt = new Date();
			return true;
		}
	}

	public DateTime getUpdatedAt()
	{
		return updatedAt == null ? null : new DateTime(updatedAt);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CriterionImportance [importance=").append(importance).append(", updatedAt=").append(updatedAt)
				.append("]");
		return builder.toString();
	}

	@Override
	protected void addToDedicatedEntity(Criterion dedicatedEntity)
	{
	}
}
