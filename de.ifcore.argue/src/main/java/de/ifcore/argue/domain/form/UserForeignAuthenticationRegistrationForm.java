package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.security.ForeignAuthentication;
import de.ifcore.argue.services.UserService;
import de.ifcore.argue.services.UsernameService;

public class UserForeignAuthenticationRegistrationForm extends AbstractUserRegistrationForm
{
	private String authenticationUuid;
	private String personName;

	public UserForeignAuthenticationRegistrationForm()
	{
	}

	public UserForeignAuthenticationRegistrationForm(ForeignAuthentication foreignAuthentication,
			UsernameService usernameService)
	{
		this.authenticationUuid = foreignAuthentication.getUuid().toString();
		this.email = foreignAuthentication.getEmail();
		this.personName = foreignAuthentication.getPersonName();
		this.username = foreignAuthentication.getUsername();

		if (this.username == null)
		{
			if (this.personName != null)
				this.username = usernameService.guess(this.personName);
			else if (this.email != null)
				this.username = usernameService.guessByEmail(this.email);
		}
		else if (this.username != null)
			this.username = usernameService.guess(username);
	}

	public String getAuthenticationUuid()
	{
		return authenticationUuid;
	}

	public void setAuthenticationUuid(String authenticationUuid)
	{
		this.authenticationUuid = authenticationUuid;
	}

	public String getPersonName()
	{
		return personName;
	}

	public void setPersonName(String personName)
	{
		this.personName = personName;
	}

	@Override
	public IdEntity accept(UserService service, Author author)
	{
		return service.register(this, author.getIp());
	}
}
