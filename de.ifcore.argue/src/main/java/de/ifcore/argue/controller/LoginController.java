package de.ifcore.argue.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.form.RegistrationPasswordInput;
import de.ifcore.argue.domain.form.UserRegistrationForm;

@Controller
public class LoginController extends AbstractController
{
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		addActiveNavElement(model, NavElement.LOGIN);
		UserRegistrationForm registrationForm = new UserRegistrationForm();
		registrationForm.setPasswordInput(new RegistrationPasswordInput(UUID.randomUUID().toString()));
		model.addAttribute(registrationForm);
		return "login";
	}
}
