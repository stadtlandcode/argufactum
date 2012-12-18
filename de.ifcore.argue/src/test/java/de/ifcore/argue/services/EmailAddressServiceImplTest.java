package de.ifcore.argue.services;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.ifcore.argue.domain.enumerations.EmailAddressTemplate;

public class EmailAddressServiceImplTest
{
	@Test
	public void testGetAddress()
	{
		Map<String, String> addressMap = new HashMap<>();
		String email = "info@argufactum.de";
		for (EmailAddressTemplate template : EmailAddressTemplate.values())
			addressMap.put(template.toString(), email);

		EmailAddressServiceImpl emailAddressServiceImpl = new EmailAddressServiceImpl(addressMap);
		assertEquals(email, emailAddressServiceImpl.getAddress(EmailAddressTemplate.REGISTRATION));
	}
}
