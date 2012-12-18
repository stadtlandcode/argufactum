package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.validation.RegistrationPasswordVerification;

@RegistrationPasswordVerification
public class RegistrationPasswordInput
{
	private String uuid;

	private String password;

	public RegistrationPasswordInput()
	{
	}

	public RegistrationPasswordInput(String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
