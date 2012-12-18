package de.ifcore.argue.domain.email;

import java.util.Collections;
import java.util.Map;

import de.ifcore.argue.domain.enumerations.EmailAddressTemplate;

public final class VelocityEmail
{
	public final EmailAddressTemplate from;
	public final String to;
	public final String subject;
	public final String velocityFile;
	public final String htmlVelocityFile;
	public final Map<String, Object> model;

	/**
	 * @param name
	 *            name of email. subject of email will be email.{name}.subject, velocityFile {name}.vm and
	 *            htmlVelocityFile {name}.html.vm
	 * @param from
	 * @param to
	 * @param model
	 */
	public VelocityEmail(String name, EmailAddressTemplate from, String to, Map<String, Object> model)
	{
		this.from = from;
		this.to = to;
		this.subject = "email." + name + ".subject";
		this.velocityFile = name + ".vm";
		this.htmlVelocityFile = name + ".html.vm";
		this.model = Collections.unmodifiableMap(model);
	}

	public EmailAddressTemplate getFrom()
	{
		return from;
	}

	public String getTo()
	{
		return to;
	}

	public String getVelocityFile()
	{
		return velocityFile;
	}

	public Map<String, Object> getModel()
	{
		return model;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getHtmlVelocityFile()
	{
		return htmlVelocityFile;
	}
}
