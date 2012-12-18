package de.ifcore.argue.extensions;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<A, B> extends LinkedHashMap<A, B>
{
	private static final long serialVersionUID = -3820263690198389010L;
	private final int maxEntries;

	public LRUCache(final int maxEntries)
	{
		super(maxEntries + 1, 1.0f, true);
		this.maxEntries = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<A, B> eldest)
	{
		return super.size() > maxEntries;
	}
}
