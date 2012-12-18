package de.ifcore.argue.dao;

import java.io.Serializable;

public interface EntityCruDao<T, PK extends Serializable> extends EntityCrDao<T, PK>
{
	public void update(T object);
}
