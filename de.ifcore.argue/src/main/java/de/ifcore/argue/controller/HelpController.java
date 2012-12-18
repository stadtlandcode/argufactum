package de.ifcore.argue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelpController extends AbstractController
{
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String getImprint(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		return "help";
	}
}
