package de.ifcore.argue.domain.models;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import de.ifcore.argue.domain.entities.Author;

public class MockUserRequest implements UserRequest
{
	private final Author author;

	public MockUserRequest()
	{
		this.author = mockAuthor();
	}

	public MockUserRequest(UserSession userSession)
	{
		this.author = mockAuthor(userSession.getUser());
	}

	@Override
	public Author getAuthor()
	{
		return author;
	}
}
