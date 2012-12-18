package de.ifcore.argue.services;

import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.EntityCruDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.form.DeleteTopicEntityForm;
import de.ifcore.argue.domain.form.RestoreTopicEntityForm;

public class AbstractDeletableTopicEntityService<E extends DeletableTopicEntity, D extends EntityCruDao<E, Long>>
		extends AbstractTopicEntityService<E, D> implements DeletableTopicEntityService<E>
{
	public AbstractDeletableTopicEntityService(D entityDao, TopicAccessRightService accessRightService)
	{
		super(entityDao, accessRightService);
	}

	@Override
	@Transactional
	public E delete(DeleteTopicEntityForm form, Author author)
	{
		E entity = entityDao.get(form.getId());
		accessRightService.checkAccess(entity, author.getRegisteredUser(), EntityPermission.WRITE);

		if (entity.delete(author))
			entityDao.update(entity);
		return entity;
	}

	@Override
	@Transactional
	public E restore(RestoreTopicEntityForm form, Author author)
	{
		E entity = entityDao.get(form.getId());
		accessRightService.checkAccess(entity, author.getRegisteredUser(), EntityPermission.WRITE);

		if (entity.restore(author))
			entityDao.update(entity);
		return entity;
	}
}
