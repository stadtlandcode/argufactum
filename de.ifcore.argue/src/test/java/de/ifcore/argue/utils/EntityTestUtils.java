package de.ifcore.argue.utils;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;

import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.ComparisonTopic;
import de.ifcore.argue.domain.entities.ComparisonValueDefinitionSet;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public class EntityTestUtils
{
	public static Author mockAuthor()
	{
		return mockAuthor(null);
	}

	public static Author mockAuthor(RegisteredUser user)
	{
		return new Author(user, RandomStringUtils.randomAlphabetic(9), "127.0.0.1");
	}

	public static RegisteredUser mockUser()
	{
		return RegisteredUser.register(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)
				+ "@argufactum.de", "123456789", null);
	}

	public static Topic mockTopic()
	{
		return mockTopic(mockAuthor(mockUser()));
	}

	public static Topic mockTopic(Author author)
	{
		return new Topic(RandomStringUtils.randomAlphabetic(10), null, DiscussionType.PRO_CONTRA, author,
				TopicVisibility.PRIVATE, null);
	}

	public static ProContraTopic mockProContraTopic()
	{
		return new ProContraTopic(mockTopic());
	}

	public static ComparisonTopic mockComparisonTopic()
	{
		return new ComparisonTopic(mockTopic());
	}

	public static Criterion mockCriterion()
	{
		return mockCriterion(mockComparisonTopic());
	}

	public static Criterion mockCriterion(ComparisonTopic topic)
	{
		return new Criterion(topic, RandomStringUtils.randomAlphabetic(11), mockDefinitionSet(), mockAuthor());
	}

	public static ComparisonValueDefinitionSet mockDefinitionSet()
	{
		return mockDefinitionSet(CriterionDataType.TEXT);
	}

	public static ComparisonValueDefinitionSet mockDefinitionSet(CriterionDataType dataType)
	{
		return new ComparisonValueDefinitionSet(dataType, null, mockAuthor());
	}

	public static ComparisonValueDefinitionSet mockGlobalDefinitionSet()
	{
		return mockGlobalDefinitionSet(CriterionDataType.TEXT);
	}

	public static ComparisonValueDefinitionSet mockGlobalDefinitionSet(CriterionDataType dataType)
	{
		return ComparisonValueDefinitionSet.maintainedSet(dataType, null, RandomStringUtils.randomAlphabetic(10),
				RandomUtils.nextInt(101), true);
	}

	public static Option mockOption()
	{
		return mockOption(mockComparisonTopic());
	}

	public static Option mockOption(ComparisonTopic topic)
	{
		return new Option(topic, RandomStringUtils.randomAlphabetic(11), mockAuthor());
	}

	public static Argument mockArgument()
	{
		return new Argument(mockProContraTopic(), TopicThesis.PRO, RandomStringUtils.randomAlphabetic(11), mockAuthor());
	}

	public static Fact mockFact()
	{
		return new Fact(mockArgument(), FactType.CONFIRMATIVE, "12345", mockAuthor());
	}

	public static Reference mockReference()
	{
		return Reference.instanceOf(RandomStringUtils.randomAlphabetic(11), mockFact(), mockAuthor());
	}

	public static Category mockCategory()
	{
		return new Category(RandomStringUtils.randomAlphabetic(10));
	}

	public static TopicAccessRight mockTopicAccessRight(Topic topic)
	{
		return new TopicAccessRight(topic, topic.getAuthor().getRegisteredUser(), EntityPermission.WRITE);
	}
}
