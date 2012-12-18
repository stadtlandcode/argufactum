package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.services.TopicService;

public class AddTopicVoteForm extends AbstractIdAjaxForm<TopicService, TopicVote>
{
	@NotNull
	private String vote;

	@Override
	public TopicVote accept(TopicService service, Author author)
	{
		return service.addVote(this, author);
	}

	public boolean isUpVote()
	{
		return "up".equals(getVote());
	}

	public String getVote()
	{
		return vote;
	}

	public void setVote(String vote)
	{
		this.vote = vote;
	}
}
