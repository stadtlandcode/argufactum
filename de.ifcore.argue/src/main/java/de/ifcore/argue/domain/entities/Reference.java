package de.ifcore.argue.domain.entities;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.ifcore.argue.domain.enumerations.LogEvent;
import de.ifcore.argue.domain.report.LogReport;
import de.ifcore.argue.domain.report.ReferenceReport;
import de.ifcore.argue.utils.UrlUtils;

@Entity
public final class Reference extends AbstractDeletableTopicEntity implements ReferenceReport, Comparable<Reference>,
		CopiableNestedEntity<Reference, Fact>
{
	private static final long serialVersionUID = 4555322096697537148L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(updatable = false)
	private String url;

	@Column(updatable = false, nullable = false)
	private String text;

	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private Fact fact;

	Reference()
	{
	}

	public static Reference instanceOf(String text, Fact fact, Author author)
	{
		String url = null;
		URL urlObject = UrlUtils.getUrl(text);
		if (urlObject != null)
		{
			url = urlObject.toString();
			text = urlObject.getHost();
			if (text.toLowerCase().startsWith("www."))
				text = text.substring(4);
		}

		return new Reference(text, url, fact, author, 0);
	}

	Reference(String text, String url, Fact fact, Author author, int timeDiff)
	{
		super(author, timeDiff);
		if (StringUtils.isBlank(text) || fact == null)
			throw new IllegalArgumentException();

		setUrl(url);
		this.text = StringUtils.normalizeSpace(text).toLowerCase();
		this.fact = fact;
		fact.addReference(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getUrl()
	{
		return url;
	}

	private void setUrl(String url)
	{
		URL urlObject = UrlUtils.getUrl(url);
		if (urlObject == null)
		{
			this.url = url;
		}
		else
		{
			this.url = urlObject.toString();
		}
	}

	@Override
	public String getText()
	{
		return text;
	}

	public Fact getFact()
	{
		return fact;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Reference))
			return false;

		final Reference rhs = (Reference)obj;
		return new EqualsBuilder().append(getCreatedAt(), rhs.getCreatedAt()).append(getText(), rhs.getText())
				.append(getUrl(), rhs.getUrl()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 79).append(getCreatedAt()).append(getText()).append(getUrl()).toHashCode();
	}

	@Override
	public String toString()
	{
		return "Reference [id=" + id + ", url=" + url + ", text=" + text + ", fact=" + fact + "]";
	}

	@Override
	public Long getFactId()
	{
		return fact == null ? null : fact.getId();
	}

	@Override
	public LogReport getCreationLog()
	{
		return this;
	}

	@Override
	public int compareTo(Reference o)
	{
		if (this.equals(o))
			return 0;

		int result = getCreationLog().getDateTime().compareTo(o.getCreationLog().getDateTime());
		if (result == 0)
			result = getText().compareTo(o.getText());
		if (result == 0)
			result = getUrl().compareTo(o.getUrl());
		if (result == 0)
			result = ObjectUtils.compare(getId(), o.getId());
		return result;
	}

	@Override
	public Long getTopicId()
	{
		return fact.getTopicId();
	}

	@Override
	protected void log(LogEvent logEvent, Author author)
	{
	}

	@Override
	public Reference copyTo(Fact destionationParent, int timeDiff)
	{
		return new Reference(getText(), getUrl(), destionationParent, destionationParent.getAuthor(), timeDiff);
	}
}
