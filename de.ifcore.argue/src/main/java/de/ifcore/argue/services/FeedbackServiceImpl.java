package de.ifcore.argue.services;

import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.FeedbackDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Feedback;
import de.ifcore.argue.domain.form.FeedbackForm;

@Service
public class FeedbackServiceImpl implements FeedbackService
{
	private static final Logger log = Logger.getLogger(FeedbackServiceImpl.class.getName());

	private final FeedbackDao feedbackDao;

	@Inject
	public FeedbackServiceImpl(FeedbackDao feedbackDao)
	{
		this.feedbackDao = feedbackDao;
	}

	@Override
	@Transactional
	public Feedback save(FeedbackForm form, Author author)
	{
		Feedback feedback = new Feedback(form.getText(), BooleanUtils.toBoolean(form.getGrantPublication()), author);
		feedbackDao.save(feedback);
		log.info("save feedback");
		return feedback;
	}
}
