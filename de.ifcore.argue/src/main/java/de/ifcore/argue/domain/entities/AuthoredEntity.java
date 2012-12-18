package de.ifcore.argue.domain.entities;

import java.io.Serializable;

import org.joda.time.DateTime;

import de.ifcore.argue.domain.report.LogReport;

public interface AuthoredEntity extends Serializable, LogReport
{
	public Author getAuthor();

	public DateTime getCreatedAt();
}
