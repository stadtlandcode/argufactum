package de.ifcore.argue.services;

import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.form.EditProContraThesesForm;

@Service
public class ProContraServiceImpl implements ProContraService
{
	private static final Logger log = Logger.getLogger(ProContraServiceImpl.class.getName());

	private final ProContraTopicDao proContraTopicDao;
	private final TopicAccessRightService accessRightService;

	@Inject
	public ProContraServiceImpl(ProContraTopicDao proContraTopicDao, TopicAccessRightService accessRightService)
	{
		this.proContraTopicDao = proContraTopicDao;
		this.accessRightService = accessRightService;
	}

	@Override
	@Transactional
	public ProContraTopic create(Topic topic)
	{
		ProContraTopic proContraTopic = new ProContraTopic(topic);
		proContraTopicDao.save(proContraTopic);
		log.info("create proContra topic");
		return proContraTopic;
	}

	@Override
	public boolean canHandle(DiscussionType discussionType)
	{
		return DiscussionType.PRO_CONTRA.equals(discussionType);
	}

	@Override
	@Transactional
	public ProContraTopic editTheses(EditProContraThesesForm form, Author author)
	{
		ProContraTopic proContraTopic = proContraTopicDao.get(form.getTopicId());
		accessRightService.checkAccess(proContraTopic, author.getRegisteredUser(), EntityPermission.WRITE);
		proContraTopic.setThesesMessageCodeAppendix(form.getMessageCodeAppendix());
		proContraTopicDao.update(proContraTopic);
		log.info("edit proContra theses");
		return proContraTopic;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Argument> getDeletedArguments(Long topicId, TopicThesis topicThesis, Author author)
	{
		ProContraTopic proContraTopic = proContraTopicDao.get(topicId);
		accessRightService.checkAccess(proContraTopic, author.getRegisteredUser(), EntityPermission.WRITE);
		log.info("get deleted arguments");
		return proContraTopic.getDeletedArguments(topicThesis);
	}

	@Override
	@Transactional
	public void saveWithDescendants(Topic topic)
	{
		proContraTopicDao.saveWithDescendants(topic.getProContraTopic());
	}
}
