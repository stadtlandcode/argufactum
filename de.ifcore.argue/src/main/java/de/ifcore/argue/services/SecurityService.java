package de.ifcore.argue.services;

public interface SecurityService
{
	/**
	 * encodes the given password
	 * 
	 * @param rawPassword
	 * @return encoded rawPassword or null if rawPassword is null
	 */
	public String encodePassword(String rawPassword);

	public void autoLogin(Long userId);
}
