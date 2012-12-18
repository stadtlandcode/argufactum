package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.movedPermanently;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.TopicUrl;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.FactType;
import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.form.ArgumentRelevanceVoteForm;
import de.ifcore.argue.domain.form.CreateArgumentForm;
import de.ifcore.argue.domain.form.DeleteTopicEntityForm;
import de.ifcore.argue.domain.form.EditArgumentForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.form.RestoreTopicEntityForm;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.CreateArgumentModel;
import de.ifcore.argue.domain.models.view.CreateFactModel;
import de.ifcore.argue.domain.models.view.DeleteArgumentModel;
import de.ifcore.argue.domain.models.view.DeletedEntitiesModel;
import de.ifcore.argue.domain.models.view.EditArgumentModel;
import de.ifcore.argue.domain.models.view.EditArgumentRelevanceModel;
import de.ifcore.argue.domain.models.view.HistoryModel;
import de.ifcore.argue.domain.models.view.PersonalCreateFactModel;
import de.ifcore.argue.domain.models.view.RestoreArgumentModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.domain.report.ArgumentReport;
import de.ifcore.argue.domain.report.EventReport;
import de.ifcore.argue.services.ArgumentService;
import de.ifcore.argue.services.TopicAccessRightService;

@Controller
public class ArgumentController extends AbstractController
{
	private final static String argumentUrl = "/topic/{topicId}/{topicTerm}/{topicThesis}/argument/{id}";
	private final static String argumentAjaxUrl = argumentUrl + "/ajax";

	@Autowired
	private ArgumentService argumentService;

	@Autowired
	private FormHandler formHandler;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private TopicAccessRightService accessRightService;

	@RequestMapping(value = "/argument/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid CreateArgumentForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Argument argument = formHandler.handleForm(form, argumentService, userRequest.getAuthor());
			CreateArgumentModel model = new CreateArgumentModel(argument, messageSource, getUrl(argument));
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/argument/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditArgumentForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Argument argument = formHandler.handleForm(form, argumentService, userRequest.getAuthor());
			EditArgumentModel model = new EditArgumentModel(argument, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/argument/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel ajaxDelete(@Valid DeleteTopicEntityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			DeletableTopicEntity entity = formHandler.handleForm(form, argumentService, userRequest.getAuthor());
			DeleteArgumentModel model = new DeleteArgumentModel(entity);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/argument/restore/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxRestore(@Valid RestoreTopicEntityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Argument argument = (Argument)formHandler.handleForm(form, argumentService, userRequest.getAuthor());
			RestoreArgumentModel model = new RestoreArgumentModel(argument,
					accessRightService.getUserIdsWithWriteAccess(argument.getTopicId()), messageSource,
					getUrl(argument));
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/argument/vote/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxVote(@Valid ArgumentRelevanceVoteForm form, Errors errors)
	{
		Argument argument = argumentService.get(form.getId());
		if (errors.hasErrors() || argument == null)
		{
			return null;
		}
		else
		{
			formHandler.handleForm(form, argumentService, userRequest.getAuthor());
			EditArgumentRelevanceModel model = new EditArgumentRelevanceModel(argument, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/argument/{id}/history/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getHistory(@PathVariable Long id)
	{
		Set<EventReport> events = argumentService.getEventReports(id, userRequest.getAuthor());
		return new HistoryModel(id, "argument", events, messageSource);
	}

	@RequestMapping(value = "/argument/{id}/deletedFacts/{factType}/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getDeletedFacts(@PathVariable Long id, @PathVariable FactType factType)
	{
		Set<Fact> facts = argumentService.getDeletedFacts(id, factType, userRequest.getAuthor());
		return new DeletedEntitiesModel(facts, messageSource);
	}

	@RequestMapping(value = argumentUrl, method = RequestMethod.GET)
	public ModelAndView read(@PathVariable Long topicId, @PathVariable String topicTerm,
			@PathVariable String topicThesis, @PathVariable Long id, Model model)
	{
		return doRead(topicId, topicTerm, topicThesis, id, model);
	}

	@RequestMapping(value = argumentAjaxUrl, method = RequestMethod.GET)
	public ModelAndView ajaxRead(@PathVariable Long id, Model model)
	{
		return doRead(null, null, null, id, model);
	}

	private ModelAndView doRead(Long topicId, String topicTermForUrl, String topicThesis, Long id, Model model)
	{
		Argument argument = argumentService.get(id, userRequest.getAuthor());
		if (argument == null)
			throw new ResourceNotFoundException();

		boolean ajaxRequest = topicTermForUrl == null && topicThesis == null;
		if (!ajaxRequest && !getUrl(argument).equals(getUrl(topicTermForUrl, topicThesis, topicId, id)))
		{
			return movedPermanently(getUrl(argument));
		}
		else
		{
			addHelpLocale(model);
			addActiveNavElement(model, NavElement.TOPIC);
			addRandomResourceId(model);
			addUseMinifiedCssJs(model);
			boolean editable = accessRightService.hasAccess(argument, userRequest.getAuthor().getRegisteredUser(),
					EntityPermission.WRITE);
			model.addAttribute("editable", Boolean.valueOf(editable));
			model.addAttribute("voteable", Boolean.valueOf(accessRightService.hasAccess(argument, userRequest
					.getAuthor().getRegisteredUser(), EntityPermission.VOTE)));
			model.addAttribute("subscribeCometUrl",
					Boolean.valueOf(editable && accessRightService.editedByMultipleUsers(argument.getTopicId())));
			model.addAttribute(argument);
			model.addAttribute("topic", argument.getTopic().getTopic());
			model.addAttribute("facts", getFactsAttribute(argument));
			model.addAttribute("relevances", Relevance.values());
			return new ModelAndView(ajaxRequest ? "argument/readAjax" : "argument/read");
		}
	}

	private Set<CreateFactModel> getFactsAttribute(Argument argument)
	{
		Set<CreateFactModel> facts = new LinkedHashSet<>();
		for (Fact fact : argument.getFacts())
			facts.add(new PersonalCreateFactModel(fact, userRequest.getAuthor().getUserId(), messageSource));
		return facts;
	}

	/**
	 * @param argument
	 * @return URL for an argument
	 */
	public static String getUrl(Argument argument)
	{
		return getUrl(argument.getTopic().getTopic().getTermForUrl(), argument.getTopicThesis().name(),
				argument.getTopicId(), argument.getId());
	}

	public static String getUrl(ArgumentReport argument, TopicUrl topic)
	{
		return getUrl(topic.getTermForUrl(), argument.getTopicThesis().name(), topic.getId(), argument.getId());
	}

	private static String getUrl(String topicTermForUrl, String topicThesis, Long topicId, Long id)
	{
		UriTemplate uriTemplate = new UriTemplate(argumentUrl);
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("topicTerm", topicTermForUrl);
		uriVariables.put("topicThesis", topicThesis.toLowerCase());
		uriVariables.put("topicId", topicId.toString());
		uriVariables.put("id", id.toString());
		return uriTemplate.expand(uriVariables).toString();
	}
}
