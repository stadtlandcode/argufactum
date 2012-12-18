package de.ifcore.argue.controller.topic;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import de.ifcore.argue.controller.ArgumentController;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.models.UserSession;
import de.ifcore.argue.domain.models.view.CreateArgumentModel;
import de.ifcore.argue.domain.models.view.PersonalCreateArgumentModel;
import de.ifcore.argue.domain.report.ArgumentReport;

@Component
public class ProContraViewHelper implements TopicViewHelper
{
	private final UserSession userSession;
	private final MessageSource messageSource;

	@Inject
	public ProContraViewHelper(UserSession userSession, MessageSource messageSource)
	{
		this.userSession = userSession;
		this.messageSource = messageSource;
	}

	@Override
	@Transactional(readOnly = true)
	public void addAttributes(Topic topic, Model model)
	{
		model.addAttribute(topic.getProContraTopic());
		model.addAttribute("arguments", getArgumentsAttribute(topic));
		model.addAttribute("relevances", Relevance.values());
	}

	private Set<CreateArgumentModel> getArgumentsAttribute(Topic topic)
	{
		Set<CreateArgumentModel> arguments = new LinkedHashSet<>();
		for (ArgumentReport argumentReport : topic.getProContraTopic().getArguments())
		{
			Long userId = userSession.getUser() == null ? null : userSession.getUser().getId();
			arguments.add(new PersonalCreateArgumentModel(argumentReport, userId, messageSource, ArgumentController
					.getUrl(argumentReport, topic)));
		}
		return arguments;
	}

	@Override
	public String getViewName()
	{
		return "topic/readProContra";
	}

	@Override
	public String getAjaxViewName()
	{
		return "topic/readProContraAjax";
	}

	@Override
	public boolean isResponsible(Topic topic)
	{
		return DiscussionType.PRO_CONTRA.equals(topic.getDiscussionType());
	}
}
