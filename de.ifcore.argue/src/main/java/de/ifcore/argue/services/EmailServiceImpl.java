package de.ifcore.argue.services;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.velocity.VelocityEngineUtils;

import de.ifcore.argue.domain.email.MultipartMailMessage;
import de.ifcore.argue.domain.email.VelocityEmail;

public class EmailServiceImpl implements EmailService
{
	private static final Logger log = Logger.getLogger(EmailServiceImpl.class.getName());

	private final JavaMailSender mailSender;
	private final VelocityEngine velocityEngine;
	private final EmailAddressService addressService;
	private final MessageSource messageSource;
	private Set<String> supportedLanguages;

	@Inject
	public EmailServiceImpl(JavaMailSender mailSender, VelocityEngine velocityEngine,
			EmailAddressService addressService, MessageSource messageSource)
	{
		this.mailSender = mailSender;
		this.velocityEngine = velocityEngine;
		this.addressService = addressService;
		this.messageSource = messageSource;
	}

	@Override
	public void send(VelocityEmail email)
	{
		MultipartMailMessage message = MultipartMailMessage.instanceFor(mailSender.createMimeMessage());
		message.setFrom(addressService.getAddress(email.getFrom()));
		message.setTo(email.getTo());
		String subject = messageSource.getMessage(email.getSubject(), null, LocaleContextHolder.getLocale());
		message.setSubject(subject);
		String plainText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				getLocalizedVelocityFile(email.getVelocityFile()), "UTF-8", email.getModel());
		String htmlText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				getLocalizedVelocityFile(email.getHtmlVelocityFile()), "UTF-8", email.getModel());
		message.setText(plainText, htmlText);

		log.info("sending email");
		mailSender.send(message.getMimeMessage());
	}

	private String getLocalizedVelocityFile(String velocityFile)
	{
		String language = LocaleContextHolder.getLocale().getLanguage();
		if (!supportedLanguages.contains(language))
			language = supportedLanguages.iterator().next();
		return language + "/email/" + velocityFile;
	}

	public void setSupportedLanguages(Set<String> supportedLanguages)
	{
		this.supportedLanguages = Collections.unmodifiableSet(supportedLanguages);
	}
}
