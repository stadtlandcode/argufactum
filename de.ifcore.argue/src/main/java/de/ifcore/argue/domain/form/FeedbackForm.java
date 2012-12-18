package de.ifcore.argue.domain.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Feedback;
import de.ifcore.argue.services.FeedbackService;

public class FeedbackForm extends AbstractForm<FeedbackService, Feedback>
{
	@Size(max = 65000)
	@NotBlank
	private String text;

	private Boolean grantPublication;

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Boolean getGrantPublication()
	{
		return grantPublication;
	}

	public void setGrantPublication(Boolean grantPublication)
	{
		this.grantPublication = grantPublication;
	}

	@Override
	public Feedback accept(FeedbackService service, Author author)
	{
		return service.save(this, author);
	}
}
