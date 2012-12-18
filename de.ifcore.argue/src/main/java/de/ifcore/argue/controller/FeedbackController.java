package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.addRedirectErrorMessage;
import static de.ifcore.argue.utils.ControllerUtils.addRedirectSuccessMessage;
import static de.ifcore.argue.utils.ControllerUtils.redirect;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.form.FeedbackForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.services.FeedbackService;

@Controller
public class FeedbackController extends AbstractController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String getFeedback(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		addActiveNavElement(model, NavElement.FEEDBACK);
		return "feedback";
	}

	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public String postFeedback(@Valid FeedbackForm form, Errors errors, RedirectAttributes redirectAttributes)
	{
		if (errors.hasErrors())
		{
			addRedirectErrorMessage(redirectAttributes, "feedback.form.error");
			return redirect("/feedback");
		}
		else
		{
			formHandler.handleForm(form, feedbackService, userRequest.getAuthor());
			addRedirectSuccessMessage(redirectAttributes, "feedback.form.success");
			return redirect("/thankyou");
		}
	}

	@RequestMapping(value = "/thankyou", method = RequestMethod.GET)
	public String getThankYou(Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		addActiveNavElement(model, NavElement.FEEDBACK);
		return "thankyou";
	}
}
