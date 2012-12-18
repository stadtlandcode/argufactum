package de.ifcore.argue.services;

public interface UrlService
{
	/**
	 * 
	 * @param path
	 *            absolute path like in controllers RequestMapping
	 * @param secure
	 *            should the returning url start with https? can be overriden by config
	 * @return
	 */
	public String getAbsoluteUrl(String path, boolean secure);
}
