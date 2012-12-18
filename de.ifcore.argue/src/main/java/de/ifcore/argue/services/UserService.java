package de.ifcore.argue.services;

import de.ifcore.argue.domain.entities.Author;
import de.ifcore.argue.domain.entities.EmailVerificationRequest;
import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.form.ForgotPasswordForm;
import de.ifcore.argue.domain.form.UserForeignAuthenticationRegistrationForm;
import de.ifcore.argue.domain.form.UserRegistrationForm;

public interface UserService extends Service
{
	public RegisteredUser register(UserRegistrationForm form, String ip);

	public RegisteredUser register(UserForeignAuthenticationRegistrationForm form, String ip);

	/**
	 * @param verificationKey
	 * @return false if no corresponding {@link EmailVerificationRequest} could be found
	 */
	public boolean verifyEmail(String verificationKey);

	/**
	 * @param email
	 * @return true when (normalized) email is taken, false if email is not taken or null
	 */
	public boolean isEmailTaken(String email);

	/**
	 * @param username
	 * @return true when username is taken, false if username is not taken or null
	 */
	public boolean isUsernameTaken(String username);

	public void sendResetPasswordEmail(ForgotPasswordForm form, Author author);
}
