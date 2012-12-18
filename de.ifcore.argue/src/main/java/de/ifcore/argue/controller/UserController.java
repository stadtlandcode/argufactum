package de.ifcore.argue.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.domain.models.view.AutocompletionModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.UsernameService;

@Controller
public class UserController extends AbstractController
{
	@Autowired
	private UsernameService usernameService;

	@RequestMapping(value = "/usernames/autocomplete/{fragment}", method = RequestMethod.GET)
	@ResponseBody
	@RolesAllowed("ROLE_USER")
	public ViewModel autoComplete(@PathVariable String fragment)
	{
		// TODO do autoCompletion context based
		return new AutocompletionModel(usernameService.getUsernamesStartingWith(""));
	}
}
