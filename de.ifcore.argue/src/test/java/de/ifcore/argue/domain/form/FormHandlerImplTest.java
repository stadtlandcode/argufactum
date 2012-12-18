package de.ifcore.argue.domain.form;

import static de.ifcore.argue.utils.EntityTestUtils.mockAuthor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.domain.entities.MockIdEntity;
import de.ifcore.argue.services.Service;

public class FormHandlerImplTest
{
	@Test
	public void testHandleForm()
	{
		// mock
		IdEntity idEntity = new MockIdEntity();
		Form<Service, IdEntity> form = new MockForm(idEntity);
		FormStorage formStorage = mock(FormStorage.class);
		Service service = mock(Service.class);
		FormHandlerImpl formHandler = new FormHandlerImpl(formStorage);

		// execute
		formHandler.handleForm(form, service, mockAuthor());
		when(formStorage.isStored(form)).thenReturn(Boolean.TRUE);
		formHandler.handleForm(form, service, mockAuthor());

		// verify
		verify(formStorage, times(2)).isStored(form);
		verify(formStorage).getStoredEntityId(form);
		verify(formStorage).store(form, idEntity);
	}

	private class MockForm implements Form<Service, IdEntity>
	{
		private IdEntity idEntity;

		private MockForm(IdEntity idEntity)
		{
			this.idEntity = idEntity;
		}

		@Override
		public IdEntity accept(Service service, Author author)
		{
			return idEntity;
		}

		@Override
		public Long getProcessId()
		{
			return idEntity.getId();
		}
	}

}
