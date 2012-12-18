package de.ifcore.argue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController extends AbstractController
{
	@RequestMapping(value = "/imprint", method = RequestMethod.GET)
	public String getImprint(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		return "imprint";
	}

	@RequestMapping(value = "/privacy", method = RequestMethod.GET)
	public String getPrivacy(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		return "privacy";
	}

	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public String getTerms(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		return "terms";
	}
}
