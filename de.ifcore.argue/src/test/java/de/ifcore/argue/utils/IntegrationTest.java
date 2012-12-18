package de.ifcore.argue.utils;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.ArgumentDao;
import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.ComparisonTopicDao;
import de.ifcore.argue.dao.ComparisonValueDao;
import de.ifcore.argue.dao.ComparisonValueDefinitionDao;
import de.ifcore.argue.dao.CriterionDao;
import de.ifcore.argue.dao.FactDao;
import de.ifcore.argue.dao.OptionDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.dao.SelectedTopicDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;

@TransactionConfiguration(defaultRollback = true)
@Transactional
@ContextConfiguration(locations = { "/applicationContext.xml" })
public abstract class IntegrationTest
{
	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	protected TopicDao topicDao;

	@Autowired
	protected ProContraTopicDao proContraTopicDao;

	@Autowired
	protected ComparisonTopicDao comparisonTopicDao;

	@Autowired
	protected ArgumentDao argumentDao;

	@Autowired
	protected FactDao factDao;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected CriterionDao criterionDao;

	@Autowired
	protected OptionDao optionDao;

	@Autowired
	protected ComparisonValueDao comparisonValueDao;

	@Autowired
	protected ComparisonValueDefinitionDao comparisonValueDefinitionDao;

	@Autowired
	protected CategoryDao categoryDao;

	@Autowired
	protected SelectedTopicDao selectedTopicDao;

	protected void flush()
	{
		sessionFactory.getCurrentSession().flush();
	}

	protected void flushAndClear()
	{
		flush();
		sessionFactory.getCurrentSession().clear();
	}

	protected Topic persistTopic(Topic topic)
	{
		if (topic.getAuthor().getRegisteredUser() != null)
			persistUser(topic.getAuthor().getRegisteredUser());
		topicDao.save(topic);
		return topic;
	}

	protected ProContraTopic persistProContraTopic(ProContraTopic proContraTopic)
	{
		if (proContraTopic.getTopic().getAuthor().getRegisteredUser() != null)
			persistUser(proContraTopic.getTopic().getAuthor().getRegisteredUser());
		proContraTopicDao.save(proContraTopic);
		return proContraTopic;
	}

	protected ComparisonTopic persistComparisonTopic(ComparisonTopic comparisonTopic)
	{
		if (comparisonTopic.getTopic().getAuthor().getRegisteredUser() != null)
			persistUser(comparisonTopic.getTopic().getAuthor().getRegisteredUser());
		comparisonTopicDao.save(comparisonTopic);
		return comparisonTopic;
	}

	protected Criterion persistCriterion(Criterion criterion)
	{
		persistComparisonTopic(criterion.getTopic());
		comparisonValueDefinitionDao.saveSet(criterion.getDefinitionSet());
		criterionDao.save(criterion);
		return criterion;
	}

	protected ComparisonValueDefinitionSet persistDefinitionSet(ComparisonValueDefinitionSet definitionSet)
	{
		comparisonValueDefinitionDao.saveSet(definitionSet);
		return definitionSet;
	}

	protected Option persistOption(Option option)
	{
		persistComparisonTopic(option.getTopic());
		optionDao.save(option);
		return option;
	}

	protected Argument persistArgument(Argument argument)
	{
		persistProContraTopic(argument.getTopic());
		argumentDao.save(argument);
		return argument;
	}

	protected Fact persistFact(Fact fact)
	{
		persistArgument(fact.getArgument());
		factDao.save(fact);
		return fact;
	}

	protected RegisteredUser persistUser(RegisteredUser user)
	{
		userDao.save(user);
		return user;
	}

	protected Category persistCategory(Category category)
	{
		categoryDao.save(category);
		return category;
	}

	protected TopicAccessRight persistTopicAccessRight(TopicAccessRight accessRight)
	{
		persistTopic(accessRight.getTopic());
		persistUser(accessRight.getRegisteredUser());
		topicDao.saveAccessRight(accessRight);
		return accessRight;
	}
}
