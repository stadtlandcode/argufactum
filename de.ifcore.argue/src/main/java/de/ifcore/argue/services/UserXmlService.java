package de.ifcore.argue.services;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.utils.BeanUtils;

public class UserXmlService extends AbstractXmlImportService<RegisteredUser>
{
	private final UserDao userDao;
	private final SecurityService securityService;

	@Inject
	public UserXmlService(UserDao userDao, SecurityService securityService,
			PlatformTransactionManager transactionManager, String startupFile)
	{
		super(transactionManager, startupFile, null, "user");
		if (userDao == null || securityService == null)
			throw new IllegalArgumentException();
		this.userDao = userDao;
		this.securityService = securityService;
	}

	@Override
	public boolean checkPreconditions()
	{
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	protected RegisteredUser parseMainNode(Element element)
	{
		String username = getTagValue("username", element);
		String password = getTagValue("password", element);
		String email = getTagValue("email", element);
		Boolean admin = Boolean.valueOf(getTagValue("admin", element));
		if (userDao.findByEmail(email) != null || userDao.findByUsername(username) != null)
			return null;

		RegisteredUser user = RegisteredUser.register(username, email, securityService.encodePassword(password), null);
		user.setEmailVerified();
		BeanUtils.setField(user, "admin", admin);
		return user;
	}

	@Override
	@Transactional
	protected void saveObjects(Set<RegisteredUser> parsedObjects)
	{
		for (RegisteredUser user : parsedObjects)
			userDao.save(user);
	}
}
