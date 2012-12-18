package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.addRedirectErrorMessage;
import static de.ifcore.argue.utils.ControllerUtils.addRedirectSuccessMessage;
import static de.ifcore.argue.utils.ControllerUtils.redirect;
import static de.ifcore.argue.utils.ControllerUtils.redirectHome;
import static de.ifcore.argue.utils.ControllerUtils.redirectLogin;

import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.ifcore.argue.domain.entities.IdEntity;
import de.ifcore.argue.domain.enumerations.NavElement;
import de.ifcore.argue.domain.form.AbstractUserRegistrationForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.form.RegistrationPasswordInput;
import de.ifcore.argue.domain.form.UserForeignAuthenticationRegistrationForm;
import de.ifcore.argue.domain.form.UserRegistrationForm;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.UserSession;
import de.ifcore.argue.security.ForeignAuthentication;
import de.ifcore.argue.security.OAuthForeignAuthentication;
import de.ifcore.argue.security.OpenIdAuthenticationFailureHandler;
import de.ifcore.argue.services.SecurityService;
import de.ifcore.argue.services.UserService;
import de.ifcore.argue.services.UsernameService;

@Controller
public class RegistrationController extends AbstractController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserSession userSession;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private UsernameService usernameService;

	@RequestMapping(value = "/registration/usernamePassword", method = RequestMethod.GET)
	public String getUsernamePasswordRegistration(@ModelAttribute UserRegistrationForm form, Model model)
	{
		addHelpLocale(model);
		addUseMinifiedCssJs(model);
		addActiveNavElement(model, NavElement.LOGIN);
		form.setPasswordInput(new RegistrationPasswordInput(UUID.randomUUID().toString()));
		return "registration/usernamePassword";
	}

	@RequestMapping(value = "/registration/usernamePassword", method = RequestMethod.POST)
	public String postUsernamePasswordRegistration(@Valid UserRegistrationForm form, Errors errors, Model model,
			RedirectAttributes redirectAttributes)
	{
		if (userRequest.getAuthor().getRegisteredUser() != null)
		{
			return redirectHome();
		}

		if (errors.hasErrors())
		{
			String uuid = form.getPasswordInput().getUuid();
			if (form.getPasswordInput().getPassword() != null && uuid != null)
			{
				String password = errors.hasFieldErrors("passwordInput") ? null : form.getPasswordInput().getPassword();
				userSession.storePassword(uuid, securityService.encodePassword(password));
			}
			model.addAttribute("passwordStored", userSession.getStoredPassword(uuid) != null);
			addHelpLocale(model);
			addUseMinifiedCssJs(model);
			addActiveNavElement(model, NavElement.LOGIN);
			return "registration/usernamePassword";
		}
		else
		{
			String redirect = doRegistration(form, redirectAttributes);
			userSession.storePassword(form.getPasswordInput().getUuid(), null);
			return redirect;
		}
	}

	@RequestMapping(value = "/registration/socialSignUp", method = RequestMethod.GET)
	public String getSocialSignUp(WebRequest request, RedirectAttributes redirectAttributes)
	{
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if (connection == null)
			return cancelForeignAuthentication(redirectAttributes);

		OAuthForeignAuthentication foreignAuthentication = new OAuthForeignAuthentication(connection);
		userSession.setForeignAuthentication(foreignAuthentication);
		return redirect("/registration/foreignAuthentication/" + foreignAuthentication.getUuid().toString());
	}

	@RequestMapping(value = "/registration/openIdSignUp", method = RequestMethod.GET)
	public String getOpenIdSignUp(HttpSession session, RedirectAttributes redirectAttributes)
	{
		ForeignAuthentication auth = (ForeignAuthentication)session
				.getAttribute(OpenIdAuthenticationFailureHandler.OPEN_ID_FOREIGN_AUTHENTICATION_ATTRIBUTE);
		if (auth == null)
			return cancelForeignAuthentication(redirectAttributes);

		userSession.setForeignAuthentication(auth);
		session.removeAttribute(OpenIdAuthenticationFailureHandler.OPEN_ID_FOREIGN_AUTHENTICATION_ATTRIBUTE);
		return redirect("/registration/foreignAuthentication/" + auth.getUuid().toString());
	}

	@RequestMapping(value = "/registration/foreignAuthentication/{uuid}", method = RequestMethod.GET)
	public String getForeignAuthenticationRegistration(@PathVariable String uuid, Model model,
			RedirectAttributes redirectAttributes)
	{
		ForeignAuthentication foreignAuthentication = userSession.getForeignAuthentication(uuid);
		if (foreignAuthentication == null)
			return cancelForeignAuthentication(redirectAttributes);

		if (userService.isEmailTaken(foreignAuthentication.getEmail()))
		{
			addRedirectErrorMessage(redirectAttributes, "registration.foreignAuthentication.emailTaken");
			return redirectLogin();
		}
		else
		{
			model.addAttribute(CommonControllerAttributes.successMessage, "registration.foreignAuthentication.note");
			model.addAttribute(new UserForeignAuthenticationRegistrationForm(foreignAuthentication, usernameService));
			addHelpLocale(model);
			addUseMinifiedCssJs(model);
			addActiveNavElement(model, NavElement.LOGIN);
			return "registration/foreignAuthentication";
		}
	}

	@RequestMapping(value = "/registration/foreignAuthentication/{uuid}", method = RequestMethod.POST)
	public String postForeignAuthenticationRegistration(@PathVariable String uuid,
			@ModelAttribute @Valid UserForeignAuthenticationRegistrationForm form, Errors errors,
			RedirectAttributes redirectAttributes, Model model)
	{
		if (userRequest.getAuthor().getRegisteredUser() != null)
		{
			return redirectHome();
		}
		if (userSession.getForeignAuthentication(uuid) == null || !uuid.equals(form.getAuthenticationUuid()))
		{
			return cancelForeignAuthentication(redirectAttributes);
		}

		if (!errors.hasErrors())
		{
			return doRegistration(form, redirectAttributes);
		}
		else
		{
			addHelpLocale(model);
			addUseMinifiedCssJs(model);
			return "registration/foreignAuthentication";
		}
	}

	private String doRegistration(AbstractUserRegistrationForm form, RedirectAttributes redirectAttributes)
	{
		IdEntity user = formHandler.handleForm(form, userService, userRequest.getAuthor());
		securityService.autoLogin(user.getId());
		addRedirectSuccessMessage(redirectAttributes, "registration.success");
		return redirectHome();
	}

	private String cancelForeignAuthentication(RedirectAttributes redirectAttributes)
	{
		addRedirectErrorMessage(redirectAttributes, "registration.foreignAuthentication.error");
		return redirectLogin();
	}

	@RequestMapping(value = "/registration/deleteForeignAuthentication", method = RequestMethod.GET)
	public String deleteForeignAuthentication()
	{
		userSession.setForeignAuthentication(null);
		return redirectLogin();
	}
}
