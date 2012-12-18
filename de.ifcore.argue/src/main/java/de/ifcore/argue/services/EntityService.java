package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.IdEntity;

public interface EntityService<T extends IdEntity> extends Service
{
	public T get(Long id);
}
