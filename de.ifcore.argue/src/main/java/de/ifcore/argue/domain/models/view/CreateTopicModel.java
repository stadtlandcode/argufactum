package de.ifcore.argue.domain.models.view;

import org.springframework.context.MessageSource;

import de.ifcore.argue.controller.topic.TopicController;
import de.ifcore.argue.domain.enumerations.License;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.report.TopicReport;
import de.ifcore.argue.services.TopicService;
import de.ifcore.argue.utils.FormatUtils;

public class CreateTopicModel extends EditTopicModel
{
	private static final long serialVersionUID = 947851600093032294L;
	private static final Byte BYTE_ZERO = Byte.valueOf((byte)0);

	private final LogModel creationLog;
	private final TopicVisibility visibility;
	private final License license;
	private final AttributionModel attribution;
	private final String urlOfOriginal;
	private final String numberOfViews;
	private final Byte upVoteBarPercent;
	private final Byte downVoteBarPercent;
	private final Boolean userVote;

	public CreateTopicModel(TopicReport topic, Long userId, MessageSource messageSource, TopicService topicService)
	{
		super(topic, messageSource);
		this.creationLog = LogModel.instanceOf(topic.getCreationLog(), messageSource);
		this.visibility = topic.getVisibility();
		this.license = topic.getLicense();
		this.attribution = new AttributionModel(topic);
		this.urlOfOriginal = topic.isCopy() ? TopicController.getUrl(topicService.get(topic.getIdOfOriginal())) : null;
		this.numberOfViews = FormatUtils.formatNumber(topic.getNumberOfViews());
		this.upVoteBarPercent = topic.getUpVotesInPercent() == null ? BYTE_ZERO : topic.getUpVotesInPercent();
		this.downVoteBarPercent = topic.getUpVotesInPercent() == null ? BYTE_ZERO : Byte.valueOf((byte)(100 - topic
				.getUpVotesInPercent().intValue()));
		this.userVote = userId == null ? null : topic.getVoteOfUser(userId);
	}

	public LogModel getCreationLog()
	{
		return creationLog;
	}

	public TopicVisibility getVisibility()
	{
		return visibility;
	}

	public License getLicense()
	{
		return license;
	}

	public AttributionModel getAttribution()
	{
		return attribution;
	}

	public String getUrlOfOriginal()
	{
		return urlOfOriginal;
	}

	public String getNumberOfViews()
	{
		return numberOfViews;
	}

	public Byte getUpVoteBarPercent()
	{
		return upVoteBarPercent;
	}

	public Byte getDownVoteBarPercent()
	{
		return downVoteBarPercent;
	}

	public Boolean getUserVote()
	{
		return userVote;
	}
}
