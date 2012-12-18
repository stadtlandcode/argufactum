package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.form.CreateCriterionForm;
import de.ifcore.argue.domain.form.DeleteCriterionForm;
import de.ifcore.argue.domain.form.EditCriterionForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.CreateCriterionModel;
import de.ifcore.argue.domain.models.view.DeleteCriterionModel;
import de.ifcore.argue.domain.models.view.EditCriterionModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.ComparisonValueDefinitionService;
import de.ifcore.argue.services.CriterionService;

@Controller
public class CriterionController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private CriterionService criterionService;

	@Autowired
	private ComparisonValueDefinitionService definitionService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/criterion/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid CreateCriterionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Criterion criterion = formHandler.handleForm(form, criterionService, userRequest.getAuthor());
			CreateCriterionModel model = new CreateCriterionModel(criterion, userRequest.getAuthor()
					.getRegisteredUser(), form.getProcessId(), definitionService.getAdditionalSets(criterion));
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/criterion/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditCriterionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Criterion criterion = formHandler.handleForm(form, criterionService, userRequest.getAuthor());
			EditCriterionModel model = new EditCriterionModel(criterion, definitionService.getAdditionalSets(criterion));
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/criterion/ajax", method = RequestMethod.DELETE)
	@ResponseBody
	public ViewModel ajaxDelete(@Valid DeleteCriterionForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			Criterion criterion = formHandler.handleForm(form, criterionService, userRequest.getAuthor());
			DeleteCriterionModel model = new DeleteCriterionModel(criterion);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}
}
