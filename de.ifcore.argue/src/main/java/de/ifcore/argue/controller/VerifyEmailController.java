package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.addRedirectSuccessMessage;
import static de.ifcore.argue.utils.ControllerUtils.redirectHome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.ifcore.argue.domain.models.ArgueUrl;
import de.ifcore.argue.services.UserService;

@Controller
public class VerifyEmailController extends AbstractController
{
	@Autowired
	private UserService userService;

	@RequestMapping(value = ArgueUrl.VERIFY_EMAIL, method = RequestMethod.GET)
	public String verifyEmail(@PathVariable String verificationKey, RedirectAttributes redirectAttributes, Model model)
	{
		boolean found = userService.verifyEmail(verificationKey);
		if (found)
		{
			addRedirectSuccessMessage(redirectAttributes, "verifyEmail.success");
			return redirectHome();
		}
		else
		{
			addUseMinifiedCssJs(model);
			return "user/verifyEmailNotFound";
		}
	}
}
