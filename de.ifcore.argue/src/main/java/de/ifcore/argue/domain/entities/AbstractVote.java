package de.ifcore.argue.domain.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@MappedSuperclass
public abstract class AbstractVote<T extends IdEntity> extends AbstractAuthoredEntity implements Vote
{
	private static final long serialVersionUID = -5816054370996863482L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(updatable = false, nullable = false)
	protected T dedicatedEntity;

	AbstractVote()
	{
	}

	AbstractVote(T dedicatedEntity, Author author)
	{
		super(author);
		if (author.getRegisteredUser() == null || dedicatedEntity == null)
			throw new IllegalArgumentException();
		this.dedicatedEntity = dedicatedEntity;
		addToDedicatedEntity(dedicatedEntity);
	}

	protected abstract void addToDedicatedEntity(T dedicatedEntity);

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public IdEntity getDedicatedEntity()
	{
		return dedicatedEntity;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof AbstractVote))
			return false;

		final Vote rhs = (Vote)obj;
		return new EqualsBuilder().append(getAuthor(), rhs.getAuthor())
				.append(getDedicatedEntity(), rhs.getDedicatedEntity()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(5, 17).append(getAuthor()).append(getDedicatedEntity()).toHashCode();
	}
}
