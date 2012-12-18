package de.ifcore.argue.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.services.UserTopicsService;

@Controller
public class UserTopicsController extends AbstractController
{
	@Autowired
	private UserTopicsService userTopicsService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/myTopics", method = RequestMethod.GET)
	@RolesAllowed("ROLE_USER")
	public String getMyTopics(Model model)
	{
		addActiveNavElement(model, NavElement.MY_TOPICS);
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		model.addAttribute("allTopics", userTopicsService.getAll(userRequest.getAuthor().getUserId()));
		return "user/myTopics";
	}
}
