package de.ifcore.argue.domain.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FormStorageImplTest
{
	@Test
	public void testStore()
	{
		FormStorage formStorage = new FormStorageImpl();
		MockFormObject formObject = new MockFormObject(1l, 10);
		Long entityId = Long.valueOf(100l);
		formStorage.store(formObject, entityId);

		assertEquals(entityId, formStorage.getStoredEntityId(formObject));
		assertNull(formStorage.getStoredEntityId(new MockFormObject(2l, 10)));

		assertTrue(formStorage.isStored(formObject));
		formObject.setCounter(11);
		assertFalse(formStorage.isStored(formObject));
		assertTrue(formStorage.isStored(new MockFormObject(1l, 10)));
		assertFalse(formStorage.isStored(new MockFormObject(2l, 10)));
	}

	@Test
	public void testCapacity()
	{
		FormStorage formStorage = new FormStorageImpl();
		List<Form<?, ?>> formObjects = new ArrayList<>();
		for (int i = 0; i < FormStorageImpl.getCacheCapacity() + 10; i++)
		{
			MockFormObject formObject = new MockFormObject((long)i, i);
			formObjects.add(formObject);
			formStorage.store(formObject, (long)i);
		}

		int foundCounter = 0;
		for (Form<?, ?> formObject : formObjects)
		{
			if (formStorage.isStored(formObject))
				foundCounter++;
		}
		assertEquals(FormStorageImpl.getCacheCapacity(), foundCounter);
		assertFalse(formStorage.isStored(formObjects.get(0)));
		assertTrue(formStorage.isStored(formObjects.get(10)));
	}
}
