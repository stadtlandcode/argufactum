package de.ifcore.argue.dao;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.entities.Feedback;
import de.ifcore.argue.utils.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class FeedbackDaoTest extends IntegrationTest
{
	@Autowired
	private FeedbackDao feedbackDao;

	@Test
	public void testSave()
	{
		Feedback feedback = new Feedback("abc", true, mockAuthor());
		feedbackDao.save(feedback);
		flushAndClear();

		Feedback persistedFeedback = feedbackDao.get(feedback.getId());
		assertEquals(feedback, persistedFeedback);
	}
}
