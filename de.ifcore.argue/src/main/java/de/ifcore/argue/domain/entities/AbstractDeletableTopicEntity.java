package de.ifcore.argue.domain.entities;

import javax.persistence.MappedSuperclass;

import de.ifcore.argue.domain.enumerations.LogEvent;

@MappedSuperclass
public abstract class AbstractDeletableTopicEntity extends AbstractAuthoredEntity implements DeletableTopicEntity
{
	private static final long serialVersionUID = -8323196206529069807L;

	private Boolean deleted;

	AbstractDeletableTopicEntity()
	{
	}

	AbstractDeletableTopicEntity(Author author)
	{
		super(author);
	}

	AbstractDeletableTopicEntity(Author author, int timeDiff)
	{
		super(author, timeDiff);
	}

	@Override
	public boolean isDeleted()
	{
		return Boolean.TRUE.equals(deleted);
	}

	@Override
	public boolean delete(Author author)
	{
		if (isDeleted())
			return false;
		log(LogEvent.DELETE, author);
		this.deleted = Boolean.TRUE;
		return true;
	}

	@Override
	public boolean restore(Author author)
	{
		if (!isDeleted())
			return false;
		log(LogEvent.RESTORE, author);
		this.deleted = Boolean.FALSE;
		return true;
	}

	protected abstract void log(LogEvent logEvent, Author author);
}
