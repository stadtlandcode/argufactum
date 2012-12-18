package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Feedback;
import de.ifcore.argue.domain.form.FeedbackForm;

public interface FeedbackService extends Service
{
	public Feedback save(FeedbackForm form, Author author);
}
