package de.ifcore.argue.services;

import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.EntityCrDao;
import de.ifcore.argue.domain.entities.IdEntity;

public abstract class AbstractEntityService<E extends IdEntity, D extends EntityCrDao<E, Long>> implements
		EntityService<E>
{
	protected final D entityDao;

	public AbstractEntityService(D entityDao)
	{
		this.entityDao = entityDao;
	}

	@Override
	@Transactional(readOnly = true)
	public E get(Long id)
	{
		return entityDao.get(id);
	}
}
