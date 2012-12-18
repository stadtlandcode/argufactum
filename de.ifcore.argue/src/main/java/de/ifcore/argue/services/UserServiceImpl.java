package de.ifcore.argue.services;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import de.ifcore.argue.dao.UserDao;
import de.ifcore.argue.domain.email.VelocityEmail;
import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.EmailVerificationRequest;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.EmailAddressTemplate;
import de.ifcore.argue.domain.form.ForgotPasswordForm;
import de.ifcore.argue.domain.form.UserForeignAuthenticationRegistrationForm;
import de.ifcore.argue.domain.form.UserRegistrationForm;
import de.ifcore.argue.domain.models.ArgueUrl;
import de.ifcore.argue.domain.models.UserSession;
import de.ifcore.argue.security.ForeignAuthentication;

@Service
public class UserServiceImpl implements UserService
{
	private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());

	private final UserDao userDao;
	private final SecurityService securityService;
	private final EmailService emailService;
	private final UserSession userSession;
	private final UrlService urlService;

	@Inject
	public UserServiceImpl(UserDao userDao, SecurityService securityService, EmailService emailService,
			UserSession userSession, UrlService urlService)
	{
		this.userDao = userDao;
		this.securityService = securityService;
		this.emailService = emailService;
		this.userSession = userSession;
		this.urlService = urlService;
	}

	@Override
	@Transactional
	public RegisteredUser register(UserRegistrationForm form, String ip)
	{
		String encryptedPassword;
		if (form.getPasswordInput().getPassword() == null)
			encryptedPassword = userSession.getStoredPassword(form.getPasswordInput().getUuid());
		else
			encryptedPassword = securityService.encodePassword(form.getPasswordInput().getPassword());
		RegisteredUser user = RegisteredUser.register(form.getUsername(), form.getEmail(), encryptedPassword, ip);
		log.info("register user (password authentication)");
		return register(user, true);
	}

	@Override
	@Transactional
	public RegisteredUser register(UserForeignAuthenticationRegistrationForm form, String ip)
	{
		ForeignAuthentication auth = userSession.getForeignAuthentication(form.getAuthenticationUuid());
		RegisteredUser user = RegisteredUser.register(form.getUsername(), form.getEmail(), auth, ip);
		boolean verify = !auth.isTrustable() || !StringUtils.equalsIgnoreCase(form.getEmail(), auth.getEmail());
		log.info("register user (openid / oauth authentication)");
		return register(user, verify);
	}

	private RegisteredUser register(RegisteredUser user, boolean verify)
	{
		userDao.save(user);
		if (verify)
			requestEmailVerification(user);
		return user;
	}

	private void requestEmailVerification(RegisteredUser user)
	{
		EmailVerificationRequest request = new EmailVerificationRequest(user);
		UriTemplate uriTemplate = new UriTemplate(urlService.getAbsoluteUrl(ArgueUrl.VERIFY_EMAIL, true));
		Map<String, Object> model = new HashMap<>();
		model.put("user", request.getRegisteredUser());
		model.put("verificationUrl", uriTemplate.expand(request.getVerificationKey()).toString());
		VelocityEmail email = new VelocityEmail("registration", EmailAddressTemplate.REGISTRATION, request.getEmail(),
				model);
		emailService.send(email);
		userDao.saveEmailVerificationRequest(request);
	}

	@Override
	@Transactional
	public boolean verifyEmail(String verificationKey)
	{
		EmailVerificationRequest verificationRequest = userDao.getEmailVerificationRequest(verificationKey);
		if (verificationRequest == null || !verificationRequest.isEnabled())
		{
			return false;
		}
		else
		{
			if (!verificationRequest.isVerified())
			{
				verificationRequest.verify();
				userDao.updateEmailVerificationRequest(verificationRequest);
				userDao.update(verificationRequest.getRegisteredUser());
				log.info("verify email");
			}
			return true;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isEmailTaken(String email)
	{
		return email != null && userDao.findByNormalizedEmail(email) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUsernameTaken(String username)
	{
		return username != null && userDao.findByUsername(username) != null;
	}

	@Override
	public void sendResetPasswordEmail(ForgotPasswordForm form, Author author)
	{
		throw new UnsupportedOperationException();
	}
}
