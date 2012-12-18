package de.ifcore.argue.domain.models.view;

import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.ifcore.argue.domain.entities.TopicUrl;
import de.ifcore.argue.domain.report.TopicReport;
import de.ifcore.argue.utils.FormatUtils;

public class TopicModel implements ViewModel, TopicUrl
{
	private static final long serialVersionUID = -1797461016063277589L;
	private static final Byte BYTE_ZERO = Byte.valueOf((byte)0);

	private final Long id;
	private final String term;
	private final String definition;
	private final CategoryModel category;
	private final LogModel creationLog;
	private final LogModel modificationLog;
	private final String labelOfMainEntities;
	private final Integer numberOfMainEntities;
	private final String labelOfSecondaryEntities;
	private final Integer numberOfSecondaryEntities;
	private final String numberOfViews;
	private final Byte upVoteBarPercent;
	private final Byte downVoteBarPercent;
	private final String termForUrl;

	public TopicModel(TopicReport topic, MessageSource messageSource)
	{
		Locale locale = LocaleContextHolder.getLocale();

		this.id = topic.getId();
		this.term = topic.getText();
		this.definition = topic.getDefinition();
		this.category = topic.getCategory() == null ? null : new CategoryModel(topic.getCategory());
		this.creationLog = LogModel.instanceOf(topic.getCreationLog(), messageSource);
		this.modificationLog = LogModel.instanceOf(topic.getModificationLog(), messageSource);
		this.labelOfMainEntities = messageSource.getMessage("topic.stats.main."
				+ topic.getDiscussionTypeReport().getNameOfMainEntities(), null, locale);
		this.numberOfMainEntities = topic.getDiscussionTypeReport().getNumberOfMainEntities();
		this.labelOfSecondaryEntities = messageSource.getMessage("topic.stats.secondary."
				+ topic.getDiscussionTypeReport().getNameOfSecondaryEntities(), null, locale);
		this.numberOfSecondaryEntities = topic.getDiscussionTypeReport().getNumberOfSecondaryEntities();
		this.numberOfViews = FormatUtils.formatNumber(topic.getNumberOfViews());
		this.upVoteBarPercent = topic.getUpVotesInPercent() == null ? BYTE_ZERO : topic.getUpVotesInPercent();
		this.downVoteBarPercent = topic.getUpVotesInPercent() == null ? BYTE_ZERO : Byte.valueOf((byte)(100 - topic
				.getUpVotesInPercent().intValue()));
		this.termForUrl = topic.getTermForUrl();
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public String getTerm()
	{
		return term;
	}

	public String getDefinition()
	{
		return definition;
	}

	public CategoryModel getCategory()
	{
		return category;
	}

	public String getLabelOfMainEntities()
	{
		return labelOfMainEntities;
	}

	public Integer getNumberOfMainEntities()
	{
		return numberOfMainEntities;
	}

	public String getLabelOfSecondaryEntities()
	{
		return labelOfSecondaryEntities;
	}

	public Integer getNumberOfSecondaryEntities()
	{
		return numberOfSecondaryEntities;
	}

	public String getNumberOfViews()
	{
		return numberOfViews;
	}

	public LogModel getCreationLog()
	{
		return creationLog;
	}

	public LogModel getModificationLog()
	{
		return modificationLog;
	}

	public Byte getUpVoteBarPercent()
	{
		return upVoteBarPercent;
	}

	public Byte getDownVoteBarPercent()
	{
		return downVoteBarPercent;
	}

	public String getText()
	{
		return getTerm();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof TopicModel))
			return false;

		final TopicModel rhs = (TopicModel)obj;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(5, 71).append(getId()).toHashCode();
	}

	@Override
	public String getTermForUrl()
	{
		return termForUrl;
	}

	@Override
	public String toString()
	{
		return "TopicModel [id=" + id + ", term=" + term + "]";
	}
}
