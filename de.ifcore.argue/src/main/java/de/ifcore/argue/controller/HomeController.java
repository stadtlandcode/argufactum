package de.ifcore.argue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.services.HomeService;

@Controller
public class HomeController extends AbstractController
{
	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHome(Model model)
	{
		addActiveNavElement(model, NavElement.HOME);
		addUseMinifiedCssJs(model);
		addHelpLocale(model);
		model.addAttribute("latestTopics", homeService.getLatestTopics());
		model.addAttribute("selectedTopics", homeService.getSelectedTopics());
		model.addAttribute("topicsByCategory", homeService.getTopicsByCategory());
		return "home";
	}
}