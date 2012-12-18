package de.ifcore.argue.domain.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.enumerations.DiscussionType;
import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.domain.enumerations.License;
import de.ifcore.argue.domain.enumerations.TopicVisibility;
import de.ifcore.argue.domain.report.DiscussionTypeReport;
import de.ifcore.argue.domain.report.LogReport;
import de.ifcore.argue.domain.report.TopicAccessRightReport;
import de.ifcore.argue.domain.report.TopicReport;
import de.ifcore.argue.utils.EntityUtils;
import de.ifcore.argue.utils.UrlUtils;

@Entity
public final class Topic extends AbstractAuthoredEntity implements TopicReport, TopicEntity
{
	private static final long serialVersionUID = 1865617128633222705L;
	private static final BigDecimal BD_100 = new BigDecimal(100);

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn
	private TopicTerm term;

	@Column(nullable = false)
	private String termForUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private DiscussionType discussionType;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "pro_contra_fk")
	private ProContraTopic proContraTopic;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "comparison_fk")
	private ComparisonTopic comparisonTopic;

	@Column(nullable = false)
	private TopicVisibility visibility;

	@ManyToOne
	private Category category;

	@OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
	private Set<TopicAccessRight> accessRights;

	private EmbeddableAuthorAttribution authorAttribution;

	@Column(updatable = false)
	private Long idOfOriginal;

	@Column(nullable = false)
	private Integer numberOfViews;

	@OneToMany(mappedBy = "dedicatedEntity", cascade = CascadeType.PERSIST)
	private Set<TopicVote> votes;

	@Transient
	private transient Byte upVotesInPercent;

	Topic()
	{
	}

	public Topic(String term, String definition, DiscussionType discussionType, Author author,
			TopicVisibility visibility, Category category)
	{
		this(term, definition, discussionType, author, visibility, category, null, null);
	}

	private Topic(String term, String definition, DiscussionType discussionType, Author author,
			TopicVisibility visibility, Category category, Set<String> attributionList, Long idOfOriginal)
	{
		super(author);
		if (discussionType == null || visibility == null)
			throw new NullPointerException();
		this.discussionType = discussionType;
		this.term = new TopicTerm(term, definition, this, author);
		this.termForUrl = UrlUtils.slugify(term);
		this.visibility = visibility;
		this.category = category;
		this.authorAttribution = new EmbeddableAuthorAttribution(getAuthorName(), attributionList);
		this.idOfOriginal = idOfOriginal;
		this.numberOfViews = Integer.valueOf(1);
		new TopicAccessRight(this, author.getRegisteredUser(), EntityPermission.WRITE);
	}

	public static Topic asCopy(String term, String definition, DiscussionType discussionType, Author author,
			TopicVisibility visibility, Category category, Set<String> attributionList, Long idOfOriginal)
	{
		return new Topic(term, definition, discussionType, author, visibility, category, attributionList, idOfOriginal);
	}

	public Topic copy(Author author)
	{
		Topic topic = Topic.asCopy(getText(), getDefinition(), getDiscussionType(), author, TopicVisibility.PRIVATE,
				getCategory(), getAuthorList(), getId());
		if (proContraTopic != null)
			proContraTopic.copyTo(topic, 0);
		// TODO implement copy of comparisonTopic
		return topic;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public TopicTerm getTerm()
	{
		return term;
	}

	public boolean setTerm(TopicTerm term)
	{
		if (term != null && term.getTopic() != this)
			throw new IllegalArgumentException();
		if (term == null || !ObjectUtils.equals(getText(), term.getText())
				|| !ObjectUtils.equals(getDefinition(), term.getDefinition()))
		{
			if (term != null && this.term != null)
			{
				getAuthorAttribution()
						.addToAuthorList(term.getAuthorName(), this.term.getAuthorName(), getAuthorName());
			}
			this.term = term;
			return true;
		}
		return false;
	}

	public DiscussionType getDiscussionType()
	{
		return discussionType;
	}

	public ProContraTopic getProContraTopic()
	{
		return proContraTopic;
	}

	void setProContraTopic(ProContraTopic proContraTopic)
	{
		if (proContraTopic != null && (comparisonTopic != null || !this.equals(proContraTopic.getTopic())))
			throw new IllegalArgumentException();
		this.proContraTopic = proContraTopic;
	}

	public ComparisonTopic getComparisonTopic()
	{
		return comparisonTopic;
	}

	void setComparisonTopic(ComparisonTopic comparisonTopic)
	{
		if (comparisonTopic != null && (proContraTopic != null || !this.equals(comparisonTopic.getTopic())))
			throw new IllegalArgumentException();
		this.comparisonTopic = comparisonTopic;
	}

	@Override
	public Category getCategory()
	{
		return category;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Topic))
			return false;

		final Topic rhs = (Topic)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getAuthor(), rhs.getAuthor())
				.append(getDiscussionType(), rhs.getDiscussionType()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(3, 71).append(getCreatedAt()).append(getAuthor()).append(getDiscussionType())
				.toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Topic [term=").append(term).append(", discussionType=").append(discussionType).append("]");
		return builder.toString();
	}

	@Override
	public Long getTermId()
	{
		return term == null ? null : term.getId();
	}

	@Override
	public String getText()
	{
		return term == null ? null : term.getText();
	}

	@Override
	public String getDefinition()
	{
		return term == null ? null : term.getDefinition();
	}

	@Override
	public LogReport getModificationLog()
	{
		return term.isFirst() ? null : term;
	}

	@Override
	public LogReport getCreationLog()
	{
		return this;
	}

	@Override
	public TopicVisibility getVisibility()
	{
		return visibility;
	}

	public Set<TopicAccessRightReport> getAccessRights()
	{
		return accessRights == null ? Collections.<TopicAccessRightReport> emptySet() : Collections
				.<TopicAccessRightReport> unmodifiableSet(accessRights);
	}

	public boolean isPublic()
	{
		return TopicVisibility.PUBLIC.equals(visibility);
	}

	public TopicAccessRightReport getAccessRightOfUser(RegisteredUser user)
	{
		if (user == null)
			throw new IllegalArgumentException();

		for (TopicAccessRightReport accessRight : getAccessRights())
		{
			if (EntityUtils.equalsId(accessRight.getUser(), user))
				return accessRight;
		}
		return null;
	}

	public Set<Long> getUserIdsWithWriteAccess()
	{
		Set<Long> users = new HashSet<>();
		for (TopicAccessRightReport accessRight : getAccessRights())
		{
			if (EntityPermission.WRITE.equals(accessRight.getPermission()) && accessRight.getUserId() != null)
				users.add(accessRight.getUserId());
		}
		return Collections.unmodifiableSet(users);
	}

	public EntityPermission getGrantedPermissionOfUser(RegisteredUser user)
	{
		EntityPermission grantedPermission = null;
		if (user != null)
		{
			TopicAccessRightReport accessRight = getAccessRightOfUser(user);
			grantedPermission = accessRight == null ? null : accessRight.getPermission();
		}
		if (grantedPermission == null && isPublic())
		{
			grantedPermission = EntityPermission.READ;
		}
		return grantedPermission;
	}

	public static boolean isPermissionSufficient(EntityPermission requestedPermission,
			EntityPermission grantedPermission, RegisteredUser user)
	{
		if (requestedPermission == null)
			throw new NullPointerException();

		if (grantedPermission == null)
			return false;
		if (requestedPermission.equals(grantedPermission) || EntityPermission.READ.equals(requestedPermission))
			return true;
		if (EntityPermission.VOTE.equals(requestedPermission) && user != null)
			return true;
		return false;
	}

	void addAccessRight(TopicAccessRight accessRight)
	{
		if (accessRight == null || accessRight.getTopic() != this)
			throw new IllegalArgumentException();
		if (this.accessRights == null)
			this.accessRights = new HashSet<>();
		this.accessRights.add(accessRight);
	}

	public boolean edit(String term, String definition, Category category, Author author)
	{
		boolean termChanged = setTerm(new TopicTerm(term, definition, this, author));
		boolean categoryChanged = setCategory(category);
		return termChanged || categoryChanged;
	}

	private boolean setCategory(Category category)
	{
		if (!ObjectUtils.equals(this.category, category))
		{
			this.category = category;
			return true;
		}
		return false;
	}

	@Override
	public Long getTopicId()
	{
		return id;
	}

	public void setVisibility(TopicVisibility visibility)
	{
		if (visibility == null)
			throw new IllegalArgumentException();
		this.visibility = visibility;
	}

	@Override
	public Integer getNumberOfViews()
	{
		return numberOfViews;
	}

	public void increaseNumberOfViews(int inc)
	{
		this.numberOfViews = Integer.valueOf(this.numberOfViews.intValue() + inc);
	}

	@Override
	public DiscussionTypeReport getDiscussionTypeReport()
	{
		return (proContraTopic != null) ? proContraTopic : comparisonTopic;
	}

	@Override
	public License getLicense()
	{
		return TopicVisibility.PUBLIC.equals(visibility) ? License.CC : License.PRIVATE;
	}

	@Override
	public String getTermForUrl()
	{
		return termForUrl;
	}

	public EmbeddableAuthorAttribution getAuthorAttribution()
	{
		if (authorAttribution == null)
		{
			authorAttribution = new EmbeddableAuthorAttribution();
		}
		return authorAttribution;
	}

	@Override
	public boolean offerAttributionList()
	{
		return getAuthorAttribution().offerAttributionList(getAuthorList(),
				Arrays.asList(term.getAuthorName(), getAuthorName()));
	}

	@Override
	public Set<String> getAuthorList()
	{
		return getAuthorAttribution().getListOfAllAuthors(term.getAuthorName(), getAuthorName());
	}

	@Override
	public boolean isCopy()
	{
		return idOfOriginal != null;
	}

	@Override
	public Long getIdOfOriginal()
	{
		return idOfOriginal;
	}

	@Override
	public Boolean getVoteOfUser(Long userId)
	{
		TopicVote vote = getTopicVoteOfUser(userId);
		if (vote == null)
			return null;
		else
			return Boolean.valueOf(vote.isUpVote());
	}

	public TopicVote getTopicVoteOfUser(Long userId)
	{
		if (userId == null)
			throw new IllegalArgumentException();
		for (TopicVote vote : getVotes())
		{
			if (userId.equals(vote.getUserId()))
				return vote;
		}
		return null;
	}

	public Set<TopicVote> getVotes()
	{
		return votes == null ? Collections.<TopicVote> emptySet() : Collections.unmodifiableSet(votes);
	}

	public void addVote(TopicVote vote)
	{
		if (vote == null || vote.getDedicatedEntity() != this)
			throw new IllegalArgumentException();

		if (votes == null)
			votes = new HashSet<>();
		else if (EntityUtils.isDuplicateVote(votes, vote))
			throw new DuplicateVoteException();

		votes.add(vote);
		resetUpVotesInPercent();
	}

	public void removeVote(TopicVote vote)
	{
		if (vote == null || vote.getDedicatedEntity() != this)
			throw new IllegalArgumentException();

		if (votes.remove(vote))
			resetUpVotesInPercent();
	}

	public void resetUpVotesInPercent()
	{
		upVotesInPercent = null;
	}

	@Override
	public Byte getUpVotesInPercent()
	{
		if (this.upVotesInPercent == null)
		{
			int numberOfUpVotes = 0;
			for (TopicVote vote : getVotes())
			{
				if (vote.isUpVote())
					numberOfUpVotes++;
			}

			BigDecimal percent = getVotes().size() > 0 ? BigDecimal.ZERO : null;
			if (numberOfUpVotes > 0)
			{
				percent = new BigDecimal(numberOfUpVotes).divide(new BigDecimal(getVotes().size()), 2,
						RoundingMode.HALF_UP).multiply(BD_100);
			}

			this.upVotesInPercent = percent == null ? null : Byte.valueOf(percent.byteValue());
		}

		return this.upVotesInPercent;
	}
}
