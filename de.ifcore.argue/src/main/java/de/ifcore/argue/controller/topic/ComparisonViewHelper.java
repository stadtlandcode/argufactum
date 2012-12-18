package de.ifcore.argue.controller.topic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.form.ComparisonValueForm;
import de.ifcore.argue.domain.form.CreateCriterionForm;
import de.ifcore.argue.domain.form.CreateOptionForm;
import de.ifcore.argue.domain.form.CriterionImportanceForm;
import de.ifcore.argue.domain.form.DeleteCriterionForm;
import de.ifcore.argue.domain.form.DeleteOptionForm;
import de.ifcore.argue.domain.models.UserSession;
import de.ifcore.argue.domain.models.view.CreateCriterionModel;
import de.ifcore.argue.domain.models.view.CreateOptionModel;
import de.ifcore.argue.domain.report.CriterionReport;
import de.ifcore.argue.domain.report.OptionReport;
import de.ifcore.argue.services.ComparisonValueDefinitionService;

@Component
public class ComparisonViewHelper implements TopicViewHelper
{
	private final UserSession userSession;
	private final ComparisonValueDefinitionService definitionService;

	@Inject
	public ComparisonViewHelper(UserSession userSession, ComparisonValueDefinitionService definitionService)
	{
		this.userSession = userSession;
		this.definitionService = definitionService;
	}

	@Override
	@Transactional(readOnly = true)
	public void addAttributes(Topic topic, Model model)
	{
		model.addAttribute(topic.getComparisonTopic());
		model.addAttribute("criteria", getCriteriaAttribute(topic));
		model.addAttribute("options", getOptionsAttribute(topic));
		model.addAttribute(new CreateCriterionForm());
		model.addAttribute(new DeleteCriterionForm());
		model.addAttribute(new CreateOptionForm());
		model.addAttribute(new DeleteOptionForm());
		model.addAttribute(new CriterionImportanceForm());
		model.addAttribute(new ComparisonValueForm());
	}

	private Set<CreateCriterionModel> getCriteriaAttribute(Topic topic)
	{
		Set<CreateCriterionModel> criteria = new LinkedHashSet<>();
		for (CriterionReport criteriaReport : topic.getComparisonTopic().getCriteria())
			criteria.add(new CreateCriterionModel(criteriaReport, userSession.getUser(), null, definitionService
					.getAdditionalSets(criteriaReport)));
		return Collections.unmodifiableSet(criteria);
	}

	private Set<CreateOptionModel> getOptionsAttribute(Topic topic)
	{
		Set<CreateOptionModel> options = new LinkedHashSet<>();
		for (OptionReport optionReport : topic.getComparisonTopic().getOptions())
			options.add(new CreateOptionModel(optionReport, null));
		return Collections.unmodifiableSet(options);
	}

	@Override
	public String getViewName()
	{
		return "topic/readComparison";
	}

	@Override
	public String getAjaxViewName()
	{
		return "topic/readComparisonAjax";
	}

	@Override
	public boolean isResponsible(Topic topic)
	{
		return DiscussionType.COMPARISON.equals(topic.getDiscussionType());
	}

}
