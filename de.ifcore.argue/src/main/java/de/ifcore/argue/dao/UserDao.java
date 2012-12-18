package de.ifcore.argue.dao;

import java.util.Set;

import de.ifcore.argue.domain.entities.EmailVerificationRequest;
import de.ifcore.argue.domain.entities.RegisteredUser;

public interface UserDao extends EntityDao<RegisteredUser, Long>
{
	public RegisteredUser findByUsername(String name);

	public RegisteredUser findByEmail(String email);

	public RegisteredUser findByNormalizedEmail(String email);

	public RegisteredUser findByOpenIdIdentifier(String identifier);

	public void saveEmailVerificationRequest(EmailVerificationRequest request);

	public void updateEmailVerificationRequest(EmailVerificationRequest verificationRequest);

	public EmailVerificationRequest getEmailVerificationRequest(String verificationKey);

	public Set<String> findUsernamesStartingWith(String username);
}
