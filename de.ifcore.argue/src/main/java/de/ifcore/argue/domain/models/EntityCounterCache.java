package de.ifcore.argue.domain.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityCounterCache
{
	private final ConcurrentMap<Long, AtomicInteger> counterCache = new ConcurrentHashMap<>();

	public void increment(Long entityId)
	{
		AtomicInteger atomicInteger = counterCache.putIfAbsent(entityId, new AtomicInteger(1));
		if (atomicInteger != null)
			atomicInteger.incrementAndGet();
	}

	public int getVotesOfEntity(Long argumentId)
	{
		AtomicInteger atomicInteger = counterCache.get(argumentId);
		return atomicInteger == null ? 0 : atomicInteger.get();
	}

	public synchronized Set<Long> evictEntities()
	{
		Set<Long> entityIds = new HashSet<>();
		for (Long entityId : counterCache.keySet())
		{
			entityIds.add(entityId);
			counterCache.remove(entityId);
		}
		return Collections.unmodifiableSet(entityIds);
	}
}
