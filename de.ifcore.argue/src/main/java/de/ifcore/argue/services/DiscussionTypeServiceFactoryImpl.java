package de.ifcore.argue.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import de.ifcore.argue.domain.enumerations.DiscussionType;

@Service
public class DiscussionTypeServiceFactoryImpl implements DiscussionTypeServiceFactory
{
	private final Map<DiscussionType, DiscussionTypeService<?>> services;

	@Inject
	public DiscussionTypeServiceFactoryImpl(List<DiscussionTypeService<?>> discussionTypeServices)
	{
		Map<DiscussionType, DiscussionTypeService<?>> services = new HashMap<>();
		for (DiscussionType discussionType : DiscussionType.values())
		{
			for (DiscussionTypeService<?> discussionTypeService : discussionTypeServices)
			{
				if (discussionTypeService.canHandle(discussionType))
				{
					services.put(discussionType, discussionTypeService);
					break;
				}
			}
		}
		this.services = Collections.unmodifiableMap(services);
	}

	@Override
	public DiscussionTypeService<?> getService(DiscussionType discussionType)
	{
		return services.get(discussionType);
	}

}
