package de.ifcore.argue.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class LRUCacheTest
{
	@Test
	public void testRemove()
	{
		LRUCache<String, String> lruCache = new LRUCache<>(1);
		for (int i = 0; i < 100; i++)
		{
			lruCache.put(RandomStringUtils.random(5), RandomStringUtils.random(10));
		}
		lruCache.put("123", "456");

		assertEquals(1, lruCache.size());
		assertEquals("123", lruCache.keySet().iterator().next());
		assertEquals("456", lruCache.values().iterator().next());
	}

	@Test
	public void testLastUsed()
	{
		LRUCache<String, String> lruCache = new LRUCache<>(2);
		lruCache.put("123", "456");
		lruCache.put("234", "567");
		lruCache.put("123", "456");
		lruCache.put("345", "678");

		assertEquals(2, lruCache.size());
		assertTrue(lruCache.containsKey("123"));
		assertTrue(lruCache.containsKey("345"));
	}
}
