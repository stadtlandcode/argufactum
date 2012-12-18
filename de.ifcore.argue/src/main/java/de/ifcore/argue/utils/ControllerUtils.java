package de.ifcore.argue.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import de.ifcore.argue.controller.CommonControllerAttributes;

public class ControllerUtils
{
	public static ModelAndView movedPermanently(String targetUrl)
	{
		RedirectView redirectView = new RedirectView(targetUrl);
		redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		return new ModelAndView(redirectView);
	}

	public static String redirect(String targetUrl)
	{
		return "redirect:" + targetUrl;
	}

	public static String redirectHome()
	{
		return redirect("/");
	}

	public static String redirectLogin()
	{
		return redirect("/login");
	}

	public static void addRedirectSuccessMessage(RedirectAttributes redirectAttributes, String messageCode)
	{
		redirectAttributes.addFlashAttribute(CommonControllerAttributes.successMessage, messageCode);
	}

	public static void addRedirectErrorMessage(RedirectAttributes redirectAttributes, String messageCode)
	{
		redirectAttributes.addFlashAttribute(CommonControllerAttributes.errorMessage, messageCode);
	}
}
