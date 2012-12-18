package de.ifcore.argue.dao;

import java.io.Serializable;

public interface EntityCrDao<T, PK extends Serializable>
{
	public T get(PK id);

	public void save(T object);
}
