package de.ifcore.argue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController extends AbstractController
{
	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public String getAccessDenied(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		return "accessDenied";
	}
}
