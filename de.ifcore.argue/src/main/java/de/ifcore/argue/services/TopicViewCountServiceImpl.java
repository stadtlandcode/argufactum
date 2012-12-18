package de.ifcore.argue.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.extensions.LRUCache;

public class TopicViewCountServiceImpl implements TopicViewCountService
{
	private TopicDao topicDao;
	private Map<Long, Set<String>> topics;

	public TopicViewCountServiceImpl()
	{
	}

	public TopicViewCountServiceImpl(TopicDao topicDao, int cacheSize, int maxUpdateQueries)
	{
		this.topicDao = topicDao;
		this.topics = Collections.synchronizedMap(new LRUCache<Long, Set<String>>(cacheSize));
	}

	@Override
	public void count(Topic topic, Author author)
	{
		if (topic != null && topic.getId() != null && author.getIp() != null && author.getIp().length() >= 7)
		{
			Set<String> ips = topics.get(topic.getId());
			if (ips == null)
				ips = Collections.synchronizedSet(new HashSet<String>());
			if (ips.add(author.getIp()))
				topics.put(topic.getId(), ips);
		}
	}

	Map<Long, Set<String>> getTopics()
	{
		return Collections.unmodifiableMap(topics);
	}

	@Override
	@Transactional
	public void updateTopics()
	{
		Map<Long, Set<String>> topics = new HashMap<>(this.topics);
		this.topics.clear();

		for (Entry<Long, Set<String>> entry : topics.entrySet())
		{
			Topic topic = topicDao.get(entry.getKey());
			if (topic == null)
				continue;
			topic.increaseNumberOfViews(entry.getValue().size());
			topicDao.update(topic);
		}
	}
}
