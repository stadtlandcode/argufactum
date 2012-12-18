package de.ifcore.argue.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.form.CreateFactForm;
import de.ifcore.argue.domain.form.DeleteTopicEntityForm;
import de.ifcore.argue.domain.form.EditFactForm;
import de.ifcore.argue.domain.form.FactRelevanceVoteForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.form.RestoreTopicEntityForm;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.CreateFactModel;
import de.ifcore.argue.domain.models.view.DeleteFactModel;
import de.ifcore.argue.domain.models.view.EditFactModel;
import de.ifcore.argue.domain.models.view.EditFactRelevanceModel;
import de.ifcore.argue.domain.models.view.HistoryModel;
import de.ifcore.argue.domain.models.view.RestoreFactModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.domain.report.EventReport;
import de.ifcore.argue.services.ArgumentService;
import de.ifcore.argue.services.FactService;
import de.ifcore.argue.services.TopicAccessRightService;

@Controller
public class FactController extends AbstractController
{
	@Autowired
	private ArgumentService argumentService;

	@Autowired
	private FactService factService;

	@Autowired
	private FormHandler formHandler;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TopicAccessRightService accessRightService;

	@RequestMapping(value = "/fact/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid CreateFactForm form, Errors errors)
	{
		Argument argument = form.getArgumentId() == null ? null : argumentService.get(form.getArgumentId());
		if (errors.hasErrors() || argument == null)
		{
			return null;
		}
		else
		{
			Fact fact = formHandler.handleForm(form, factService, userRequest.getAuthor());
			CreateFactModel model = new CreateFactModel(fact, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/fact/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditFactForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Fact fact = formHandler.handleForm(form, factService, userRequest.getAuthor());
			EditFactModel model = new EditFactModel(fact, fact.getArgument().getTopicId(), messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/fact/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel ajaxDelete(@Valid DeleteTopicEntityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			DeletableTopicEntity fact = formHandler.handleForm(form, factService, userRequest.getAuthor());
			DeleteFactModel model = new DeleteFactModel(fact);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/fact/restore/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxRestore(@Valid RestoreTopicEntityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Fact fact = (Fact)formHandler.handleForm(form, factService, userRequest.getAuthor());
			RestoreFactModel model = new RestoreFactModel(fact, accessRightService.getUserIdsWithWriteAccess(fact
					.getTopicId()), messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/fact/{id}/history/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getHistory(@PathVariable Long id)
	{
		Set<EventReport> events = factService.getEventReports(id, userRequest.getAuthor());
		return new HistoryModel(id, "fact", events, messageSource);
	}

	@RequestMapping(value = "/fact/vote/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxVote(@Valid FactRelevanceVoteForm form, Errors errors)
	{
		Fact fact = factService.get(form.getId());
		if (errors.hasErrors() || fact == null)
		{
			return null;
		}
		else
		{
			formHandler.handleForm(form, factService, userRequest.getAuthor());
			EditFactRelevanceModel model = new EditFactRelevanceModel(fact, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}
}
