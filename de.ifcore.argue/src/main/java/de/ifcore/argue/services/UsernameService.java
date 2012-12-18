package de.ifcore.argue.services;

import java.util.Set;

public interface UsernameService extends Service
{
	public String guessByEmail(String email);

	public String guess(String username);

	public Set<String> getUsernamesStartingWith(String fragment);
}
