package de.ifcore.argue.domain.models.view;

public class TopicVoteModel implements ViewModel
{
	private static final long serialVersionUID = -2247790056222033969L;
	private final Boolean vote;

	public TopicVoteModel(Boolean vote)
	{
		this.vote = vote;
	}

	public Boolean getVote()
	{
		return vote;
	}
}
