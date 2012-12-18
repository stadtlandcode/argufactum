package de.ifcore.argue.domain.entities;

public class MockIdEntity implements IdEntity
{
	private static final long serialVersionUID = 8376394084961241478L;
	private final Long id;

	public MockIdEntity()
	{
		this(System.currentTimeMillis());
	}

	public MockIdEntity(Long id)
	{
		this.id = id;
	}

	@Override
	public Long getId()
	{
		return this.id;
	}
}
