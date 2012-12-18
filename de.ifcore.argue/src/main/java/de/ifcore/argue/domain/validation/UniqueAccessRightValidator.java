package de.ifcore.argue.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.ifcore.argue.dao.TopicDao;
import de.ifcore.argue.domain.entities.Topic;
import de.ifcore.argue.domain.form.AddTopicAccessRightForm;
import de.ifcore.argue.domain.report.TopicAccessRightReport;

public class UniqueAccessRightValidator implements ConstraintValidator<UniqueAccessRight, AddTopicAccessRightForm>
{
	private final TopicDao topicDao;

	@Inject
	public UniqueAccessRightValidator(TopicDao topicDao)
	{
		this.topicDao = topicDao;
	}

	@Override
	public void initialize(UniqueAccessRight constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(AddTopicAccessRightForm form, ConstraintValidatorContext context)
	{
		if (form.getId() != null)
		{
			Topic topic = topicDao.get(form.getId());
			if (topic != null)
			{
				for (TopicAccessRightReport accessRight : topic.getAccessRights())
				{
					if (accessRight.getContactName().equals(form.getContact()))
						return false;
				}
			}
		}
		return true;
	}
}
