package de.ifcore.argue.security;

import java.util.UUID;

import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import de.ifcore.argue.domain.entities.RegisteredOpenIdIdentifier;
import de.ifcore.argue.domain.entities.RegisteredUser;

public final class OpenIdForeignAuthentication implements ForeignAuthentication
{
	private static final long serialVersionUID = -6092451003841797473L;
	private final String identifier;
	private final String email;
	private final UUID uuid;
	private final boolean trustable;

	public OpenIdForeignAuthentication(OpenIDAuthenticationToken token, boolean trustable)
	{
		this.identifier = token.getIdentityUrl();
		String email = null;
		for (OpenIDAttribute attribute : token.getAttributes())
		{
			if ("email".equals(attribute.getName()) && attribute.getValues() != null
					&& !attribute.getValues().isEmpty())
				email = attribute.getValues().get(0);
		}
		this.email = email;
		this.uuid = UUID.randomUUID();
		this.trustable = trustable;
	}

	@Override
	public void addToUser(RegisteredUser user)
	{
		new RegisteredOpenIdIdentifier(user, identifier);
	}

	@Override
	public String getEmail()
	{
		return email;
	}

	@Override
	public String getUsername()
	{
		return null;
	}

	@Override
	public String getPersonName()
	{
		return null;
	}

	@Override
	public UUID getUuid()
	{
		return uuid;
	}

	@Override
	public boolean isTrustable()
	{
		return trustable;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OpenIdForeignAuthentication [identifier=").append(identifier).append(", email=").append(email)
				.append(", uuid=").append(uuid).append(", trustable=").append(trustable).append("]");
		return builder.toString();
	}
}
