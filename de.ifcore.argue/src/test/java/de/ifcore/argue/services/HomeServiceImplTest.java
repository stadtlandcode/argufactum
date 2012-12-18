package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockArgument;
import static de.ifcore.argue.utils.EntityTestUtils.mockProContraTopic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.models.view.CategoryTopicsModel;
import de.ifcore.argue.domain.models.view.TopicModel;

public class HomeServiceImplTest
{
	@Test
	public void testGetLatestTopics()
	{
		TopicDao topicDao = mock(TopicDao.class);
		Topic filledTopic = mockArgument().getTopic().getTopic();
		ReflectionTestUtils.setField(filledTopic, "id", Long.valueOf(1));
		Topic filledTopic2 = mockArgument().getTopic().getTopic();
		ReflectionTestUtils.setField(filledTopic2, "id", Long.valueOf(2));
		Topic emptyTopic = mockProContraTopic().getTopic();
		ReflectionTestUtils.setField(emptyTopic, "id", Long.valueOf(3));

		when(topicDao.getLatestPublicTopics(anyInt())).thenReturn(Arrays.asList(filledTopic, filledTopic2, emptyTopic));
		MessageSource messageSource = mock(MessageSource.class);
		HomeServiceImpl homeService = new HomeServiceImpl(topicDao, null, messageSource);
		homeService.setMinNumberOfMainEntities(1);
		homeService.setNumberOfTopicsPerSection(1);

		Set<TopicModel> selectedTopics = new HashSet<>();
		selectedTopics.add(new TopicModel(filledTopic2, messageSource));
		ReflectionTestUtils.setField(homeService, "selectedTopics", selectedTopics);

		homeService.updateLatestTopics();
		Set<TopicModel> latestTopics = homeService.getLatestTopics();
		assertEquals(1, latestTopics.size());
		assertTrue(latestTopics.contains(new TopicModel(filledTopic, messageSource)));

		homeService.setNumberOfTopicsPerSection(2);
		homeService.updateLatestTopics();
		latestTopics = homeService.getLatestTopics();
		assertEquals(2, latestTopics.size());
		assertTrue(latestTopics.contains(new TopicModel(filledTopic, messageSource)));
		assertTrue(latestTopics.contains(new TopicModel(emptyTopic, messageSource)));
	}

	@Test
	public void testUpdateTopicsByCategory()
	{
		Topic filledTopic = mockArgument().getTopic().getTopic();
		Topic filledTopic2 = mockArgument().getTopic().getTopic();
		Topic filledTopic3 = mockArgument().getTopic().getTopic();
		Topic emptyTopic = mockProContraTopic().getTopic();
		Topic privateTopic = mockProContraTopic().getTopic();

		ReflectionTestUtils.setField(filledTopic, "id", Long.valueOf(1));
		ReflectionTestUtils.setField(filledTopic2, "id", Long.valueOf(2));
		ReflectionTestUtils.setField(filledTopic3, "id", Long.valueOf(3));
		ReflectionTestUtils.setField(emptyTopic, "id", Long.valueOf(4));
		ReflectionTestUtils.setField(privateTopic, "id", Long.valueOf(5));

		filledTopic.setVisibility(TopicVisibility.PUBLIC);
		filledTopic2.setVisibility(TopicVisibility.PUBLIC);
		filledTopic3.setVisibility(TopicVisibility.PUBLIC);
		emptyTopic.setVisibility(TopicVisibility.PUBLIC);
		privateTopic.setVisibility(TopicVisibility.PRIVATE);

		ReflectionTestUtils.setField(filledTopic2, "numberOfViews", Integer.valueOf(10));
		ReflectionTestUtils.setField(filledTopic3, "numberOfViews", Integer.valueOf(100));
		ReflectionTestUtils.setField(emptyTopic, "numberOfViews", Integer.valueOf(10000));

		Category category = new Category("Politik");
		ReflectionTestUtils.setField(category, "topics",
				new HashSet<>(Arrays.asList(emptyTopic, privateTopic, filledTopic2, filledTopic, filledTopic3)));

		MessageSource messageSource = mock(MessageSource.class);
		CategoryDao categoryDao = mock(CategoryDao.class);
		when(categoryDao.getAll()).thenReturn(Arrays.asList(category));
		HomeServiceImpl homeService = new HomeServiceImpl(null, categoryDao, messageSource);
		homeService.setNumberOfTopicsPerSection(10);
		homeService.updateTopicsByCategory();

		assertEquals(1, homeService.getTopicsByCategory().size());
		CategoryTopicsModel categoryTopicsModel = homeService.getTopicsByCategory().iterator().next();
		assertEquals(4, categoryTopicsModel.getTopics().size());
		assertEquals(Integer.valueOf(5), categoryTopicsModel.getTotalNumberOfTopics());

		Iterator<TopicModel> iterator = categoryTopicsModel.getTopics().iterator();
		assertEquals(filledTopic3.getId(), iterator.next().getId());
		assertEquals(filledTopic2.getId(), iterator.next().getId());
		assertEquals(filledTopic.getId(), iterator.next().getId());
		assertEquals(emptyTopic.getId(), iterator.next().getId());

		homeService.setNumberOfTopicsPerSection(2);
		homeService.updateTopicsByCategory();
		categoryTopicsModel = homeService.getTopicsByCategory().iterator().next();
		assertEquals(2, categoryTopicsModel.getTopics().size());

		iterator = categoryTopicsModel.getTopics().iterator();
		assertEquals(filledTopic3.getId(), iterator.next().getId());
		assertEquals(filledTopic2.getId(), iterator.next().getId());
	}
}
