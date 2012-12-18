package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.Option;
import de.ifcore.argue.domain.form.CreateOptionForm;
import de.ifcore.argue.domain.form.DeleteOptionForm;
import de.ifcore.argue.domain.form.EditOptionForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.CreateOptionModel;
import de.ifcore.argue.domain.models.view.DeleteOptionModel;
import de.ifcore.argue.domain.models.view.EditOptionModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.OptionService;

@Controller
public class OptionController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private OptionService optionService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/option/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid CreateOptionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Option option = formHandler.handleForm(form, optionService, userRequest.getAuthor());
			CreateOptionModel model = new CreateOptionModel(option, form.getProcessId());
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/option/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditOptionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Option option = formHandler.handleForm(form, optionService, userRequest.getAuthor());
			EditOptionModel model = new EditOptionModel(option);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/option/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel ajaxDelete(@Valid DeleteOptionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Option option = formHandler.handleForm(form, optionService, userRequest.getAuthor());
			DeleteOptionModel model = new DeleteOptionModel(option);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}
}
