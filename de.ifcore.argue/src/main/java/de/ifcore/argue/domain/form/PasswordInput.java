package de.ifcore.argue.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.ifcore.argue.domain.validation.PasswordVerification;

@PasswordVerification
public class PasswordInput
{
	@NotNull
	@Size(min = 6, max = 128)
	private String password;

	private String password2;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword2()
	{
		return password2;
	}

	public void setPassword2(String password2)
	{
		this.password2 = password2;
	}
}
