package de.ifcore.argue.services;

public interface JsonStorageService
{
	public String get(String base36Id);

	public String store(String json);
}
