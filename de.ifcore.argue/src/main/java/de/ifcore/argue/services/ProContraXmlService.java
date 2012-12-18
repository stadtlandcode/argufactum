package de.ifcore.argue.services;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class ProContraXmlService extends AbstractXmlImportService<ProContraTopic>
{
	private final TopicDao topicDao;
	private final ProContraTopicDao proContraTopicDao;
	private final UserDao userDao;
	private final CategoryDao categoryDao;

	@Inject
	public ProContraXmlService(TopicDao topicDao, ProContraTopicDao proContraTopicDao, UserDao userDao,
			CategoryDao categoryDao, PlatformTransactionManager transactionManager, String directory)
	{
		super(transactionManager, null, directory, "proContraTopic");
		if (topicDao == null || proContraTopicDao == null || userDao == null)
			throw new IllegalArgumentException();
		this.topicDao = topicDao;
		this.proContraTopicDao = proContraTopicDao;
		this.userDao = userDao;
		this.categoryDao = categoryDao;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkPreconditions()
	{
		return topicDao.getLatestPublicTopics(1).isEmpty();
	}

	@Override
	@Transactional(readOnly = true)
	protected ProContraTopic parseMainNode(Element element)
	{
		String term = getTagValue("term", element);
		String definition = getOptionalTagValue("definition", element);
		RegisteredUser user = userDao.findByUsername(getTagValue("user", element));
		TopicThesesMessageCodeAppendix topicTheses = TopicThesesMessageCodeAppendix.valueOf(getTagValue("theses",
				element));
		Category category = categoryDao.findByName(getTagValue("category", element));

		Topic topic = new Topic(term, definition, DiscussionType.PRO_CONTRA, new Author(user, null),
				TopicVisibility.PUBLIC, category);
		ProContraTopic proContraTopic = new ProContraTopic(topic);
		proContraTopic.setThesesMessageCodeAppendix(topicTheses);

		parseArguments(element.getElementsByTagName("argument"), proContraTopic);
		return proContraTopic;
	}

	private void parseArguments(NodeList argumentNodes, ProContraTopic proContraTopic)
	{
		int timeDiff = 180;
		for (int i = 0; i < argumentNodes.getLength(); i++)
		{
			Element element = (Element)argumentNodes.item(i);
			TopicThesis topicThesis = TopicThesis.valueOf(getTagValue("topicThesis", element));
			String thesis = getTagValue("thesis", element);
			Argument argument = new Argument(proContraTopic, topicThesis, thesis,
					proContraTopic.getTopic().getAuthor(), timeDiff - i, null);

			parseFacts(element.getElementsByTagName("fact"), argument);
		}
	}

	private void parseFacts(NodeList factNodes, Argument argument)
	{
		int timeDiff = 120;
		if (factNodes != null && factNodes.getLength() > 0)
		{
			for (int i = 0; i < factNodes.getLength(); i++)
			{
				Element element = (Element)factNodes.item(i);
				String evidence = getTagValue("evidence", element);
				FactType factType = FactType.valueOf(getTagValue("type", element));
				Fact fact = new Fact(argument, factType, evidence, argument.getAuthor(), timeDiff - i, null);

				parseReferences(element.getElementsByTagName("reference"), fact);
			}
		}
	}

	private void parseReferences(NodeList referenceNodes, Fact fact)
	{
		if (referenceNodes != null && referenceNodes.getLength() > 0)
		{
			for (int i = 0; i < referenceNodes.getLength(); i++)
			{
				Element element = (Element)referenceNodes.item(i);
				String text = element.getChildNodes().item(0).getNodeValue();
				Reference.instanceOf(text, fact, fact.getAuthor());
			}
		}
	}

	@Override
	@Transactional
	protected void saveObjects(Set<ProContraTopic> topics)
	{
		for (ProContraTopic proContraTopic : topics)
		{
			proContraTopicDao.saveWithDescendants(proContraTopic);
		}
	}
}
