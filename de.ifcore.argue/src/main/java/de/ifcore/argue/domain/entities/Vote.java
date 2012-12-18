package de.ifcore.argue.domain.entities;

public interface Vote extends AuthoredEntity, IdEntity
{
	public IdEntity getDedicatedEntity();
}
