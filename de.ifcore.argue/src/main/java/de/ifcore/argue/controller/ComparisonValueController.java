package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.ComparisonValue;
import de.ifcore.argue.domain.entities.Criterion;
import de.ifcore.argue.domain.form.ComparisonValueForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.ComparisonValueModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.ComparisonValueService;
import de.ifcore.argue.services.CriterionService;

@Controller
public class ComparisonValueController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private ComparisonValueService comparisonValueService;

	@Autowired
	private CriterionService criterionService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/comparisonValue/ajax", method = RequestMethod.POST)
	@ResponseBody
	public ViewModel ajaxCreate(@Valid ComparisonValueForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			// TODO
			return null;
		}
		else
		{
			ComparisonValue comparisonValue = formHandler.handleForm(form, comparisonValueService,
					userRequest.getAuthor());
			Criterion criterion = criterionService.get(comparisonValue.getCriterionId());
			ComparisonValueModel model = new ComparisonValueModel(comparisonValue, criterion.getTopicId());
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}
}
