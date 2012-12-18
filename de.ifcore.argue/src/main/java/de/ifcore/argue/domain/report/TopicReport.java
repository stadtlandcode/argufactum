package de.ifcore.argue.domain.report;

import de.ifcore.argue.domain.entities.AuthorAttribution;
import de.ifcore.argue.domain.entities.Category;
import de.ifcore.argue.domain.entities.TopicUrl;
import de.ifcore.argue.domain.enumerations.License;
import de.ifcore.argue.domain.enumerations.TopicVisibility;

public interface TopicReport extends EntityReport, TopicUrl, AuthorAttribution
{
	public Long getTermId();

	public String getText();

	public String getDefinition();

	public LogReport getModificationLog();

	public LogReport getCreationLog();

	public Category getCategory();

	public TopicVisibility getVisibility();

	public DiscussionTypeReport getDiscussionTypeReport();

	public Integer getNumberOfViews();

	public Byte getUpVotesInPercent();

	public Boolean getVoteOfUser(Long userId);

	public License getLicense();

	public boolean isCopy();

	public Long getIdOfOriginal();
}
