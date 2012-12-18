package de.ifcore.argue.services;

import java.util.Set;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.domain.form.AddTopicVoteForm;
import de.ifcore.argue.domain.form.CreateTopicForm;
import de.ifcore.argue.domain.form.EditTopicForm;
import de.ifcore.argue.domain.form.RemoveTopicVoteForm;
import de.ifcore.argue.domain.report.TopicEventReport;

public interface TopicService extends TopicEntityService<Topic>
{
	public Topic create(CreateTopicForm form, Author author);

	public Topic edit(EditTopicForm form, Author author);

	public Set<TopicEventReport> getEventReports(Long id, Author author);

	public Topic copy(Long id, Author author);

	public TopicVote addVote(AddTopicVoteForm form, Author author);

	public Topic removeVote(RemoveTopicVoteForm form, Author author);
}
