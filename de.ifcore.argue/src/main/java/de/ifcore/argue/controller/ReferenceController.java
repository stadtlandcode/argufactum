package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.DeletableTopicEntity;
import de.ifcore.argue.domain.entities.Fact;
import de.ifcore.argue.domain.entities.Reference;
import de.ifcore.argue.domain.form.CreateReferenceForm;
import de.ifcore.argue.domain.form.DeleteTopicEntityForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.DeleteReferenceModel;
import de.ifcore.argue.domain.models.view.ReferenceModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.FactService;
import de.ifcore.argue.services.ReferenceService;

@Controller
public class ReferenceController extends AbstractController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private FactService factService;

	@Autowired
	private ReferenceService referenceService;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/reference/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid CreateReferenceForm form, Errors errors)
	{
		Fact fact = form.getFactId() == null ? null : factService.get(form.getFactId());
		if (errors.hasErrors() || fact == null)
		{
			return null;
		}
		else
		{
			Reference reference = formHandler.handleForm(form, referenceService, userRequest.getAuthor());
			ReferenceModel model = new ReferenceModel(reference, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/reference/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel ajaxDelete(@Valid DeleteTopicEntityForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			DeletableTopicEntity reference = formHandler.handleForm(form, referenceService, userRequest.getAuthor());
			DeleteReferenceModel model = new DeleteReferenceModel(reference);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}
}
