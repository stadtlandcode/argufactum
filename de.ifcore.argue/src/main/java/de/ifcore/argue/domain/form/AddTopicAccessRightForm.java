package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.validation.ExistingUsername;
import de.ifcore.argue.domain.validation.UniqueAccessRight;
import de.ifcore.argue.services.TopicAccessRightService;

@UniqueAccessRight
public class AddTopicAccessRightForm extends AbstractIdAjaxForm<TopicAccessRightService, TopicAccessRight>
{
	@NotBlank
	@ExistingUsername
	private String contact;

	@NotNull
	private EntityPermission permission;

	public String getContact()
	{
		return contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public EntityPermission getPermission()
	{
		return permission;
	}

	public void setPermission(EntityPermission permission)
	{
		this.permission = permission;
	}

	@Override
	public TopicAccessRight accept(TopicAccessRightService service, Author author)
	{
		return service.shareTo(this, author);
	}
}
