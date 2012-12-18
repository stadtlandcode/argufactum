package de.ifcore.argue.domain.form;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.services.Service;

public class MockFormObject implements Form<Service, IdEntity>
{
	private Long formId;
	private int counter;

	public MockFormObject(Long formId, int counter)
	{
		this.formId = formId;
		this.counter = counter;
	}

	@Override
	public Long getProcessId()
	{
		return formId;
	}

	public void setFormId(Long formId)
	{
		this.formId = formId;
	}

	public int getCounter()
	{
		return counter;
	}

	public void setCounter(int counter)
	{
		this.counter = counter;
	}

	@Override
	public IdEntity accept(Service service, Author author)
	{
		return null;
	}
}
