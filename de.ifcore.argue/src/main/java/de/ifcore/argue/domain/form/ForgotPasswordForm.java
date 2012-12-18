package de.ifcore.argue.domain.form;

import org.hibernate.validator.constraints.NotBlank;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.services.UserService;

public class ForgotPasswordForm extends AbstractForm<UserService, IdEntity>
{
	@NotBlank
	private String email;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public IdEntity accept(UserService service, Author author)
	{
		service.sendResetPasswordEmail(this, author);
		return null;
	}
}
