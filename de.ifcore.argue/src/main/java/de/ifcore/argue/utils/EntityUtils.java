package de.ifcore.argue.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import de.ifcore.argue.domain.entities.CopiableNestedEntity;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.domain.entities.Vote;

public class EntityUtils
{
	public static boolean isDuplicateVote(Collection<? extends Vote> votes, Vote newVote)
	{
		if (votes.contains(newVote))
			return true;

		for (Vote vote : votes)
		{
			if (vote.getAuthor().getRegisteredUser().equals(newVote.getAuthor().getRegisteredUser()))
			{
				return true;
			}
		}

		return false;
	}

	public static <T, P> void copyNested(Collection<? extends CopiableNestedEntity<T, P>> entities, P parentEntity)
	{
		int timeDiff = entities.size();
		for (CopiableNestedEntity<T, P> entity : entities)
		{
			entity.copyTo(parentEntity, timeDiff);
			timeDiff--;
		}
	}

	public static <T extends DeletableTopicEntity> Set<T> getVisibleEntities(Set<T> allEntities)
	{
		Set<T> visibleEntities = new LinkedHashSet<>();
		for (T entity : allEntities)
		{
			if (!entity.isDeleted())
				visibleEntities.add(entity);
		}
		return Collections.unmodifiableSet(visibleEntities);
	}

	public static <T> Set<T> nullSafeSet(Set<T> entries)
	{
		return entries == null ? Collections.<T> emptySet() : Collections.unmodifiableSet(entries);
	}

	public static boolean equalsId(IdEntity obj1, IdEntity obj2)
	{
		if (obj1.getId() == null || obj2 == null || obj2.getId() == null)
		{
			return obj1.equals(obj2);
		}
		else
		{
			return obj1.getId().equals(obj2.getId());
		}
	}
}
