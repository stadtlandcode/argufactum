package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.report.LogReport;
import de.ifcore.argue.utils.FormatUtils;

public class LogModel implements ViewModel
{
	private static final long serialVersionUID = -1999092191867929650L;
	private final String dateTimeGmt;
	private final String dateTimeLocalized;
	private final Long timestamp;
	private final String authorName;
	private final Long userId;

	/**
	 * @param log
	 *            nullable
	 * @param messageSource
	 * @return LogModel or null if log is null
	 */
	public static LogModel instanceOf(LogReport log, MessageSource messageSource)
	{
		if (log == null)
			return null;
		else
			return new LogModel(log, messageSource);
	}

	protected LogModel(LogReport log, MessageSource messageSource)
	{
		if (log == null)
			throw new NullPointerException();
		this.dateTimeGmt = FormatUtils.formatDateTime(log.getDateTime(), null);
		this.dateTimeLocalized = FormatUtils.formatDateTime(log.getDateTime(),
				messageSource.getMessage("datetime.default", null, LocaleContextHolder.getLocale()));
		this.timestamp = Long.valueOf(log.getDateTime().getMillis());
		this.authorName = log.getAuthorName();
		this.userId = log.getUserId();
	}

	public String getDateTimeGmt()
	{
		return dateTimeGmt;
	}

	public String getDateTimeLocalized()
	{
		return dateTimeLocalized;
	}

	public String getAuthorName()
	{
		return authorName;
	}

	public Long getUserId()
	{
		return userId;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LogModel [dateTimeGmt=").append(dateTimeGmt).append(", dateTimeLocalized=")
				.append(dateTimeLocalized).append(", authorName=").append(authorName).append(", userId=")
				.append(userId).append("]");
		return builder.toString();
	}
}
