package de.ifcore.argue.services;

import de.ifcore.argue.domain.enumerations.EmailAddressTemplate;

public interface EmailAddressService
{
	public String getAddress(EmailAddressTemplate template);
}
