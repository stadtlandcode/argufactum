package de.ifcore.argue.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class SelectedTopic
{
	@Id
	private Byte position;

	@OneToOne
	@JoinColumn(nullable = false)
	private Topic topic;

	SelectedTopic()
	{
	}

	public SelectedTopic(Byte position, Topic topic)
	{
		this.position = position;
		this.topic = topic;
	}

	public Byte getPosition()
	{
		return position;
	}

	public Topic getTopic()
	{
		return topic;
	}
}
