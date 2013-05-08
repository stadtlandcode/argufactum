package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.JsonString;

public interface JsonStorageService
{
	public JsonString get(String base36Id);

	public String store(String json);
}
