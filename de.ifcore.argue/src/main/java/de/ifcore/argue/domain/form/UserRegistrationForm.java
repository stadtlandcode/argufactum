package de.ifcore.argue.domain.form;

import javax.validation.Valid;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.services.UserService;

public class UserRegistrationForm extends AbstractUserRegistrationForm
{
	@Valid
	private RegistrationPasswordInput passwordInput;

	public RegistrationPasswordInput getPasswordInput()
	{
		return passwordInput;
	}

	public void setPasswordInput(RegistrationPasswordInput passwordInput)
	{
		this.passwordInput = passwordInput;
	}

	@Override
	public IdEntity accept(UserService service, Author author)
	{
		return service.register(this, author.getIp());
	}
}
