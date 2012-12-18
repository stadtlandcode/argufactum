package de.ifcore.argue.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.form.FormHandler;
import de.ifcore.argue.domain.form.SearchForm;
import de.ifcore.argue.domain.models.UserRequest;
import de.ifcore.argue.domain.models.view.SearchResultModel;
import de.ifcore.argue.services.SearchService;

@Controller
public class SearchController extends AbstractController
{
	@Autowired
	private FormHandler formHandler;

	@Autowired
	private SearchService searchService;

	@Autowired
	private UserRequest userRequest;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@Valid SearchForm form, Errors errors, Model model)
	{
		addUseMinifiedCssJs(model);
		if (!errors.hasErrors())
		{
			SearchResultModel searchResult = formHandler.handleForm(form, searchService, userRequest.getAuthor());
			model.addAttribute("searchResult", searchResult);
		}
		return "search";
	}
}
