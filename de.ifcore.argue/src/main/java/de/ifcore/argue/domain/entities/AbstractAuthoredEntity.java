package de.ifcore.argue.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;

@MappedSuperclass
public abstract class AbstractAuthoredEntity implements AuthoredEntity
{
	private static final long serialVersionUID = 7549785242230506565L;

	@Column(updatable = false, nullable = false)
	private Date createdAt;

	private Author author;

	AbstractAuthoredEntity()
	{
	}

	AbstractAuthoredEntity(Author author)
	{
		this(author, 0);
	}

	AbstractAuthoredEntity(Author author, int timeDiff)
	{
		this.createdAt = new Date(System.currentTimeMillis() - timeDiff);
		this.author = author;
	}

	@Override
	public Author getAuthor()
	{
		return author;
	}

	@Override
	public DateTime getCreatedAt()
	{
		return createdAt == null ? null : new DateTime(createdAt);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractAuthoredEntity [createdAt=").append(createdAt).append(", author=").append(author)
				.append("]");
		return builder.toString();
	}

	@Override
	public String getAuthorName()
	{
		return author.getDisplayName();
	}

	@Override
	public Long getUserId()
	{
		return author.getUserId();
	}

	@Override
	public DateTime getDateTime()
	{
		return getCreatedAt();
	}
}
