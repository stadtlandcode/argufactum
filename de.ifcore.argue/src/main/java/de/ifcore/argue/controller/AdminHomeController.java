package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.redirect;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.services.HomeService;

@Controller
public class AdminHomeController
{
	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	@RolesAllowed("ROLE_ADMIN")
	public String getHomeAdmin(Model model)
	{
		return "admin/home";
	}

	@RequestMapping(value = "/admin/home/updateSelectedTopics", method = RequestMethod.POST)
	@RolesAllowed("ROLE_ADMIN")
	public String updateSelectedTopics()
	{
		homeService.updateSelectedTopics();
		return redirect("/admin/home");
	}

	@RequestMapping(value = "/admin/home/updateLatestTopics", method = RequestMethod.POST)
	@RolesAllowed("ROLE_ADMIN")
	public String updateLatestTopics()
	{
		homeService.updateLatestTopics();
		return redirect("/admin/home");
	}

	@RequestMapping(value = "/admin/home/updateTopicsByCategory", method = RequestMethod.POST)
	@RolesAllowed("ROLE_ADMIN")
	public String updateTopicsByCategory()
	{
		homeService.updateTopicsByCategory();
		return redirect("/admin/home");
	}
}
