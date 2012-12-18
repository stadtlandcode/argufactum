package de.ifcore.argue.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.ifcore.argue.domain.enumerations.EmailAddressTemplate;

public class EmailAddressServiceImpl implements EmailAddressService
{
	private final Map<EmailAddressTemplate, String> templateToAddressMap;

	public EmailAddressServiceImpl(Map<String, String> addressMap)
	{
		Map<EmailAddressTemplate, String> templateToAddressMap = new HashMap<>();
		for (EmailAddressTemplate template : EmailAddressTemplate.values())
		{
			String address = addressMap.get(template.toString());
			if (address == null)
				throw new IllegalArgumentException();
			templateToAddressMap.put(template, address);
		}
		this.templateToAddressMap = Collections.unmodifiableMap(templateToAddressMap);
	}

	@Override
	public String getAddress(EmailAddressTemplate template)
	{
		return templateToAddressMap.get(template);
	}
}
