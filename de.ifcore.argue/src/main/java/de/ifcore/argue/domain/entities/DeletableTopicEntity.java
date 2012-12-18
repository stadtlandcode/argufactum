package de.ifcore.argue.domain.entities;

public interface DeletableTopicEntity extends TopicEntity
{
	public boolean delete(Author author);

	public boolean restore(Author author);

	public boolean isDeleted();
}
