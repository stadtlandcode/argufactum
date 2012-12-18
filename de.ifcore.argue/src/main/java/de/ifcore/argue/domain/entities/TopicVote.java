package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TopicVote extends AbstractVote<Topic>
{
	private static final long serialVersionUID = -29888283830478727L;

	@Column(nullable = false)
	private Boolean upVote;

	TopicVote()
	{

	}

	public TopicVote(boolean upVote, Topic topic, Author author)
	{
		super(topic, author);
		setVote(upVote);
	}

	@Override
	protected void addToDedicatedEntity(Topic topic)
	{
		topic.addVote(this);
	}

	public boolean isUpVote()
	{
		return Boolean.TRUE.equals(upVote);
	}

	public void setVote(boolean upVote)
	{
		this.upVote = Boolean.valueOf(upVote);
	}

	public Boolean getVote()
	{
		return upVote;
	}
}
