package de.ifcore.argue.services;

import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.EntityCrDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.TopicEntity;
import de.ifcore.argue.domain.enumerations.EntityPermission;

public class AbstractTopicEntityService<E extends TopicEntity, D extends EntityCrDao<E, Long>> extends
		AbstractEntityService<E, D> implements TopicEntityService<E>
{
	protected final TopicAccessRightService accessRightService;

	public AbstractTopicEntityService(D entityDao, TopicAccessRightService accessRightService)
	{
		super(entityDao);
		this.accessRightService = accessRightService;
	}

	@Override
	@Transactional(readOnly = true)
	public E get(Long id, Author author)
	{
		E entity = get(id);
		accessRightService.checkAccess(entity, author.getRegisteredUser(), EntityPermission.READ);
		return entity;
	}
}
