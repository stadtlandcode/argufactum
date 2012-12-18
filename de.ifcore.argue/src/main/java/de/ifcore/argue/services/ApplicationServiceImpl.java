package de.ifcore.argue.services;

public class ApplicationServiceImpl implements ApplicationService
{
	private final String version;
	private final String buildType;
	private final boolean useMinifiedCssJs;

	public ApplicationServiceImpl(String version, String buildType, boolean useMinifiedCssJs)
	{
		if (buildType == null)
			throw new IllegalArgumentException();

		this.version = version == null ? "0" : version;
		this.buildType = buildType;
		this.useMinifiedCssJs = useMinifiedCssJs;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public String getBuildType()
	{
		return buildType;
	}

	@Override
	public boolean useMinifiedCssJs()
	{
		return useMinifiedCssJs;
	}
}
