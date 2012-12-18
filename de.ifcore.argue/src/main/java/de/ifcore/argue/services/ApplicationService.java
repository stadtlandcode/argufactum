package de.ifcore.argue.services;

public interface ApplicationService
{
	/**
	 * @return current product version, never null
	 */
	public String getVersion();

	/**
	 * @return current BuildType
	 */
	public String getBuildType();

	boolean useMinifiedCssJs();
}
