package de.ifcore.argue.services;

import de.ifcore.argue.domain.email.VelocityEmail;

public class MockEmailService implements EmailService
{
	@Override
	public void send(VelocityEmail email)
	{
	}
}
