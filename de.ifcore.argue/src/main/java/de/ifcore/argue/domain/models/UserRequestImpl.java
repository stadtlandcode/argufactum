package de.ifcore.argue.domain.models;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.RegisteredUser;

public final class UserRequestImpl implements UserRequest
{
	private final Author author;

	@Inject
	public UserRequestImpl(HttpServletRequest request, UserSession session, boolean behindProxy)
	{
		if (request == null || session == null)
			throw new NullPointerException();

		RegisteredUser user = session.getUser();
		String ip = behindProxy ? request.getHeader("X-Real-IP") : request.getRemoteAddr();
		String username = user == null ? ip : user.getUsername();
		this.author = new Author(user, username, ip);
	}

	@Override
	public Author getAuthor()
	{
		return author;
	}
}
