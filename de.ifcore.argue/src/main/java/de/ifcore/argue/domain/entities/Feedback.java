package de.ifcore.argue.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Feedback extends AbstractAuthoredEntity
{
	private static final long serialVersionUID = 2157944447662770674L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false, length = 65536)
	private String text;

	@Column(nullable = false, updatable = false)
	private Boolean publicationGranted;

	Feedback()
	{
	}

	public Feedback(String text, boolean grantPublication, Author author)
	{
		super(author);
		if (text == null)
			throw new IllegalArgumentException();

		this.text = text;
		this.publicationGranted = Boolean.valueOf(grantPublication);
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public Boolean getPublicationGranted()
	{
		return publicationGranted;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Feedback))
			return false;

		final Feedback rhs = (Feedback)obj;
		return new EqualsBuilder().append(getText(), rhs.getText()).append(getCreatedAt(), rhs.getCreatedAt())
				.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(83, 89).append(getText()).append(getCreatedAt()).toHashCode();
	}

	@Override
	public String toString()
	{
		return "Feedback [id=" + id + ", text=" + text + ", publicationGranted=" + publicationGranted + "]";
	}
}
