package de.ifcore.argue.services;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static de.ifcore.argue.utils.EntityTestUtils.mockCategory;
import static de.ifcore.argue.utils.EntityTestUtils.mockUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import de.ifcore.argue.dao.CategoryDao;
import de.ifcore.argue.dao.ProContraTopicDao;
import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.form.CreateTopicForm;

public class TopicServiceImplTest
{
	@Test
	public void testCreate()
	{
		CreateTopicForm form = new CreateTopicForm();
		form.setDiscussionType(DiscussionType.PRO_CONTRA);
		form.setTerm("12345");
		form.setVisibility(TopicVisibility.PRIVATE);
		form.setCategoryId(1l);

		Author author = mockAuthor(mockUser());
		Category category = mockCategory();
		TopicDao topicDao = mock(TopicDao.class);
		ProContraTopicDao proContraTopicDao = mock(ProContraTopicDao.class);
		TopicAccessRightService accessRightService = mock(TopicAccessRightService.class);
		DiscussionTypeService<?> proContraService = new ProContraServiceImpl(proContraTopicDao, accessRightService);
		CategoryDao categoryDao = mock(CategoryDao.class);
		when(categoryDao.get(form.getCategoryId())).thenReturn(category);
		TopicService service = new TopicServiceImpl(topicDao, accessRightService, categoryDao,
				new DiscussionTypeServiceFactoryImpl(Arrays.<DiscussionTypeService<?>> asList(proContraService)));

		Topic topic = service.create(form, author);
		assertEquals(author, topic.getAuthor());
		assertNotNull(topic.getProContraTopic());
		assertEquals(form.getDiscussionType(), topic.getDiscussionType());
		assertEquals(form.getTerm(), topic.getTerm().getText());
		assertEquals(category, topic.getCategory());
		verify(proContraTopicDao).save(topic.getProContraTopic());
	}
}
