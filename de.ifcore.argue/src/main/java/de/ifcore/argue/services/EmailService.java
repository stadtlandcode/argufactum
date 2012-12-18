package de.ifcore.argue.services;

import de.ifcore.argue.domain.email.VelocityEmail;

public interface EmailService
{
	public void send(VelocityEmail email);
}
