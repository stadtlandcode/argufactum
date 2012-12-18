package de.ifcore.argue.social;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.transaction.annotation.Transactional;

public class HibernateUsersConnectionRepository implements UsersConnectionRepository
{
	private final SessionFactory sessionFactory;
	private final ConnectionRepository connectionRepository = new HibernateConnectionRepository();

	@Inject
	public HibernateUsersConnectionRepository(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findUserIdsWithConnection(Connection<?> connection)
	{
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"SELECT c.registeredUser.id FROM RegisteredOAuthConnection c WHERE c.providerId = :providerId AND c.providerUserId = :providerUserId");
		query.setParameter("providerId", connection.getKey().getProviderId());
		query.setParameter("providerUserId", connection.getKey().getProviderUserId());
		Long userId = (Long)query.uniqueResult();

		return userId == null ? Collections.<String> emptyList() : Collections.unmodifiableList(Arrays.asList(userId
				.toString()));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds)
	{
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"SELECT c.registeredUser.id FROM RegisteredOAuthConnection c WHERE c.providerId = :providerId AND c.providerUserId IN (:providerUserIds)");
		query.setParameter("providerId", providerId);
		query.setParameterList("providerUserIds", providerUserIds);
		List<Long> userIds = query.list();
		Set<String> userStringIds = new HashSet<String>();
		for (Long userId : userIds)
			userStringIds.add(userId.toString());
		return Collections.unmodifiableSet(userStringIds);
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId)
	{
		return connectionRepository;
	}
}
