package de.ifcore.argue.domain.form;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.extensions.LRUCache;

public class FormStorageImpl implements FormStorage, Serializable
{
	private static final long serialVersionUID = 29762126527699733L;
	private static final int cacheCapacity = 3;
	private final Map<Integer, Object> processedForms = Collections.synchronizedMap(new LRUCache<Integer, Object>(
			cacheCapacity));

	@Override
	public boolean isStored(Form<?, ?> form)
	{
		return processedForms.containsKey(getHashCode(form));
	}

	@Override
	public void store(Form<?, ?> form, Object entityId)
	{
		processedForms.put(getHashCode(form), entityId);
	}

	@Override
	public Object getStoredEntityId(Form<?, ?> form)
	{
		return processedForms.get(getHashCode(form));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("FormStorageImpl [processedForms=").append(processedForms).append("]");
		return builder.toString();
	}

	public static int getCacheCapacity()
	{
		return cacheCapacity;
	}

	private Integer getHashCode(Form<?, ?> form)
	{
		return Integer.valueOf(HashCodeBuilder.reflectionHashCode(form, false));
	}
}
