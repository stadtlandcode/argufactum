package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.Email;

import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.domain.validation.UniqueUserEmail;
import de.ifcore.argue.domain.validation.UniqueUsername;
import de.ifcore.argue.domain.validation.Username;
import de.ifcore.argue.services.UserService;

public abstract class AbstractUserRegistrationForm extends AbstractForm<UserService, IdEntity>
{
	@Username
	@UniqueUsername
	protected String username;

	@NotNull
	@Email
	@UniqueUserEmail
	protected String email;

	@Null
	protected String city;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}
}
