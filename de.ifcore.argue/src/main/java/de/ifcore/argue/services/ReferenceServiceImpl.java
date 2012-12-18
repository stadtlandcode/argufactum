package de.ifcore.argue.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.ReferenceDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.form.CreateReferenceForm;

@Service
public class ReferenceServiceImpl extends AbstractDeletableTopicEntityService<Reference, ReferenceDao> implements
		ReferenceService
{
	private static final Logger log = Logger.getLogger(ReferenceServiceImpl.class.getName());

	private final FactDao factDao;

	@Autowired
	public ReferenceServiceImpl(FactDao factDao, ReferenceDao referenceDao, TopicAccessRightService accessRightService)
	{
		super(referenceDao, accessRightService);
		this.factDao = factDao;
	}

	@Override
	@Transactional
	public Reference create(CreateReferenceForm form, Author author)
	{
		Fact fact = factDao.get(form.getFactId());
		accessRightService.checkAccess(fact, author.getRegisteredUser(), EntityPermission.WRITE);

		Reference reference = Reference.instanceOf(form.getText(), fact, author);
		entityDao.save(reference);
		log.info("create reference");
		return reference;
	}
}
