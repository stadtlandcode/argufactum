package de.ifcore.argue.services;

public class UrlServiceImpl implements UrlService
{
	private static final String protocol = "http://";
	private final String secureProtocol;
	private final String absoluteUrlFragment;

	/**
	 * @param absoluteUrlFragment
	 *            absolute url containing deploy path but without protocol and trailing slash. examples:
	 *            www.argufactum.de, localhost:8080/de.ifcore.argue
	 * @param secureProtocol
	 *            http or https
	 */
	public UrlServiceImpl(String absoluteUrlFragment, String secureProtocol)
	{
		if (absoluteUrlFragment == null || secureProtocol == null)
			throw new NullPointerException();
		if (!"http".equals(secureProtocol) && !"https".equals(secureProtocol))
			throw new IllegalArgumentException("protocol " + secureProtocol + " is not equal to http or https");

		this.absoluteUrlFragment = absoluteUrlFragment;
		this.secureProtocol = secureProtocol + "://";
	}

	@Override
	public String getAbsoluteUrl(String path, boolean secure)
	{
		String protocol = secure ? this.secureProtocol : UrlServiceImpl.protocol;
		return protocol + absoluteUrlFragment + path;
	}
}
