package de.ifcore.argue.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.services.ApplicationService;
import de.ifcore.argue.services.HelpTextService;

public abstract class AbstractController
{
	private final String randomResourceId = RandomStringUtils.randomNumeric(14);

	@Autowired
	private HelpTextService helpTextService;

	@Autowired
	private ApplicationService applicationService;

	public void addUseMinifiedCssJs(Model model)
	{
		model.addAttribute("useMinifiedCssJs", applicationService.useMinifiedCssJs());
	}

	public void addRandomResourceId(Model model)
	{
		model.addAttribute("randomResourceId", randomResourceId);
	}

	public void addHelpLocale(Model model)
	{
		model.addAttribute("helpLocale", helpTextService.getHelpLocale());
	}

	public void addActiveNavElement(Model model, NavElement navElement)
	{
		model.addAttribute(CommonControllerAttributes.activeNavElement, navElement);
	}
}
