package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.form.ForgotPasswordForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.services.UserService;

@Controller
public class ForgotPasswordController extends AbstractController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String getForgotPassword(@ModelAttribute ForgotPasswordForm form, Model model)
	{
		addActiveNavElement(model, NavElement.LOGIN);
		addUseMinifiedCssJs(model);
		return "user/forgotPassword";
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String postForgotPassword(@Valid ForgotPasswordForm form, Model model)
	{
		formHandler.handleForm(form, userService, userRequest.getAuthor());
		return "redirect:/user/forgotPassword";
	}
}
