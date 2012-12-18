package de.ifcore.argue.domain.models.view;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.MessageSource;

import de.ifcore.argue.domain.entities.RegisteredUser;
import de.ifcore.argue.domain.report.TopicAccessRightReport;

public class TopicAccessRightsModel implements ViewModel
{
	private static final long serialVersionUID = -1764240091755649836L;

	private final Long id;
	private final Set<TopicAccessRightModel> accessRights;

	public TopicAccessRightsModel(Long topicId, Set<TopicAccessRightReport> accessRightReports, RegisteredUser user,
			MessageSource messageSource)
	{
		this.id = topicId;
		Set<TopicAccessRightModel> accessRights = new LinkedHashSet<>();
		for (TopicAccessRightReport accessRightReport : accessRightReports)
			accessRights.add(new TopicAccessRightModel(accessRightReport, topicId, user, messageSource));
		this.accessRights = Collections.unmodifiableSet(accessRights);
	}

	public Long getId()
	{
		return id;
	}

	public Set<TopicAccessRightModel> getAccessRights()
	{
		return accessRights;
	}
}
