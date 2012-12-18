package de.ifcore.argue.domain.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MultipartMailMessage extends MimeMailMessage
{
	private MultipartMailMessage(MimeMessageHelper mimeMessageHelper)
	{
		super(mimeMessageHelper);
	}

	public static MultipartMailMessage instanceFor(MimeMessage mimeMessage)
	{
		try
		{
			return new MultipartMailMessage(new MimeMessageHelper(mimeMessage, true, "UTF-8"));
		}
		catch (MessagingException ex)
		{
			throw new MailParseException(ex);
		}
	}

	public void setText(String plainText, String htmlText) throws MailParseException
	{
		try
		{
			getMimeMessageHelper().setText(plainText, htmlText);
		}
		catch (MessagingException ex)
		{
			throw new MailParseException(ex);
		}
	}
}
