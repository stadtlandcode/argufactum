package de.ifcore.argue.dao;

import java.io.Serializable;

public interface EntityDao<T, PK extends Serializable> extends EntityCruDao<T, PK>
{
	public void delete(T object);
}
