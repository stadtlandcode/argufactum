package de.ifcore.argue.controller.topic;

import static de.ifcore.argue.utils.ControllerUtils.addRedirectSuccessMessage;
import static de.ifcore.argue.utils.ControllerUtils.movedPermanently;
import static de.ifcore.argue.utils.ControllerUtils.redirect;
import static de.ifcore.argue.utils.ControllerUtils.redirectHome;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriTemplate;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.controller.AbstractController;
import de.ifcore.argue.controller.ResourceNotFoundException;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.entities.TopicUrl;
import de.ifcore.argue.domain.entities.TopicVote;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.form.AddTopicAccessRightForm;
import de.ifcore.argue.domain.form.AddTopicVoteForm;
import de.ifcore.argue.domain.form.CopyTopicForm;
import de.ifcore.argue.domain.form.CreateTopicForm;
import de.ifcore.argue.domain.form.EditTopicForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.form.RemoveTopicAccessRightForm;
import de.ifcore.argue.domain.form.RemoveTopicVoteForm;
import de.ifcore.argue.domain.form.TopicVisibilityForm;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.CreateTopicModel;
import de.ifcore.argue.domain.models.view.EditTopicModel;
import de.ifcore.argue.domain.models.view.EditTopicVisibilityModel;
import de.ifcore.argue.domain.models.view.ErrorsModel;
import de.ifcore.argue.domain.models.view.RemoveAccessRightModel;
import de.ifcore.argue.domain.models.view.TopicAccessRightModel;
import de.ifcore.argue.domain.models.view.TopicAccessRightsModel;
import de.ifcore.argue.domain.models.view.TopicHistoryModel;
import de.ifcore.argue.domain.models.view.TopicVoteModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.domain.report.TopicEventReport;
import de.ifcore.argue.services.CategoryService;
import de.ifcore.argue.services.TopicAccessRightService;
import de.ifcore.argue.services.TopicService;
import de.ifcore.argue.services.TopicViewCountService;

@Controller
public class TopicController extends AbstractController
{
	private final static String topicUrl = "/topic/{id}/{term}";
	private final static String topicAjaxUrl = "/topic/{id}/{term}/ajax";

	@Autowired
	private TopicService topicService;

	@Autowired
	private FormHandler formHandler;

	@Autowired
	private TopicViewHelperFactory viewHelperFactory;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TopicAccessRightService accessRightService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TopicViewCountService viewCountService;

	@RequestMapping(value = "/topic", method = RequestMethod.GET)
	public String create(@ModelAttribute CreateTopicForm form, Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		addActiveNavElement(model, NavElement.NEW_TOPIC);
		model.addAttribute("categories", categoryService.getAll());
		return "topic/create";
	}

	@RequestMapping(value = "/topic", method = RequestMethod.POST)
	public String postCreate(@Valid CreateTopicForm form, Errors errors, Model model, Locale locale)
	{
		String view;
		if (errors.hasErrors())
		{
			view = create(form, model);
		}
		else
		{
			form.setValidTerm(messageSource, locale);
			Topic topic = formHandler.handleForm(form, topicService, userRequest.getAuthor());
			view = redirect(getUrl(topic));
		}
		return view;
	}

	@RequestMapping(value = "/topic/{id}/history/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getHistory(@PathVariable Long id)
	{
		Set<TopicEventReport> events = topicService.getEventReports(id, userRequest.getAuthor());
		return new TopicHistoryModel(id, events, messageSource);
	}

	@RequestMapping(value = "/topic/{id}/accessRights/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getAccessRights(@PathVariable Long id)
	{
		accessRightService.checkAccess(id, userRequest.getAuthor().getRegisteredUser(), EntityPermission.WRITE);
		return new TopicAccessRightsModel(id, accessRightService.getReport(id), userRequest.getAuthor()
				.getRegisteredUser(), messageSource);
	}

	@RequestMapping(value = "/topic/share/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel addAccessRight(@Valid AddTopicAccessRightForm form, Errors errors, HttpServletResponse response)
	{
		if (errors.hasErrors())
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new ErrorsModel(errors, messageSource);
		}
		else
		{
			TopicAccessRight accessRight = formHandler.handleForm(form, accessRightService, userRequest.getAuthor());
			TopicAccessRightModel model = new TopicAccessRightModel(accessRight, form.getId(), null, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/topic/share/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel removeAccessRight(@Valid RemoveTopicAccessRightForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			TopicAccessRight accessRight = formHandler.handleForm(form, accessRightService, userRequest.getAuthor());
			RemoveAccessRightModel model = new RemoveAccessRightModel(accessRight);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/topic/setVisibility/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel setVisibility(@Valid TopicVisibilityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Topic topic = formHandler.handleForm(form, accessRightService, userRequest.getAuthor());
			EditTopicVisibilityModel model = new EditTopicVisibilityModel(topic);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/topic/vote/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel addVote(@Valid AddTopicVoteForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			TopicVote vote = formHandler.handleForm(form, topicService, userRequest.getAuthor());
			return vote == null ? new TopicVoteModel(null) : new TopicVoteModel(vote.getVote());
		}
	}

	@RequestMapping(value = "/topic/vote/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel removeVote(@Valid RemoveTopicVoteForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			formHandler.handleForm(form, topicService, userRequest.getAuthor());
			return new TopicVoteModel(null);
		}
	}

	@RequestMapping(value = "/topic/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditTopicForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			Topic topic = formHandler.handleForm(form, topicService, userRequest.getAuthor());
			EditTopicModel model = new EditTopicModel(topic, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/topic/copy", method = RequestMethod.POST)
	public String copy(@Valid CopyTopicForm form, Errors errors, RedirectAttributes redirectAttributes)
	{
		String view;
		if (errors.hasErrors())
		{
			view = redirectHome();
		}
		else
		{
			Topic topic = formHandler.handleForm(form, topicService, userRequest.getAuthor());
			addRedirectSuccessMessage(redirectAttributes, "topic.copy.success");
			view = redirect(getUrl(topic));
		}
		return view;
	}

	@RequestMapping(value = topicUrl, method = RequestMethod.GET)
	public ModelAndView read(@PathVariable Long id, @PathVariable String term, Model model)
	{
		return doRead(id, term, model);
	}

	@RequestMapping(value = topicAjaxUrl, method = RequestMethod.GET)
	public ModelAndView readAjax(@PathVariable Long id, Model model)
	{
		return doRead(id, null, model);
	}

	private ModelAndView doRead(Long id, String term, Model model)
	{
		Topic topic = topicService.get(id, userRequest.getAuthor());
		if (topic == null)
			throw new ResourceNotFoundException();

		boolean ajaxRequest = term == null;
		if (!ajaxRequest && !topic.getTermForUrl().equals(term))
		{
			return movedPermanently(getUrl(topic));
		}
		else
		{
			addHelpLocale(model);
			addActiveNavElement(model, NavElement.TOPIC);
			addRandomResourceId(model);
			addUseMinifiedCssJs(model);
			boolean editable = accessRightService.hasAccess(topic, userRequest.getAuthor().getRegisteredUser(),
					EntityPermission.WRITE);
			model.addAttribute("editable", Boolean.valueOf(editable));
			model.addAttribute("voteable", Boolean.valueOf(accessRightService.hasAccess(topic, userRequest.getAuthor()
					.getRegisteredUser(), EntityPermission.VOTE)));
			model.addAttribute("subscribeCometUrl",
					Boolean.valueOf(editable && accessRightService.editedByMultipleUsers(id)));
			model.addAttribute("topic", new CreateTopicModel(topic, userRequest.getAuthor().getUserId(), messageSource,
					topicService));

			TopicViewHelper viewHelper = viewHelperFactory.getHelper(topic);
			viewHelper.addAttributes(topic, model);
			viewCountService.count(topic, userRequest.getAuthor());
			return new ModelAndView(ajaxRequest ? viewHelper.getAjaxViewName() : viewHelper.getViewName());
		}
	}

	/**
	 * @param topic
	 * @return URL for a topic
	 */
	public static String getUrl(TopicUrl topic)
	{
		return getUrl(topic.getTermForUrl(), topic.getId());
	}

	static String getUrl(String termForUrl, Long id)
	{
		UriTemplate uriTemplate = new UriTemplate(topicUrl);
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("term", termForUrl);
		uriVariables.put("id", id.toString());
		return uriTemplate.expand(uriVariables).toString();
	}

	public static String getBroadcasterId(Long topicId)
	{
		return "topic" + topicId.toString();
	}
}
