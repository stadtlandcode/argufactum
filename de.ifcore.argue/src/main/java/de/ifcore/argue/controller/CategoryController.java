package de.ifcore.argue.controller;

import static de.ifcore.argue.utils.ControllerUtils.movedPermanently;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;

import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.CategoryUrl;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.models.view.CategoryModel;
import de.ifcore.argue.domain.models.view.TopicModel;
import de.ifcore.argue.services.CategoryService;

@Controller
public class CategoryController extends AbstractController
{
	public static final String categoryUrl = "/category/{id}/{name}";

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = categoryUrl, method = RequestMethod.GET)
	public ModelAndView getTopics(@PathVariable Long id, @PathVariable String name, Model model)
	{
		Category category = categoryService.get(id);
		if (category == null)
			throw new ResourceNotFoundException();

		if (!category.getNameForUrl().equals(name))
		{
			return movedPermanently(getUrl(category));
		}
		else
		{
			addHelpLocale(model);
			addUseMinifiedCssJs(model);
			model.addAttribute("category", new CategoryModel(category));
			model.addAttribute("topics", getTopicModels(categoryService.getPublicTopics(category.getId())));
			return new ModelAndView("category/topics");
		}
	}

	private Set<TopicModel> getTopicModels(Set<Topic> topics)
	{
		Set<TopicModel> topicModels = new LinkedHashSet<>();
		for (Topic topic : topics)
		{
			topicModels.add(new TopicModel(topic, messageSource));
		}
		return Collections.unmodifiableSet(topicModels);
	}

	public static String getUrl(CategoryUrl category)
	{
		return getUrl(category.getNameForUrl(), category.getId());
	}

	static String getUrl(String nameForUrl, Long id)
	{
		UriTemplate uriTemplate = new UriTemplate(categoryUrl);
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("name", nameForUrl);
		uriVariables.put("id", id.toString());
		return uriTemplate.expand(uriVariables).toString();
	}
}
