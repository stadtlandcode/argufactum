package de.ifcore.argue.security;

import org.joda.time.DateTime;
import org.springframework.security.core.userdetails.UserDetails;

public interface ArgueUserDetails extends UserDetails
{
	public Long getUserId();

	public String getPersonName();

	public DateTime getCreatedAt();
}
