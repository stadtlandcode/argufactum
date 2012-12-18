package de.ifcore.argue.dao.hibernate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.EmailVerificationRequest;
import de.ifcore.argue.domain.entities.RegisteredUser;

public class HibernateUserDao extends HibernateAbstractEntityDao<RegisteredUser, Long> implements UserDao
{
	@Inject
	public HibernateUserDao(SessionFactory sessionFactory)
	{
		super(RegisteredUser.class, sessionFactory);
	}

	@Override
	public RegisteredUser findByUsername(String name)
	{
		Query query = getSession().createQuery("FROM RegisteredUser WHERE usernameNormalized=:name");
		query.setParameter("name", RegisteredUser.formatForUsernameNormalized(name));
		query.setCacheable(true);
		RegisteredUser user = (RegisteredUser)query.uniqueResult();
		return user;
	}

	@Override
	public RegisteredUser findByEmail(String email)
	{
		Query query = getSession().createQuery("FROM RegisteredUser WHERE email=:email");
		query.setParameter("email", RegisteredUser.formatForEmail(email));
		query.setCacheable(true);
		RegisteredUser user = (RegisteredUser)query.uniqueResult();
		return user;
	}

	@Override
	public RegisteredUser findByNormalizedEmail(String email)
	{
		Query query = getSession().createQuery("FROM RegisteredUser WHERE emailNormalized=:email");
		query.setParameter("email", RegisteredUser.formatForEmailNormalized(email));
		query.setCacheable(true);
		RegisteredUser user = (RegisteredUser)query.uniqueResult();
		return user;
	}

	@Override
	public RegisteredUser findByOpenIdIdentifier(String identifier)
	{
		Query query = getSession().createQuery(
				"SELECT registeredUser FROM RegisteredOpenIdIdentifier WHERE identifier=:identifier");
		query.setParameter("identifier", identifier);
		query.setCacheable(true);
		RegisteredUser user = (RegisteredUser)query.uniqueResult();
		return user;
	}

	@Override
	public void saveEmailVerificationRequest(EmailVerificationRequest request)
	{
		getSession().persist(request);
	}

	@Override
	public void updateEmailVerificationRequest(EmailVerificationRequest request)
	{
		getSession().merge(request);
	}

	@Override
	public EmailVerificationRequest getEmailVerificationRequest(String verificationKey)
	{
		Query query = getSession().createQuery("FROM EmailVerificationRequest WHERE verificationKey=:key");
		query.setParameter("key", verificationKey);
		query.setCacheable(true);
		EmailVerificationRequest request = (EmailVerificationRequest)query.uniqueResult();
		return request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findUsernamesStartingWith(String username)
	{
		Query query = getSession().createQuery("SELECT username FROM RegisteredUser WHERE LOWER(username) LIKE :name");
		query.setParameter("name", username.toLowerCase() + "%");
		query.setCacheable(true);
		Set<String> usernames = new HashSet<>(query.list());
		return Collections.unmodifiableSet(usernames);
	}

}
