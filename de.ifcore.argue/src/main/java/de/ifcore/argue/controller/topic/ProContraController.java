package de.ifcore.argue.controller.topic;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.ifcore.argue.atmosphere.BroadcasterServiceFactory;
import de.ifcore.argue.domain.entities.Argument;
import de.ifcore.argue.domain.entities.ProContraTopic;
import de.ifcore.argue.domain.enumerations.TopicThesis;
import de.ifcore.argue.domain.form.EditProContraThesesForm;
import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.DeletedEntitiesModel;
import de.ifcore.argue.domain.models.view.EditProContraThesesModel;
import de.ifcore.argue.domain.models.view.ViewModel;
import de.ifcore.argue.services.ProContraService;

@Controller
public class ProContraController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private ProContraService proContraService;

	@Autowired
	private UserRequest userRequest;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/proContra/theses/ajax", method = RequestMethod.PUT)
	@ResponseBody
	public ViewModel ajaxEdit(@Valid EditProContraThesesForm form, Errors errors)
	{
		if (errors.hasErrors())
		{
			return null;
		}
		else
		{
			ProContraTopic proContraTopic = formHandler.handleForm(form, proContraService, userRequest.getAuthor());
			EditProContraThesesModel model = new EditProContraThesesModel(proContraTopic, messageSource);
			BroadcasterServiceFactory.getService().broadcast(model, form.getCometResourceId());
			return model;
		}
	}

	@RequestMapping(value = "/proContra/{id}/{topicThesis}/deletedArguments/ajax", method = RequestMethod.GET)
	@ResponseBody
	public ViewModel getDeletedArguments(@PathVariable Long id, @PathVariable TopicThesis topicThesis)
	{
		Set<Argument> arguments = proContraService.getDeletedArguments(id, topicThesis, userRequest.getAuthor());
		return new DeletedEntitiesModel(arguments, messageSource);
	}
}
