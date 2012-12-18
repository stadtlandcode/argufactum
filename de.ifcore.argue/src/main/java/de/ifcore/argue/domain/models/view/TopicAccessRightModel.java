package de.ifcore.argue.domain.models.view;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.report.TopicAccessRightReport;
import de.ifcore.argue.utils.EntityUtils;

public class TopicAccessRightModel extends AbstractTopicEntityModel implements ViewModel
{
	private static final long serialVersionUID = -5833155165551330111L;

	private final String contactName;
	private final String accessRight;
	private final boolean deletable;
	private final boolean writeAccess;

	public TopicAccessRightModel(TopicAccessRightReport accessRightReport, Long topicId, RegisteredUser user,
			MessageSource messageSource)
	{
		super(accessRightReport, topicId);
		Locale locale = LocaleContextHolder.getLocale();
		if (accessRightReport.isOwner())
		{
			this.contactName = messageSource.getMessage("topic.share.accessRight.owner",
					new String[] { accessRightReport.getContactName() }, locale);
		}
		else
		{
			this.contactName = accessRightReport.getContactName();
		}

		this.accessRight = messageSource.getMessage("topic.share.permission."
				+ accessRightReport.getPermission().name(), null, locale);
		this.deletable = accessRightReport.isDeletable() && !EntityUtils.equalsId(accessRightReport.getUser(), user);
		this.writeAccess = EntityPermission.WRITE.equals(accessRightReport.getPermission());
	}

	public String getContactName()
	{
		return contactName;
	}

	public String getAccessRight()
	{
		return accessRight;
	}

	public boolean isDeletable()
	{
		return deletable;
	}

	public boolean isWriteAccess()
	{
		return writeAccess;
	}

	@Override
	public String getBroadcastSubject()
	{
		return "topic.addAccessRight";
	}
}
