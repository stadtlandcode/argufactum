package de.ifcore.argue.controller;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.ifcore.argue.domain.entities.TopicAccessRight;
import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.domain.models.view.ComparisonValueDefinitionSetModel;
import de.ifcore.argue.domain.models.view.CreateArgumentModel;
import de.ifcore.argue.domain.models.view.CreateFactModel;
import de.ifcore.argue.domain.models.view.CreateTopicModel;
import de.ifcore.argue.domain.models.view.EditOptionModel;
import de.ifcore.argue.domain.models.view.ReferenceModel;
import de.ifcore.argue.services.CategoryService;
import de.ifcore.argue.services.ComparisonValueDefinitionService;
import de.ifcore.argue.services.JavaScriptTemplateService;

@Controller
public class JavaScriptTemplateController extends AbstractController
{
	public static final String JS_TEMPLATE_ATTRIBUTE = "jsTemplate";
	private static final Logger log = Logger.getLogger(JavaScriptTemplateController.class.getName());

	private final Relevance[] relevancesReversed;
	private Set<ComparisonValueDefinitionSetModel> globalDefinitionSets;

	public JavaScriptTemplateController()
	{
		Relevance[] relevances = Relevance.values();
		ArrayUtils.reverse(relevances);
		this.relevancesReversed = relevances;
	}

	@Autowired
	private JavaScriptTemplateService javaScriptTemplateService;

	@Autowired
	private ObjectMapper ajaxObjectMapper;

	@Autowired
	private ComparisonValueDefinitionService definitionService;

	@Autowired
	private CategoryService categoryService;

	@ModelAttribute(JS_TEMPLATE_ATTRIBUTE)
	public Boolean getJsTemplate()
	{
		return Boolean.TRUE;
	}

	@RequestMapping(value = "/jsTemplates/proContra/{randomResourceId}", method = RequestMethod.GET)
	public String getProContraTemplates(Model model)
	{
		model.addAttribute("topic", javaScriptTemplateService.getTemplateMap(CreateTopicModel.class));
		model.addAttribute("permissions", TopicAccessRight.getAllowedPermissions());
		model.addAttribute("argument", javaScriptTemplateService.getTemplateMap(CreateArgumentModel.class));
		model.addAttribute("fact", javaScriptTemplateService.getTemplateMap(CreateFactModel.class));
		model.addAttribute("reference", javaScriptTemplateService.getTemplateMap(ReferenceModel.class));
		model.addAttribute("number", "{{= number}}");
		model.addAttribute("categories", categoryService.getAll());
		model.addAttribute("topicThesesMessageCodeAppendices", TopicThesesMessageCodeAppendix.values());
		model.addAttribute("relevancesReversed", relevancesReversed);
		model.addAttribute("relevances", Relevance.values());
		addHelpLocale(model);
		return "jsTemplates/proContra";
	}

	@RequestMapping(value = "/jsTemplates/comparison/{randomResourceId}", method = RequestMethod.GET)
	public String getComparisonTemplates(Model model)
	{
		model.addAttribute("topic", javaScriptTemplateService.getTemplateMap(CreateTopicModel.class));
		model.addAttribute("option", javaScriptTemplateService.getTemplateMap(EditOptionModel.class));
		model.addAttribute("processId", javaScriptTemplateService.getTemplateString("processId"));
		try
		{
			if (globalDefinitionSets == null)
			{
				globalDefinitionSets = ComparisonValueDefinitionSetModel.convert(definitionService.getGlobalSets());
			}
			model.addAttribute("globalDefinitionSets", ajaxObjectMapper.writeValueAsString(globalDefinitionSets));
		}
		catch (IOException e)
		{
			log.error(e);
		}
		return "jsTemplates/comparison";
	}
}
